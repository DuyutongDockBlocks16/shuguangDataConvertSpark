package cn.jj.simulation;

import cn.jj.simulation.utils.BDRead;
import org.apache.hadoop.io.compress.SnappyCodec;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * @program: JavaSOTest
 * @description:
 * @author: wangyb04
 * @create: 2021-04-06 18:09
 */
public class SplitToHive {

//    public static String[] RETURN_TYPE = {"Hero","Creep","Monster","Tower","Error"};
    public static String[] RETURN_TYPE = {"Hero","Tower"};

    public static void main(String[] args) throws Exception {

        String year = args[0];
        String month = args[1];
        String day = args[2];
        int partition = Integer.valueOf(args[3]);
        String version = args[4];
        String part = args[5];

        SparkConf conf = new SparkConf().setAppName("SplitToHive"+part);
        conf.set("mapreduce.map.output.compress", "false");
        conf.set("mapreduce.output.fileoutputformat.compress", "false");
        conf.set("mapred.map.output.compress", "false");
        conf.set("mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.map.output.compress", "false");
        conf.set("spark.hadoop.mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.output.compress", "false");
        JavaSparkContext sc = new JavaSparkContext(conf);

        System.out.println("---- gagsimu version: "+version+"----");

        String game_server_version = BDRead.get_game_server_version(version);
        String game_big_version_short = BDRead.get_game_big_version_short(version);
        String game_key_version = BDRead.get_game_key_version(version);

        JavaRDD<String> totalRDD = sc.textFile("hdfs:///staging/shuguang/rec_parse/ods/game/state/delta/"+ year + "/" + month + "/" + day+"/"+game_server_version+"/"+game_big_version_short+"/"+game_key_version+"/"+part);
        for (String type: RETURN_TYPE) {
            JavaRDD<String> res_data = totalRDD.filter(d -> {
                String d_type = d.split(":")[0];
                if (type.equals(d_type)) {
                    return true;
                }
                return false;
            }).map(d-> {
                return d.substring(d.indexOf(":")+1, d.length());
            });
            res_data.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/ods/game/"+type+"_state/delta/"+ year + "/" + month + "/" + day+"/"+game_server_version+"/"+game_big_version_short+"/"+game_key_version+"/"+part);
        }
    }
}
