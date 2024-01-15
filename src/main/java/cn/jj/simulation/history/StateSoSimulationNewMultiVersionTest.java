package cn.jj.simulation.history;

import cn.jj.simulation.utils.BDRead;
import org.apache.spark.*;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: JavaSOTest
 * @description: 模拟曙光state的so模块功能，线程安全
 * @author: wangyb04
 * @create: 2021-05-13 10:12
 */
public class StateSoSimulationNewMultiVersionTest {

    public static void main(String[] args) throws Exception {
        SparkConf conf = new SparkConf().setAppName("StateSoSimulationNew");
        JavaSparkContext sc = new JavaSparkContext(conf);
        // 分区字段
        String year = args[0];
        String month = args[1];
        String day = args[2];
        // task 并发数量
        int partition = Integer.valueOf(args[3]);
        // gagsimu 文件名（包含版本号）
//        String f_name = args[4];
        String f_name = "20210601144922_formal_1.0.6.2_6001_ce347f5d_2b00d8c1_cd81619b.tar.gz";
        // gagsimu 对应的版本号（只能处理对应版本的bd文件）
        String version = f_name.substring(0, f_name.indexOf(".tar"));
        System.out.println("---- gagsimu version: "+version+"----");
        // 读HDFS上的bd文件
        // TODO har文件和普通小文件读取的性能对比
//      JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("har:///staging/shuguang/rec/archive/"+year+"-"+month+"-"+day+".har/00/");
      JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/"+year+"-"+month+"-"+day+"/00/");
//        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/tmp/51011750014327.bd");
        // 每一个bd文件，先用版本号过滤，再flat为pb字节流的集合，并且cache，供多个action使用
        // TODO 需要测试一下flatMap是否会重新分发key，产生网络损耗
        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_repartition_pairRDD = bdBinaryFiles_pairRDD.partitionBy(new HashPartitioner(partition));
        JavaPairRDD<String, byte[]> pb_bytes_pairRDD = bdBinaryFiles_repartition_pairRDD.flatMapValues(b_stream -> {
            BDRead BDRead = new BDRead();
//            String bd_version = byteRead.get_version(b_stream.toArray());
            byte[] data = BDRead.get_data(b_stream.toArray());
            List<byte[]> pb_bytes_list = new ArrayList<byte[]>();
            pb_bytes_list.add(data);
            return pb_bytes_list;
        });
        // 缓存二进制RDD，避免多个action重复解析bd文件
        pb_bytes_pairRDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/"+ year + "/" + month + "/" + day +"/"+version+"/ods_total");
    }
}
