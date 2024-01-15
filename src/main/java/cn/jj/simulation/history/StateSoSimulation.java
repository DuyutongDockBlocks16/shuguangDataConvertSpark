package cn.jj.simulation.history;

import cn.jj.simulation.test.OutFuncI;
import cn.jj.simulation.utils.BdUtils;
import cn.jj.simulation.utils.DateUtils;
import cn.jj.simulation.utils.GamePbUtils;
import cn.jj.simulation.utils.StringUtils;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;
import org.apache.spark.storage.StorageLevel;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: JavaSOTest
 * @description: 模拟曙光state的so模块功能
 * @author: wangyb04
 * @create: 2021-05-13 10:12
 */
public class StateSoSimulation {

    public static void main(String[] args) throws Exception {
        SparkConf conf = new SparkConf().setAppName("StateSoSimulation");
        JavaSparkContext sc = new JavaSparkContext(conf);
        String year = args[0];
        String month = args[1];
        String day = args[2];
        // 读HDFS上的bd文件
        // TODO har文件和普通小文件读取的性能对比
        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/tmp/51011750014327.bd");
        // 每一个bd文件，flat为pb字节流的集合，并且cache，供多个action使用
        JavaPairRDD<String, byte[]> pb_bytes_pairRDD = bdBinaryFiles_pairRDD.flatMapValues(b_stream -> {
            InputStream input_stream = b_stream.open();
            BdUtils bdUtils = new BdUtils();
            List<byte[]> pb_bytes_list = new ArrayList<byte[]>();
            // 游戏command解析，底层调用录像工具so
            bdUtils.analysis(input_stream, new OutFuncI() {
                @Override
                public void OutputFunc(Pointer msg, NativeLong sz) {
                    byte[] pb_bytes = bdUtils.getPbBytes(msg, sz);
                    if (pb_bytes != null && pb_bytes.length != 0) {
                        pb_bytes_list.add(pb_bytes);
                    } else {
                        System.out.println("---- invalid msg");
                    }
                }
            });
            return pb_bytes_list;
        });
        // 缓存二进制RDD，避免多个action重复调用录像工具so
        pb_bytes_pairRDD.persist(StorageLevel.MEMORY_ONLY_2());
        // TODO 需要测试一下flatMap是否会重新分发key，产生网络损耗
        // heroState_RDD
        JavaRDD<String> heroState_RDD = pb_bytes_pairRDD.flatMap(pair-> {
            String battle_id = StringUtils.getSplitIndex(pair._1.substring(0, pair._1.length()-3), "/", -1);
            byte[] pb_bytes = pair._2;
            List<String> heroState_list = GamePbUtils.getHeroStateList(battle_id, pb_bytes);
            return heroState_list.iterator();
        });
//        List<String> res = heroState_RDD.collect();
//        for(String r: res) {
//            System.out.println("heroState: "+r);
//        }
        // TODO 待规范hdfs路径，以及设置压缩格式
        heroState_RDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/"+ year + "/" + month + "/" + day +"/ods_stateHero");
        // creepState_RDD
        JavaRDD<String> creepState_RDD = pb_bytes_pairRDD.flatMap(pair -> {
            String battle_id = StringUtils.getSplitIndex(pair._1.substring(0, pair._1.length() - 3), "/", -1);
            byte[] pb_bytes = pair._2;
            List<String> creepState_list = GamePbUtils.getCreepStateList(battle_id, pb_bytes);
            return creepState_list.iterator();
        });
        creepState_RDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/"+ year + "/" + month + "/" + day +"/ods_stateCreep");

        // monsterState_RDD
        JavaRDD<String> monsterState_RDD = pb_bytes_pairRDD.flatMap(pair -> {
            String battle_id = StringUtils.getSplitIndex(pair._1.substring(0, pair._1.length() - 3), "/", -1);
            byte[] pb_bytes = pair._2;
            List<String> monsterState_list = GamePbUtils.getMonsterStateList(battle_id, pb_bytes);
            return monsterState_list.iterator();
        });
        monsterState_RDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/"+ year + "/" + month + "/" + day +"/ods_stateMonster");

        // monsterState_RDD
        JavaRDD<String> towerState_RDD = pb_bytes_pairRDD.flatMap(pair -> {
            String battle_id = StringUtils.getSplitIndex(pair._1.substring(0, pair._1.length() - 3), "/", -1);
            byte[] pb_bytes = pair._2;
            List<String> towerStateList = GamePbUtils.getTowerStateList(battle_id, pb_bytes);
            return towerStateList.iterator();
        });
        towerState_RDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/"+ year + "/" + month + "/" + day +"/ods_stateTower");
//        pb_bytes_pairRDD.unpersist();
    }
}
