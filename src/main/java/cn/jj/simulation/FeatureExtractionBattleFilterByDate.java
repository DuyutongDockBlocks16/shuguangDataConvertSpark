package cn.jj.simulation;

import cn.jj.simulation.entity.ODS_Battle;
import cn.jj.simulation.entity.ODS_MatchingEnsure;
import cn.jj.simulation.entity.ODS_PlayerBattle;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.LongAccumulator;
import scala.Tuple2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: JavaSOTest
 * @description: 筛选曙光录像文件
 * @author: wangyb04
 * @create: 2021-05-13 10:12
 * @editor: duyt
 */
public class FeatureExtractionBattleFilterByDate {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static void main(String[] args) throws Exception {

        // 格式为：yyyyMMdd
        String start_date_str = args[0];//起始，例如，如果过滤2021-06-01到2021-11-01的数据是，这里就是20210601
        String end_date_str = args[1];//结束，例如，如果过滤2021-06-01到2021-11-01的数据是，这里就是20211101
//        黄泉：28
        String hero_id = args[2];
        String elo = args[3];
//        最低的elo分数，暂时为1300
//        int elo = Integer.valueOf(args[3]);
        BigDecimal elo_DB=new BigDecimal(elo);
//        格式：[108,117,118,119,121,122,127,136]
        String modetid_battle = args[4];
        String[] modetid_battle_arr = modetid_battle.substring(1, modetid_battle.length() - 1).split(",");
        Map<String, Integer> modetid_battle_map = new HashMap<String, Integer>();
        for (String mb: modetid_battle_arr) {
            modetid_battle_map.put(mb, 1);
        }
        String version = args[5]; ///版本号
//        prod 和 local，local有每个过滤rdd的cache，可以统计过滤后的结果，做sql同步对比用，会消耗性能，prod没有cache
        String mode = args[6];
        String score_value = args[7];
//        float score_value=Float.valueOf(args[7]);
        BigDecimal score_value_DB=new BigDecimal(score_value);
        String date_section = build_date_section(start_date_str, end_date_str);

        SparkConf conf = new SparkConf().setAppName(FeatureExtractionBattleFilterByDate.class.getSimpleName()+"_"+mode+"_"+version);
        JavaSparkContext sc = new JavaSparkContext(conf);

        LongAccumulator filter1_accum = sc.sc().longAccumulator("playerbattle_filter");
        LongAccumulator filter2_accum = sc.sc().longAccumulator("matchingensure_filter");

        // 从服务端日志中筛选对局
        //  1. 筛选playerBattle日志
        JavaRDD<String> player_battle_RDD = sc.textFile("hdfs:///staging/shuguang/server/100055/PlayerBattle/"+date_section);
        JavaPairRDD<String, Integer> battle_id_RDD1 = player_battle_RDD.map(line->{
            return new ODS_PlayerBattle(line);
        }).filter(pb->{
            if ("28".equals(pb.herotid) && modetid_battle_map.containsKey(pb.modetid) &&  pb.score.compareTo(score_value_DB)!=-1  &&  "10".equals(pb.usernum)  ) {
                return true;
            } else {
                filter1_accum.add(1);
                return false;
            }
        }).mapToPair(pb->{
            return new Tuple2<String, Integer>(pb.battleid+"\001"+pb.role_id,1);
        });
//
        if ("local".equals(mode)) {
            long filter1_count = battle_id_RDD1.cache().count();
            System.out.println("---- playerbattle_filter_count: "+filter1_count);
        }
        //  2. 筛选matchEnsure日志
        JavaRDD<String> matching_ensure_RDD = sc.textFile("hdfs:///staging/shuguang/server/100055/MatchingEnsure/"+date_section);
        JavaPairRDD<String, Integer> battle_id_RDD2 = matching_ensure_RDD.map(line -> {
            return new ODS_MatchingEnsure(line);
        }).filter(me->{
            if ( me.team_elo.compareTo(elo_DB) != -1 ) {
                return true;
            } else {
                filter1_accum.add(1);
                return false;
            }
        }).mapToPair(me -> {
            return new Tuple2<String,Integer>(me.matching_id+"\001"+me.role_id,1);
        });
//            join,并且值保留battleid
        battle_id_RDD1.join(battle_id_RDD2).map(tuple -> {
            return tuple._1.split("\001")[0];
        }).distinct().coalesce(1).saveAsTextFile("hdfs:///staging/shuguang/rec_parse/feature_extraction/battle_total/"+hero_id+"/"+version+"/"+start_date_str+"-"+end_date_str+"/");
        System.out.println("---- playerbattle_filter: "+filter1_accum.value());
        System.out.println("---- matchingensure_filter: "+filter2_accum.value());
    }

    private static String build_date_section(String start_date_str, String end_date_str) {
        LocalDate end_date = LocalDate.parse(end_date_str, DATE_TIME_FORMATTER);
        LocalDate battle_date = LocalDate.parse(start_date_str, DATE_TIME_FORMATTER);
        StringBuilder sb = new StringBuilder("{");
        while(!battle_date.isAfter(end_date)) {
            sb.append(battle_date.getYear()+"/"+(battle_date.getMonthValue()<10?"0"+battle_date.getMonthValue():battle_date.getMonthValue()+"")+"/"+(battle_date.getDayOfMonth()<10?"0"+battle_date.getDayOfMonth():battle_date.getDayOfMonth()+"")+"/");
            sb.append(",");
            battle_date = battle_date.plusDays(1);
        }
        return sb.substring(0, sb.length()-1)+"}";
    }
}
