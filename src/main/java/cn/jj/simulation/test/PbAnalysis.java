package cn.jj.simulation.test;

import cn.jj.simulation.test.OutFuncI;
import cn.jj.simulation.utils.BDRead;
import cn.jj.simulation.utils.BdUtils;
import cn.jj.simulation.utils.ByteUtils;
import cn.jj.simulation.utils.GameThinPbUtils;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.apache.hadoop.io.BytesWritable;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PbAnalysis {

    public static void main(String[] args) throws Exception {

        String year = args[0];
        String month = args[1];
        String day = args[2];
        // task 并发数量
        int partition = Integer.valueOf(args[3]);
        String file_name = args[4];

        SparkConf conf = new SparkConf().setAppName("PbAnalysis");
        conf.set("mapreduce.map.output.compress", "false");
        conf.set("mapreduce.output.fileoutputformat.compress", "false");
        conf.set("mapred.map.output.compress", "false");
        conf.set("mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.map.output.compress", "false");
        conf.set("spark.hadoop.mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.output.compress", "false");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> pb_RDD = sc.textFile("hdfs:///staging/shuguang/rec_parse/pb/game/state/delta/2021/10/19/00/test/part-00000").repartition(1);

        // 每一个bd文件，先用版本号过滤，再flat为pb字节流的集合，最后flat为解析数据的集合
        String first = pb_RDD.first();
        byte[] pb_bytes = ByteUtils.hexString2Bytes(first);
        List<String> state_list = new ArrayList<String>();
        String battle_id = "10001";
        state_list.addAll(GameThinPbUtils.getStateList(battle_id, pb_bytes));
        for (String state: state_list) {
            System.out.println("state2: "+state);
        }
    }
}
