package cn.jj.simulation;

import cn.jj.simulation.test.OutFuncI;
import cn.jj.simulation.utils.*;
import com.google.protobuf.GeneratedMessage;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.apache.spark.HashPartitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkFiles;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.input.PortableDataStream;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 1. 多个task来处理小文件，而不是按照block大小分发task
 * 2. executor的内存设置的大些
 * @program: JavaSOTest
 * @description: 模拟曙光state的so模块功能，线程安全
 * @author: wangyb04
 * @create: 2021-05-13 10:12
 */
public class StateSimulationNew {

    public static void main(String[] args) throws Exception {

        String year = args[0];
        String month = args[1];
        String day = args[2];
        // task 并发数量
        int partition = Integer.valueOf(args[3]);
        String version = args[4];
        //        0~9，0代表小分区00~09的集合
        String part = args[5];

        SparkConf conf = new SparkConf().setAppName("StateSimulation"+part);
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

        // 读HDFS上的bd文件
        // TODO har文件和普通小文件读取的性能对比
        // 测试文件 51012257660900.bd
//        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("har:///staging/shuguang/rec/archive/"+year+"-"+month+"-"+day+".har/"+part+"*/");
        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/"+year+"-"+month+"-"+day+"/"+part+"*/");
//        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/"+year+"-"+month+"-"+day+"/20/51013017825720.bd");
//        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/tmp/");
        JavaRDD<PortableDataStream> bdBinaryFiles_repartition_pairRDD = bdBinaryFiles_pairRDD.values().repartition(partition);

        // 每一个bd文件，先用版本号过滤，再flat为pb字节流的集合，最后flat为解析数据的集合
        JavaRDD<String> state_RDD = bdBinaryFiles_repartition_pairRDD.filter(b_stream-> {
            String bd_version = BDRead.get_version(b_stream.toArray());
            System.out.println("---- bd_version: "+bd_version+"");
            if (bd_version!=null && version.equals(bd_version)) return true;
            return false;
        }).flatMap(b_stream -> {
            List<String> state_list = new ArrayList<String>();
            String bd_version = BDRead.get_version(b_stream.toArray());
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
                            state_list.addAll(GameThinPbUtils.getStateList(battle_id, pb_bytes));
                        } else {
                            System.out.println("---- invalid msg");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("---- exception ----");
            }
            return state_list.iterator();
        });
        state_RDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/ods/game/state/delta/"+ year + "/" + month + "/" + day+"/"+game_server_version+"/"+game_big_version_short+"/"+game_key_version+"/"+part);
    }
}
