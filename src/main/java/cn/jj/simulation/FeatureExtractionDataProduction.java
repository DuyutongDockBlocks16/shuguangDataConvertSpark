package cn.jj.simulation;

import cn.jj.simulation.test.OutFuncI;
import cn.jj.simulation.utils.*;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.input.PortableDataStream;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: JavaSOTest
 * @description: 对筛选的录像文件做解析
 * @author: wangyb04
 * @create: 2021-05-13 10:12
 */
public class FeatureExtractionDataProduction {

    public static void main(String[] args) throws Exception {

        String year = args[0];
        String month = args[1];
        String day = args[2];
        // task 并发数量=该日期的对局数量
        String version = args[3];
        int partition = Integer.valueOf(args[4]);
        //        0~9，0代表小分区00~09的集合
        String hero_id = args[5];
        String extract_verion = args[6]; ///版本号

        SparkConf conf = new SparkConf().setAppName(FeatureExtractionDataProduction.class.getSimpleName());
        conf.set("mapreduce.map.output.compress", "false");
        conf.set("mapreduce.output.fileoutputformat.compress", "false");
        conf.set("mapred.map.output.compress", "false");
        conf.set("mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.map.output.compress", "false");
        conf.set("spark.hadoop.mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.output.compress", "false");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // gagsimu 对应的版本号（只能处理对应版本的bd文件）
        System.out.println("---- gagsimu version: "+version+"----");

        String game_server_version = BDRead.get_game_server_version(version);
        String game_big_version_short = BDRead.get_game_big_version_short(version);
        String game_key_version = BDRead.get_game_key_version(version);

//        读取日期对应的经过筛选的battle_id[0-9][0-9]
        JavaRDD<String> battle_total_RDD = sc.textFile("hdfs:///staging/shuguang/rec_parse/feature_extraction/battle_total/" + hero_id + "/" + extract_verion+ "/" + year + "/" + month + "/" + day + "/");
        List<String> battle_total_collect = battle_total_RDD.collect();
        int partition_index = 0;
//        key: battle_id，value: partititon_index
        Map<String, Integer> battle_total_map = new HashMap<String, Integer>();
        // 1. 构建battle_id的partition_index
        // 2. 构建需要读取hdfs路径上的对局集合
        StringBuilder battle_sb = new StringBuilder("{");
        for (String battle_id: battle_total_collect) {
            battle_total_map.put(battle_id, partition_index);
            partition_index++;
            battle_sb.append(battle_id).append(".bd").append(",");
        }
        String battle_str = battle_sb.toString().substring(0, battle_sb.length()-1)+"}";
        System.out.println("---- battle_str: "+battle_str);
//        battld_id:partition_index 结构的map，用于repartition，确保数据不倾斜，每个task只处理一场对局
        Broadcast<Map<String, Integer>> bc = sc.broadcast(battle_total_map);

        // 读HDFS上的筛选之后的bd文件，bd集合来自于上一步筛选的battle_id
//        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/tmp/*");
        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("har:///staging/shuguang/rec/archive/"+year+"-"+month+"-"+day+".har/*/"+battle_str);
//        版本过滤
        JavaPairRDD<String, String> state_pairRDD = bdBinaryFiles_pairRDD.filter(bdBinaryFiles -> {
            PortableDataStream b_stream = bdBinaryFiles._2;
            String bd_version = BDRead.get_version(b_stream.toArray());
//            String bd_version = "formal_1.0.5_b335f93828c3260886266237397b3c85";
            System.out.println("---- bd_version: " + bd_version + "");
            if (bd_version != null && version.equals(bd_version)) return true;
            return false;
        }).mapToPair(bdBinaryFiles -> {
//            构建带partition_index的新的key
            String file_name = bdBinaryFiles._1;
            String battle_id = StringUtils.getSplitIndex(file_name.substring(0, file_name.length() - 3), "/", -1);
            return new Tuple2<String, PortableDataStream>(battle_id+":"+bc.value().get(battle_id), bdBinaryFiles._2);
        }).partitionBy(new BattleIdPartitioner(partition)).flatMapValues(b_stream -> {
//            按照partition_index进行repartition
//            解析bd文件
            List<String> state_list = new ArrayList<String>();
            String bd_version = BDRead.get_version(b_stream.toArray());
//            String bd_version = "formal_1.0.5_b335f93828c3260886266237397b3c85";
            String battle_id = BDRead.get_head_line(b_stream.toArray(), 1);
            byte[] data = BDRead.get_data(b_stream.toArray());
            // 游戏command解析，底层调用录像工具so
            BdUtils bdUtils = new BdUtils();
            try {
                bdUtils.analysis(data, bd_version, new OutFuncI() {
                    @Override
                    public void OutputFunc(Pointer msg, NativeLong sz) {
                        byte[] pb_bytes = bdUtils.getPbBytes(msg, sz);
                        if (pb_bytes != null && pb_bytes.length != 0) {
//                            字节数组转换为16进制字符串，之后的数据处理就用16进制字符串转成字节数组
                            state_list.add(ByteUtils.bytes2HexString(pb_bytes));
                        } else {
                            System.out.println("---- invalid msg");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("---- exception ----");
            }
            return state_list;
        });
        String HDFS_output_path = "hdfs:///staging/shuguang/rec_parse/feature_extraction/data/" + hero_id + "/" + extract_verion + "/" + year + "/" + month + "/" + day + "/" + game_server_version + "/" + game_big_version_short + "/" + game_key_version;
        state_pairRDD.saveAsHadoopFile(HDFS_output_path, String.class, String.class, RDDMultipleTextOutputFormat.class, GzipCodec.class);
//        state_pairRDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/ods/game/state/delta/"+ year + "/" + month + "/" + day+"/"+game_server_version+"/"+game_big_version_short+"/"+game_key_version+"/"+part);
    }
}
