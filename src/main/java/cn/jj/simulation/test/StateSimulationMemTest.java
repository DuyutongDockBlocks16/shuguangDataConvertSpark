package cn.jj.simulation.test;

import cn.jj.simulation.utils.BDRead;
import cn.jj.simulation.utils.BdUtils;
import cn.jj.simulation.utils.GameNewPbUtils;
import cn.jj.simulation.utils.StringUtils;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.apache.spark.HashPartitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkFiles;
import org.apache.spark.TaskContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.executor.Executor;
import org.apache.spark.input.PortableDataStream;
import org.apache.spark.memory.UnifiedMemoryManager;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * 1. 多个task来处理小文件，而不是按照block大小分发task
 * 2. executor的内存设置的大些
 * @program: JavaSOTest
 * @description: 模拟曙光state的so模块功能，线程安全
 * @author: wangyb04
 * @create: 2021-05-13 10:12
 */
public class StateSimulationMemTest {

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
        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("har:///staging/shuguang/rec/archive/"+year+"-"+month+"-"+day+".har/"+part+"*/");
//        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/tmp/");

        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_repartition_pairRDD = bdBinaryFiles_pairRDD.partitionBy(new HashPartitioner(partition));

        // 每一个bd文件，先用版本号过滤，再flat为pb字节流的集合，最后flat为解析数据的集合
        JavaPairRDD<String, byte[]> pb_bytes_pairRDD = bdBinaryFiles_repartition_pairRDD.flatMapValues(b_stream -> {
            List<byte[]> pb_bytes_list = new ArrayList<byte[]>();
            byte[] data = BDRead.get_data(b_stream.toArray());

            // 游戏command解析，底层调用录像工具so
            for (int i=0; i<10000; i++) {
                pb_bytes_list.add(data);
            }
            return pb_bytes_list;
        });

        // TODO 需要测试一下flatMap是否会重新分发key，产生网络损耗
        JavaRDD<String> state_RDD = pb_bytes_pairRDD.flatMap(pair-> {
            byte[] pb_bytes = pair._2;
            List<String> state_list =  new ArrayList<String>();
            for(int i=0; i<2000; i++) {
                state_list.add(pb_bytes.toString());
            }
            return state_list.iterator();
        });

        long JVM_used_memory = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
        long on_heap_execution_memory = UnifiedMemoryManager.apply(conf, 1).onHeapExecutionMemoryPool().memoryUsed();
        long on_heap_storage_memory = UnifiedMemoryManager.apply(conf, 1).onHeapStorageMemoryPool().memoryUsed();
        long task_attempt_id = TaskContext.get().taskAttemptId();
        System.out.println("---- task_attempt_id: "+task_attempt_id+", JVM_used_memory: "+JVM_used_memory/1024/1024+", on_heap_execution_memory: "+on_heap_execution_memory/1024/1024+", on_heap_storage_memory: "+on_heap_storage_memory);

        state_RDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/ods/game/state/test/delta/"+ year + "/" + month + "/" + day+"/"+game_server_version+"/"+game_big_version_short+"/"+game_key_version+"/"+part);
    }

    private static void testRootDirectory() {
        String rootDirectory = SparkFiles.getRootDirectory();
        File root_path = new File(rootDirectory);
        for (File file: root_path.listFiles()) {
            System.out.println("---- file name: "+file.getName());
        }
    }
}
