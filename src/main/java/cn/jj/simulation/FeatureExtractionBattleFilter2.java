package cn.jj.simulation;

import cn.jj.simulation.entity.ODS_PlayerBattle;
import cn.jj.simulation.test.OutFuncI;
import cn.jj.simulation.utils.*;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import net.sf.json.JSONObject;
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
 * @description: 对modetid_battle做筛选，迭代筛选
 * @author: wangyb04
 * @create: 2021-05-13 10:12
 */
public class FeatureExtractionBattleFilter2 {

    public static void main(String[] args) throws Exception {

        String year = args[0];
        String month = args[1];
        String day = args[2];
        // task 并发数量=该日期的对局数量
        //        0~9，0代表小分区00~09的集合
        String hero_id = args[3];

        SparkConf conf = new SparkConf().setAppName(FeatureExtractionBattleFilter2.class.getSimpleName());
        conf.set("mapreduce.map.output.compress", "false");
        conf.set("mapreduce.output.fileoutputformat.compress", "false");
        conf.set("mapred.map.output.compress", "false");
        conf.set("mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.map.output.compress", "false");
        conf.set("spark.hadoop.mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.output.compress", "false");
        JavaSparkContext sc = new JavaSparkContext(conf);

//        读取日期对应的经过筛选的battle_id[0-9][0-9]
        JavaRDD<String> battle_total_RDD = sc.textFile("hdfs:///staging/shuguang/rec_parse/feature_extraction/battle_total/" + hero_id + "/"+year + "/" + month + "/" + day+"/");
//        JavaRDD<String> battle_total_RDD = sc.textFile("hdfs:///staging/shuguang/rec_parse/feature_extraction/battle_total/" + hero_id + "/2021/*/*/");
        List<String> battle_total_collect = battle_total_RDD.collect();

        Map<String, Integer> battle_total_map = new HashMap<String, Integer>();
        // 1. 构建battle_id的map
        for (String battle_id: battle_total_collect) {
            battle_total_map.put(battle_id, 1);
        }
        Broadcast<Map<String, Integer>> bc = sc.broadcast(battle_total_map);
        System.out.println("---- battle_total size: "+battle_total_collect.size());

        JavaRDD<String> player_battle_RDD = sc.textFile("hdfs:///staging/shuguang/server/100055/PlayerBattle/"+year + "/" + month + "/" + day+"/");
//        JavaRDD<String> player_battle_RDD = sc.textFile("hdfs:///staging/shuguang/server/100055/PlayerBattle/2021/{06,07,08,09,10}/*/");
        JavaRDD<String> cache = player_battle_RDD.filter(line -> {
            Map<String, Integer> bt_map = bc.value();
            JSONObject lineObj = JSONUtil.JSONString2JSONObject(line);
            String battleid = lineObj.get("battleid") == null ? "" : lineObj.get("battleid").toString();
            String modetid_battle = lineObj.get("modetid_battle") == null ? "" : lineObj.get("modetid_battle").toString();
            if (!bt_map.containsKey(battleid)) {
                return false;
            }
            if ("108".equals(modetid_battle) ||
                    "117".equals(modetid_battle) ||
                    "118".equals(modetid_battle) ||
                    "119".equals(modetid_battle) ||
                    "121".equals(modetid_battle) ||
                    "122".equals(modetid_battle) ||
                    "127".equals(modetid_battle) ||
                    "136".equals(modetid_battle)
            ) {
                return false;
            }
            System.out.println("---- modetid_battle: " + modetid_battle);
            return true;
        }).map(line -> {
            JSONObject lineObj = JSONUtil.JSONString2JSONObject(line);
            String battleid = lineObj.get("battleid") == null ? "" : lineObj.get("battleid").toString();
            return battleid;
        }).distinct().cache();
        long count = cache.count();
        System.out.println("---- filter count: "+count);
        cache.coalesce(1).saveAsTextFile("hdfs:///staging/shuguang/rec_parse/feature_extraction/battle_total_modetid_filter/"+hero_id+"/"+year + "/" + month + "/" + day+"/");
    }
}
