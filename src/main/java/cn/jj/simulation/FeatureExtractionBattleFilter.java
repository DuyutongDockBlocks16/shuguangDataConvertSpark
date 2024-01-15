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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @program: JavaSOTest
 * @description: 筛选曙光录像文件
 * @author: wangyb04
 * @create: 2021-05-13 10:12
 * @editor: duyt
 */
@Deprecated
public class FeatureExtractionBattleFilter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static void main(String[] args) throws Exception {

        // 格式为：yyyyMMdd
        String year = args[0];
        String month = args[1];
        String day = args[2];
        String start_date_str = year+month+day;//起始，例如，如果过滤2021-06-01到2021-11-01的数据是，这里就是20210601

//        黄泉：28
        String hero_id = args[3];
//        最低的elo分数，暂时为1300
        int elo = Integer.valueOf(args[4]);
        int mode_tid = Integer.valueOf(args[5]);
//        暂时定的7万场
        int battle_min = Integer.valueOf(args[6]);
        String end_date_str = args[7];//结束，例如，如果过滤2021-06-01到2021-11-01的数据是，这里就是20211101
        String extract_verion = args[8]; ///版本号
        SparkConf conf = new SparkConf().setAppName(FeatureExtractionBattleFilter.class.getSimpleName());
        conf.set("mapreduce.map.output.compress", "false");
        conf.set("mapreduce.output.fileoutputformat.compress", "false");
        conf.set("mapred.map.output.compress", "false");
        conf.set("mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.map.output.compress", "false");
        conf.set("spark.hadoop.mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.output.compress", "false");
        JavaSparkContext sc = new JavaSparkContext(conf);
//        符合筛选条件的对局总数
        int battle_total = 0;
//        每个日期的符合筛选条件的对局数量
        List<String> battle_date_count = new ArrayList<String>();

        LocalDate start_date = LocalDate.parse(start_date_str, DATE_TIME_FORMATTER);
        LocalDate battle_date = LocalDate.parse(start_date_str, DATE_TIME_FORMATTER);
        LocalDate end_date = LocalDate.parse(end_date_str, DATE_TIME_FORMATTER);
        LongAccumulator filter1_accum = sc.sc().longAccumulator("playerbattle_filter");
        LongAccumulator filter2_accum = sc.sc().longAccumulator("matchingensure_filter");
        LongAccumulator filter3_accum = sc.sc().longAccumulator("battle_filter");

        // 判断对局数量
        while ( !battle_date.isAfter(end_date) ) {

            // 从服务端日志中筛选对局
            //  1. 筛选playerBattle日志
            JavaRDD<String> player_battle_RDD = sc.textFile("hdfs:///staging/shuguang/server/100055/PlayerBattle/" + battle_date.getYear() + "/" + (battle_date.getMonthValue()<10?"0"+battle_date.getMonthValue():battle_date.getMonthValue()+"") + "/" + (battle_date.getDayOfMonth()<10?"0"+battle_date.getDayOfMonth():battle_date.getDayOfMonth()+""));
            JavaPairRDD<String, Integer> battle_id_RDD1 = player_battle_RDD.map(line->{
                return new ODS_PlayerBattle(line);
            }).mapToPair(pb->{
                return new Tuple2<String, ODS_PlayerBattle>(pb.battleid, pb);
            }).groupByKey().filter(pb_pair->{
                Iterable<ODS_PlayerBattle> iterable = pb_pair._2;
                boolean contains_hero = false;
                for (ODS_PlayerBattle pb: iterable) {
//                    modetid_battle筛选
                    if (!"108".equals(pb.modetid_battle) &&
                            !"117".equals(pb.modetid_battle) &&
                            !"118".equals(pb.modetid_battle) &&
                            !"119".equals(pb.modetid_battle) &&
                            !"121".equals(pb.modetid_battle) &&
                            !"122".equals(pb.modetid_battle) &&
                            !"127".equals(pb.modetid_battle) &&
                            !"136".equals(pb.modetid_battle)
                    ) {
                        filter1_accum.add(1);
                        return false;
                    }
//                    ai_type筛选
                    if ("0".equals(pb.ai_type)) {
                        filter1_accum.add(1);
                        return false;
                    }
//                    hero_id筛选
                    if (!contains_hero && hero_id.equals(pb.herotid)) contains_hero = true;
                }
                if (contains_hero) return true;
                filter1_accum.add(1);
                return false;
            }).mapValues(value->1);
//            long filter1_count = battle_id_RDD1.cache().count();
//            System.out.println("---- playerbattle_filter_count: "+filter1_count);
            //  2. 筛选matchEnsure日志
            JavaRDD<String> matching_ensure_RDD = sc.textFile("hdfs:///staging/shuguang/server/100055/MatchingEnsure/" + battle_date.getYear() + "/" + (battle_date.getMonthValue()<10?"0"+battle_date.getMonthValue():battle_date.getMonthValue()+"") + "/" + (battle_date.getDayOfMonth()<10?"0"+battle_date.getDayOfMonth():battle_date.getDayOfMonth()+""));
            JavaPairRDD<String, Integer> battle_id_RDD2 = matching_ensure_RDD.map(line -> {
                return new ODS_MatchingEnsure(line);
            }).mapToPair(me -> {
                return new Tuple2<String, ODS_MatchingEnsure>(me.matching_id, me);
            }).groupByKey().filter(me_pair -> {
                Iterable<ODS_MatchingEnsure> iterable = me_pair._2;
                float match_elo_total = 0;
                int size = 0;
                for (ODS_MatchingEnsure me : iterable) {
//                    success和ensure筛选
                    if (!"1".equals(me.success) || "-1".equals(me.ensure)) {
                        filter2_accum.add(1);
                        return false;
                    }
                    match_elo_total += me.team_elo;
                    size++;
                }
                float match_elo = 0;
                if (size >0) match_elo = match_elo_total / size;
//                match_elo筛选
                if (match_elo >= elo) {
                    return true;
                } else {
                    filter2_accum.add(1);
                    return false;
                }
            }).mapValues(value -> 1);
//            long filter2_count = battle_id_RDD2.cache().count();
//            System.out.println("---- matchingensure_filter_count: "+filter2_count);
            //  3. 筛选battle日志
            JavaRDD<String> battle_RDD = sc.textFile("hdfs:///staging/shuguang/server/100055/Battle/" + battle_date.getYear() + "/" + (battle_date.getMonthValue()<10?"0"+battle_date.getMonthValue():battle_date.getMonthValue()+"") + "/" + (battle_date.getDayOfMonth()<10?"0"+battle_date.getDayOfMonth():battle_date.getDayOfMonth()+"/"));
            JavaPairRDD<String, Integer> battle_id_RDD3 = battle_RDD.map(line -> {
                return new ODS_Battle(line);
            }).filter(battle -> {
//            筛选挂机人数
                if ("0".equals(battle.illegal_player_1)) {
                    return true;
                } else {
                    filter3_accum.add(1);
                    return false;
                }
            }).mapToPair(battle -> {
                return new Tuple2<String, Integer>(battle.battleid, 1);
            });
//            long filter3_count = battle_id_RDD3.cache().count();
//            System.out.println("---- battle_filter_count: "+filter3_count);
//            join
            List<String> battle_id_list = battle_id_RDD1.join(battle_id_RDD2).join(battle_id_RDD3).map(tuple -> {
                return tuple._1;
            }).distinct().collect();
            System.out.println("bt_list: size: "+battle_id_list.size()+", data: "+String.join(",", battle_id_list));
//        对局上传至HDFS
            sc.parallelize(battle_id_list).coalesce(1).saveAsTextFile("hdfs:///staging/shuguang/rec_parse/feature_extraction/battle_total/"+hero_id+"/"+extract_verion+"/"+battle_date.getYear() + "/" + (battle_date.getMonthValue()<10?"0"+battle_date.getMonthValue():battle_date.getMonthValue()+"") + "/" + (battle_date.getDayOfMonth()<10?"0"+battle_date.getDayOfMonth():battle_date.getDayOfMonth()+"")+"/");
//            累加对局数量
            battle_total+=battle_id_list.size();
//            新增对局日期统计
            battle_date_count.add(battle_date.format(DATE_TIME_FORMATTER)+":"+battle_id_list.size());
//            日期+1
            battle_date = battle_date.plusDays(1);
            System.out.println("---- battle_total: "+battle_total);
            System.out.println("---- playerbattle_filter: "+filter1_accum.value());
            System.out.println("---- matchingensure_filter: "+filter2_accum.value());
            System.out.println("---- battle_filter: "+filter3_accum.value());

        }
//        对局日期和对局数量上传至HDFS
        sc.parallelize(battle_date_count).coalesce(1).saveAsTextFile("hdfs:///staging/shuguang/rec_parse/feature_extraction/battle_date_count/"+hero_id+"/"+extract_verion+"/"+start_date.getYear() + "/" + (start_date.getMonthValue()<10?"0"+start_date.getMonthValue():start_date.getMonthValue()+"") + "/" + (start_date.getDayOfMonth()<10?"0"+start_date.getDayOfMonth():start_date.getDayOfMonth()+"")+"/");
    }
}
