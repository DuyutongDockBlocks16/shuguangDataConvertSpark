package cn.jj.simulation;

import cn.jj.simulation.utils.BDRead;
import org.apache.hadoop.fs.FileSystem;
import org.apache.spark.HashPartitioner;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-08-10 10:45
 */
public class Main {
    public static void main(String[] args) throws Exception {

        String year = args[0];
        String month = args[1];
        String day = args[2];
        // task 并发数量
//        int partition = Integer.valueOf(args[3]);
//        0~9，0代表小分区00~09的集合
        String part = args[3];
        SparkConf conf = new SparkConf().setAppName("Main"+part);
        conf.set("mapreduce.map.output.compress", "false");
        conf.set("mapreduce.output.fileoutputformat.compress", "false");
        conf.set("mapred.map.output.compress", "false");
        conf.set("mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.map.output.compress", "false");
        conf.set("spark.hadoop.mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.output.compress", "false");
        JavaSparkContext sc = new JavaSparkContext(conf);

//        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("har:///staging/shuguang/rec/archive/"+year+"-"+month+"-"+day+".har/"+part+"*/");
//        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/"+year+"-"+month+"-"+day+"/"+part+"/"+file_name);
        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/"+year+"-"+month+"-"+day+"/"+part+"*/");
//        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/tmp/");
//        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_repartition_pairRDD = bdBinaryFiles_pairRDD.partitionBy(new HashPartitioner(partition));
        // 获取bd文件版本
        JavaPairRDD<String, Integer> version_pairRDD = bdBinaryFiles_pairRDD.mapToPair(bd_binaryfiles -> {
            PortableDataStream b_stream = bd_binaryfiles._2;
            String version = BDRead.get_version(b_stream.toArray());
            return new Tuple2<String, Integer>(version, 1);
        }).filter(version->{
            if (BDRead.UNKNOWN_VERSION.equals(version)) return false;
            return true;
        });
        // shuffle获得每个版本的bd文件数量（数量暂时没有业务需求，留着以后用）
        JavaPairRDD<String, Integer> version_count_pairRDD = version_pairRDD.reduceByKey((v1, v2) -> v1 + v2);
        version_count_pairRDD.coalesce(1).map(version-> {
            return version._1+'\001'+version._2;
        }).saveAsTextFile("hdfs:///staging/shuguang/rec_parse/ods/game/version/"+ year + "/" + month + "/" + day+"/"+part);
    }
}
