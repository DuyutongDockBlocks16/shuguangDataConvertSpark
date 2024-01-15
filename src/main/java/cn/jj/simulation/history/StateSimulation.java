package cn.jj.simulation.history;

import cn.jj.simulation.test.OutFuncI;
import cn.jj.simulation.utils.BDRead;
import cn.jj.simulation.utils.BdUtils;
import cn.jj.simulation.utils.GameNewPbUtils;
import cn.jj.simulation.utils.StringUtils;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.apache.spark.*;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
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
public class StateSimulation {

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
//        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/tmp/");

        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_repartition_pairRDD = bdBinaryFiles_pairRDD.partitionBy(new HashPartitioner(partition));

        // 每一个bd文件，先用版本号过滤，再flat为pb字节流的集合，最后flat为解析数据的集合
        JavaPairRDD<String, byte[]> pb_bytes_pairRDD = bdBinaryFiles_repartition_pairRDD.filter(bd_binaryfiles-> {
            // 测试root_directory
//            testRootDirectory();
            PortableDataStream b_stream = bd_binaryfiles._2;
            String bd_version = BDRead.get_version(b_stream.toArray());
            System.out.println("---- bd_version: "+bd_version+"");
            if (bd_version!=null && version.equals(bd_version)) return true;
            return false;
        })
                .flatMapValues(b_stream -> {
            List<byte[]> pb_bytes_list = new ArrayList<byte[]>();
            String bd_version = BDRead.get_version(b_stream.toArray());
            byte[] data = BDRead.get_data(b_stream.toArray());
            // 游戏command解析，底层调用录像工具so
            BdUtils bdUtils = new BdUtils();
            try {
                bdUtils.analysis(data, bd_version, new OutFuncI() {
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
                if(Thread.currentThread().isInterrupted()) {
                    System.out.println("---- interrupted ----");
                }
                if(!Thread.currentThread().isAlive()) {
                    System.out.println("---- no alive ----");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("---- exception ----");
            }
            return pb_bytes_list;
        });

        // TODO 需要测试一下flatMap是否会重新分发key，产生网络损耗
        JavaRDD<String> state_RDD = pb_bytes_pairRDD.flatMap(pair-> {
            String battle_id = StringUtils.getSplitIndex(pair._1.substring(0, pair._1.length()-3), "/", -1);
            byte[] pb_bytes = pair._2;
            List<String> state_list =  GameNewPbUtils.getStateList(battle_id, pb_bytes);
//            long JVM_used_memory = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();
//            long on_heap_execution_memory = UnifiedMemoryManager.apply(conf, 2).onHeapExecutionMemoryPool().memoryUsed();
//            long on_heap_storage_memory = UnifiedMemoryManager.apply(conf, 2).onHeapStorageMemoryPool().memoryUsed();
//            long on_heap_execution_memory2 = UnifiedMemoryManager.apply(conf, 2).executionMemoryUsed();
//            long on_heap_storage_memory2 = UnifiedMemoryManager.apply(conf, 2).storageMemoryUsed();
//            long task_attempt_id = TaskContext.get().taskAttemptId();
//            System.out.println("---- task_attempt_id: "+task_attempt_id+", JVM_used_memory: "+JVM_used_memory/1024/1024+", on_heap_execution_memory: "+on_heap_execution_memory+", on_heap_storage_memory: "+on_heap_storage_memory+", on_heap_execution_memory2: "+on_heap_execution_memory2+", on_heap_storage_memory2: "+on_heap_storage_memory2);
            return state_list.iterator();
        });

        state_RDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/ods/game/state/delta/"+ year + "/" + month + "/" + day+"/"+game_server_version+"/"+game_big_version_short+"/"+game_key_version+"/"+part);
    }

    private static void testRootDirectory() {
        String rootDirectory = SparkFiles.getRootDirectory();
        File root_path = new File(rootDirectory);
        for (File file: root_path.listFiles()) {
            System.out.println("---- file name: "+file.getName());
        }
    }
}
