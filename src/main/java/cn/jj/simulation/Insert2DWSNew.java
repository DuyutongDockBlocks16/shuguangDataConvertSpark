//package cn.jj.simulation;
//
//import cn.jj.simulation.entity.HeroState;
//import cn.jj.simulation.utils.*;
//import net.sf.json.JSONObject;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaPairRDD;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.broadcast.Broadcast;
//import org.apache.spark.storage.StorageLevel;
//import org.apache.spark.util.SizeEstimator;
//import scala.Tuple2;
//import scala.Tuple3;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @program: JavaSOTest
// * @description:
// * @author: wangyb04
// * @create: 2021-04-06 18:09
// */
//public class Insert2DWSNew {
//
////    public static String[] RETURN_TYPE = {"Hero","Creep","Monster","Tower","Error"};
//    public static String[] RETURN_TYPE = {"Hero","Tower"};
//
//    public static void main(String[] args) throws Exception {
//
//        String year = args[0];
//        String month = args[1];
//        String day = args[2];
//        int partition = Integer.valueOf(args[3]);
//        String version = args[4];
//        String part = args[5];
//        int hosting_minute_section_interval = Integer.valueOf(args[6]);
//
//
//        SparkConf conf = new SparkConf().setAppName("Insert2DWS"+part);
//        conf.set("mapreduce.map.output.compress", "false");
//        conf.set("mapreduce.output.fileoutputformat.compress", "false");
//        conf.set("mapred.map.output.compress", "false");
//        conf.set("mapred.output.fileoutputformat.compress", "false");
//        conf.set("spark.hadoop.mapred.map.output.compress", "false");
//        conf.set("spark.hadoop.mapred.output.fileoutputformat.compress", "false");
//        conf.set("spark.hadoop.mapred.output.compress", "false");
//        JavaSparkContext sc = new JavaSparkContext(conf);
//
//        System.out.println("---- gagsimu version: "+version+"----");
//
//        String game_server_version = BDRead.get_game_server_version(version);
//        String game_big_version_short = BDRead.get_game_big_version_short(version);
//        String game_key_version = BDRead.get_game_key_version(version);
////        broadcast user 自定义数据结构
//        Map<String, Map<String, String>> bcMap = new HashMap<String, Map<String, String>>();
//
////        获取小表
////        获取维表：(没有用到码表，用函数直接计算的码表数据)
////        获取server表：
//        JavaRDD<String> playerBattle_RDD = sc.textFile("hdfs:///staging/shuguang/server/100055/PlayerBattle/" + year + "/" + month + "/" + day);
//        bcMap.put("playerBattle", MapJoinBuilder.build_player_battle_map(playerBattle_RDD));
//
//        JavaRDD<String> userArenaEndStat_RDD = sc.textFile("hdfs:///staging/shuguang/server/100055/UserArenaEndStat/" + year + "/" + month + "/" + day);
//        bcMap.put("userArenaEndStat", MapJoinBuilder.build_userArenaEndStat_map(userArenaEndStat_RDD, hosting_minute_section_interval));
//
//        JavaRDD<String> battle_RDD = sc.textFile("hdfs:///hive/warehouse/shuguang.db/shuguang_dwd_game_battle_d_delta/year=" + year + "/month=" + month + "/day=" + day);
//        bcMap.put("battle", MapJoinBuilder.build_battle_map(battle_RDD));
//        System.out.println("---- bcMap size: "+ SizeEstimator.estimate(bcMap)+"bytes");
////        封装broadcast
//        Broadcast<Map<String, Map<String, String>>> bc = sc.broadcast(bcMap);
////        构建 hero_state 大表
//        JavaRDD<String> hero_state_RDD = sc.textFile("hdfs:///staging/shuguang/rec_parse/ods/game/Hero_state/delta/"+ year + "/" + month + "/" + day+"/"+game_server_version+"/"+game_big_version_short+"/"+game_key_version+"/"+part);
//        JavaRDD<HeroState> hero_state_rich_RDD = hero_state_RDD.coalesce(partition).map(line -> {
//            return new HeroState(line);
//        });
//        hero_state_rich_RDD.persist(StorageLevel.DISK_ONLY());
//        hero_state_rich_RDD.count();
////        map_appear 业务
//        hero_state_rich_RDD.mapToPair(hero_state_rich -> {
//            JSONObject richObj = JSONUtil.JSONString2JSONObject(hero_state_rich);
//            JSONObject transObj = new JSONObject();
//            transObj.put("battle_id", richObj.get("battle_id").toString());
//            transObj.put("area_id", richObj.get("area_id").toString());
//            transObj.put("bs_area", "");
//            transObj.put("second_col", richObj.get("second_col").toString());
//            transObj.put("minute_col", richObj.get("minute_col").toString());
//            transObj.put("camp_id", richObj.get("camp_id").toString());
//            transObj.put("hero_id", richObj.get("hero_id").toString());
//            transObj.put("race_id", richObj.get("race_id").toString());
//            transObj.put("appear", richObj.get("appear").toString());
//            String playerBattle = bc.value().get("playerBattle").get(richObj.get("battle_id").toString() + ":" + richObj.get("hero_id").toString());
//            if (StringUtils.isEmpty(playerBattle)) {
//                transObj.put("ai_type", "");
//                transObj.put("ai_base", "");
//                transObj.put("deepainum", "");
//            } else {
//                JSONObject playerBattleObj = JSONUtil.JSONString2JSONObject(playerBattle);
//                transObj.put("ai_type", playerBattleObj.get("ai_type").toString());
//                transObj.put("ai_base", playerBattleObj.get("ai_base").toString());
//                transObj.put("deepainum", playerBattleObj.get("deepainum").toString());
//            }
//            String battle = bc.value().get("battle").get(richObj.get("battle_id").toString());
//            if (StringUtils.isEmpty(battle)) {
//                transObj.put("pool_id", "");
//                transObj.put("match_elo", "");
//                transObj.put("mode_tid", "");
//                transObj.put("battle_start_time", "");
//            } else {
//                JSONObject battleObj = JSONUtil.JSONString2JSONObject(battle);
//                transObj.put("pool_id", battleObj.get("pool_id").toString());
//                transObj.put("match_elo", battleObj.get("match_elo").toString());
//                transObj.put("mode_tid", battleObj.get("mode_tid").toString());
//                transObj.put("battle_start_time", battleObj.get("battle_start_time").toString());
//            }
//            String groupby_key = transObj.get("battle_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("area_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("camp_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("second_col").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("minute_col").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("pool_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("match_elo").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("hero_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("race_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("ai_type").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("ai_base").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("deepainum").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("bs_area").toString();
//            return new Tuple2<String, Integer>(groupby_key, Integer.valueOf(transObj.get("appear").toString()));
//        }).reduceByKey((v1, v2) -> {
//            return v1 + v2;
//        }).map(tuple -> {
//            String groupby_key = tuple._1;
//            int appear = tuple._2;
//            return groupby_key + HiveUtils.FIELDS_TERMINATED + (appear>0?1:0);
//        }).saveAsTextFile("hdfs:///staging/shuguang/rec_parse/dws/game/second_map_appear/delta/"+ year + "/" + month + "/" + day+"/"+game_server_version+"/"+game_big_version_short+"/"+game_key_version+"/"+part);
////        tower_aim 业务
//
//        JavaRDD<String> tower_state_RDD = sc.textFile("hdfs:///hive/warehouse/shuguang.db/shuguang_dwd_game_t_tower_state_d_delta/game_server_version=" + game_server_version + "/game_big_version_short=" + game_big_version_short + "/game_key_version=" + game_key_version + "/year=" + year + "/month=" + month + "/day=" + day + "/part=" + part);
//        JavaPairRDD<String, String> tower_state_pairRDD = tower_state_RDD.coalesce(partition).mapToPair(tower_state -> {
//            String[] lines = tower_state.split(String.valueOf(HiveUtils.FIELDS_TERMINATED));
//            JSONObject transObj = new JSONObject();
//            transObj.put("Id", lines[3]);
//            transObj.put("camp_id", lines[4]);
//            transObj.put("type", lines[5]);
//            transObj.put("map_area_id", lines[8]);
//            transObj.put("tower_id", lines[9]);
//            String join_Key = lines[0]
//                    + ":" + lines[15]
//                    + ":" + lines[2];
//            return new Tuple2<String, String>(join_Key, JSONUtil.JSONObject2JSONString(transObj));
//        });
//        JavaPairRDD<String, String> hero_state_rich_pairRDD = hero_state_rich_RDD.mapToPair(hero_state_rich -> {
//            JSONObject richObj = JSONUtil.JSONString2JSONObject(hero_state_rich);
//            JSONObject transObj = new JSONObject();
//            transObj.put("camp_id", richObj.get("camp_id"));
//            transObj.put("hero_id", richObj.get("hero_id"));
//            transObj.put("race_id", richObj.get("race_id"));
//            transObj.put("hp_section", richObj.get("hp_section"));
//            transObj.put("mana_section", richObj.get("mana_section"));
//            String join_Key = richObj.get("battle_id").toString()
//                    + ":" + richObj.get("id").toString()
//                    + ":" + richObj.get("frame_no").toString();
//            return new Tuple2<String, String>(join_Key, JSONUtil.JSONObject2JSONString(transObj));
//        });
//
//        JavaPairRDD<String, Tuple2<String, String>> join_RDD = hero_state_rich_pairRDD.join(tower_state_pairRDD);
//        join_RDD.mapToPair(join -> {
//            String join_key = join._1;
//            String hero_state_rich = join._2._1;
//            String tower_state = join._2._2;
//            JSONObject richObj = JSONUtil.JSONString2JSONObject(hero_state_rich);
//            JSONObject transObj = new JSONObject();
//            String battle_id = join_key.split(":")[0];
//            String frame_no = join_key.split(":")[2];
//            transObj.put("battle_id", battle_id);
//            transObj.put("second_col", DimUtils.battle_time.get_second(frame_no));
//            transObj.put("minute_col", DimUtils.battle_time.get_minute(frame_no));
//            transObj.put("camp_id", richObj.get("camp_id").toString());
//            transObj.put("hero_id", richObj.get("hero_id").toString());
//            transObj.put("race_id", richObj.get("race_id").toString());
//            transObj.put("hp_section", richObj.get("hp_section").toString());
//            transObj.put("mana_section", richObj.get("mana_section").toString());
//
//            JSONObject tower_state_Obj = JSONUtil.JSONString2JSONObject(tower_state);
//            transObj.put("tower_inside_id", tower_state_Obj.get("Id").toString());
//            transObj.put("tower_camp_id", tower_state_Obj.get("camp_id").toString());
//            transObj.put("type", tower_state_Obj.get("type").toString());
//            transObj.put("tower_area_id", tower_state_Obj.get("map_area_id").toString());
//            transObj.put("tower_id", tower_state_Obj.get("tower_id").toString());
//            transObj.put("aim_count", 1);
//
//            String playerBattle = bc.value().get("playerBattle").get(battle_id + ":" + richObj.get("hero_id").toString());
//            if (StringUtils.isEmpty(playerBattle)) {
//                transObj.put("ai_type", "");
//                transObj.put("ai_base", "");
//                transObj.put("deepainum", "");
//            } else {
//                JSONObject playerBattleObj = JSONUtil.JSONString2JSONObject(playerBattle);
//                transObj.put("ai_type", playerBattleObj.get("ai_type").toString());
//                transObj.put("ai_base", playerBattleObj.get("ai_base").toString());
//                transObj.put("deepainum", playerBattleObj.get("deepainum").toString());
//            }
//            String battle = bc.value().get("battle").get(battle_id);
//            if (StringUtils.isEmpty(battle)) {
//                transObj.put("pool_id", "");
//                transObj.put("match_elo", "");
//                transObj.put("mode_tid", "");
//                transObj.put("battle_start_time", "");
//            } else {
//                JSONObject battleObj = JSONUtil.JSONString2JSONObject(battle);
//                transObj.put("pool_id", battleObj.get("pool_id").toString());
//                transObj.put("match_elo", battleObj.get("match_elo").toString());
//                transObj.put("mode_tid", battleObj.get("mode_tid").toString());
//                transObj.put("battle_start_time", battleObj.get("battle_start_time").toString());
//            }
//            String groupby_key = transObj.get("battle_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("second_col").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("minute_col").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("camp_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("tower_inside_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("tower_camp_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("type").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("tower_area_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("pool_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("match_elo").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("hero_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("race_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("ai_type").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("ai_base").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("deepainum").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("hp_section").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("mana_section").toString();
//            return new Tuple2<String, Integer>(groupby_key, Integer.valueOf(transObj.get("aim_count").toString()));
//        }).reduceByKey((v1, v2) -> {
//            return v1 + v2;
//        }).map(tuple -> {
//            String groupby_key = tuple._1;
//            int aim_count = tuple._2;
//            return groupby_key + HiveUtils.FIELDS_TERMINATED + aim_count;
//        }).saveAsTextFile("hdfs:///staging/shuguang/rec_parse/dws/game/second_tower_aim/delta/"+ year + "/" + month + "/" + day+"/"+game_server_version+"/"+game_big_version_short+"/"+game_key_version+"/"+part);
//
//        // tp业务
//        hero_state_rich_RDD.mapToPair(hero_state_rich -> {
//            JSONObject richObj = JSONUtil.JSONString2JSONObject(hero_state_rich);
//            JSONObject transObj = new JSONObject();
//            transObj.put("battle_id", richObj.get("battle_id").toString());
//            transObj.put("area_id", richObj.get("area_id").toString());
//            transObj.put("bs_area", "");
//            transObj.put("camp_id", richObj.get("camp_id").toString());
//            transObj.put("hero_id", richObj.get("hero_id").toString());
//            transObj.put("race_id", richObj.get("race_id").toString());
//            transObj.put("frame_no", richObj.get("frame_no").toString());
//            transObj.put("second_col", richObj.get("second_col").toString());
//            transObj.put("minute_col", richObj.get("minute_col").toString());
//            transObj.put("tp_begintime", richObj.get("tp_begintime").toString());
//            transObj.put("tp_endtime", richObj.get("tp_endtime").toString());
//            transObj.put("hp_percent", richObj.get("hp_percent").toString());
//            transObj.put("mana_percent", richObj.get("mana_percent").toString());
//            String playerBattle = bc.value().get("playerBattle").get(richObj.get("battle_id").toString() + ":" + richObj.get("hero_id").toString());
//            if (StringUtils.isEmpty(playerBattle)) {
//                transObj.put("ai_type", "");
//                transObj.put("ai_base", "");
//                transObj.put("deepainum", "");
//            } else {
//                JSONObject playerBattleObj = JSONUtil.JSONString2JSONObject(playerBattle);
//                transObj.put("ai_type", playerBattleObj.get("ai_type").toString());
//                transObj.put("ai_base", playerBattleObj.get("ai_base").toString());
//                transObj.put("deepainum", playerBattleObj.get("deepainum").toString());
//            }
//            String battle = bc.value().get("battle").get(richObj.get("battle_id").toString());
//            if (StringUtils.isEmpty(battle)) {
//                transObj.put("pool_id", "");
//                transObj.put("match_elo", "");
//                transObj.put("mode_tid", "");
//                transObj.put("battle_start_time", "");
//            } else {
//                JSONObject battleObj = JSONUtil.JSONString2JSONObject(battle);
//                transObj.put("pool_id", battleObj.get("pool_id").toString());
//                transObj.put("match_elo", battleObj.get("match_elo").toString());
//                transObj.put("mode_tid", battleObj.get("mode_tid").toString());
//                transObj.put("battle_start_time", battleObj.get("battle_start_time").toString());
//            }
//            String groupby_key = transObj.get("battle_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("area_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("bs_area").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("camp_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("hero_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("race_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("pool_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("match_elo").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("ai_type").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("ai_base").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("deepainum").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("tp_begintime").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("tp_endtime").toString();
//            String reduce_value = transObj.get("frame_no").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("hp_percent").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("mana_percent").toString();
//            return new Tuple2<String, Tuple3<String, String, String>>(groupby_key,
//                    new Tuple3<String, String, String>(transObj.get("frame_no").toString(),
//                            transObj.get("hp_percent").toString(),
//                            transObj.get("mana_percent").toString()));
//        }).groupByKey().mapToPair(tuple -> {
//            String groupby_key = tuple._1;
//            Iterable<Tuple3<String, String, String>> reduce_values = tuple._2;
//            int min_hp_percent = -1;
//            int min_mana_percent = -1;
//            int min_frame_no = -1;
//            String groupby_key_new = groupby_key.substring(0, groupby_key.substring(0, groupby_key.lastIndexOf(HiveUtils.FIELDS_TERMINATED)).lastIndexOf(HiveUtils.FIELDS_TERMINATED));
//
//            for (Tuple3<String, String, String> tuple3: reduce_values) {
//                String frame_no = tuple3._1();
//                String hp_percent = tuple3._2();
//                String mana_percent = tuple3._3();
//                if (!StringUtils.isEmpty(frame_no)) if (min_frame_no==-1 || Integer.valueOf(frame_no) < min_frame_no) min_frame_no = Integer.valueOf(frame_no);
//                if (!StringUtils.isEmpty(hp_percent)) if (min_hp_percent==-1 || Integer.valueOf(hp_percent) < min_hp_percent) min_hp_percent = Integer.valueOf(hp_percent);
//                if (!StringUtils.isEmpty(mana_percent)) if (min_mana_percent==-1 || Integer.valueOf(mana_percent) < min_mana_percent) min_mana_percent = Integer.valueOf(mana_percent);
//
//            }
//            String reduce_value = (min_hp_percent==-1?"":DimUtils.hp_section.get_hp_section(String.valueOf(min_hp_percent)))
//                    +HiveUtils.FIELDS_TERMINATED+(min_mana_percent==-1?"":DimUtils.mana_section.get_mana_section(String.valueOf(min_mana_percent)))
//                    +HiveUtils.FIELDS_TERMINATED+DimUtils.battle_time.get_second(String.valueOf(min_frame_no))
//                    +HiveUtils.FIELDS_TERMINATED+DimUtils.battle_time.get_minute(String.valueOf(min_frame_no));
//            return new Tuple2<String, Integer>(groupby_key_new + HiveUtils.FIELDS_TERMINATED + reduce_value, 1);
//        }).reduceByKey((v1, v2)->{
//            return v1+v2;
//        }).map(tuple->{
//            return tuple._1+HiveUtils.FIELDS_TERMINATED+tuple._2;
//        }).saveAsTextFile("hdfs:///staging/shuguang/rec_parse/dws/game/second_tp_d/delta/"+ year + "/" + month + "/" + day+"/"+game_server_version+"/"+game_big_version_short+"/"+game_key_version+"/"+part);
//
//        // 托管业务
//        JavaPairRDD<String, String> hero_state_rich_pairRDD1 = hero_state_rich_RDD.mapToPair(hero_state_rich -> {
//            JSONObject richObj = JSONUtil.JSONString2JSONObject(hero_state_rich);
//            JSONObject transObj = new JSONObject();
//            transObj.put("race_id", richObj.get("race_id"));
//            transObj.put("kill", richObj.get("kill"));
//            transObj.put("assist", richObj.get("assist"));
//            transObj.put("dead", richObj.get("dead"));
//            String join_Key = richObj.get("battle_id").toString()
//                    + ":" + richObj.get("hero_id").toString()
//                    + ":" + richObj.get("frame_no").toString();
//            return new Tuple2<String, String>(join_Key, JSONUtil.JSONObject2JSONString(transObj));
//        });
//
//        JavaPairRDD<String, String> hero_state_rich_pairRDD2 = hero_state_rich_RDD.mapToPair(hero_state_rich -> {
//            JSONObject richObj = JSONUtil.JSONString2JSONObject(hero_state_rich);
//            JSONObject transObj = new JSONObject();
//            transObj.put("kill", richObj.get("kill"));
//            transObj.put("assist", richObj.get("assist"));
//            transObj.put("dead", richObj.get("dead"));
//            String join_Key = richObj.get("battle_id").toString()
//                    + ":" + richObj.get("hero_id").toString()
//                    // frame_no+1，实际数据为上一帧数据，为了join的时候用当前帧减去上一帧，得到这一帧的相对KDA
//                    + ":" + String.valueOf(Integer.valueOf(richObj.get("frame_no").toString())+1);
//            return new Tuple2<String, String>(join_Key, JSONUtil.JSONObject2JSONString(transObj));
//        });
//
//        JavaPairRDD<String, Tuple2<String, String>> hero_state_rich_pairRDD3 = hero_state_rich_pairRDD1.join(hero_state_rich_pairRDD2);
//        join_RDD.mapToPair(join -> {
//            String join_key = join._1;
//            String hero_state_rich = join._2._1;
//            String hero_state = join._2._2;
//            JSONObject richObj = JSONUtil.JSONString2JSONObject(hero_state_rich);
//            JSONObject transObj = new JSONObject();
//            String battle_id = join_key.split(":")[0];
//            String hero_id = join_key.split(":")[1];
//            String frame_no = join_key.split(":")[2];
//            transObj.put("battle_id", battle_id);
//            transObj.put("hero_id", hero_id);
//            transObj.put("frame_no", frame_no);
//            transObj.put("battle_time_section", DimUtils.battle_time.get_hosting_minute_section(frame_no, 5));
//            transObj.put("race_id", richObj.get("race_id").toString());
//            int accum_kill = Integer.valueOf(richObj.get("kill").toString());
//            int accum_assist = Integer.valueOf(richObj.get("assist").toString());
//            int accum_dead = Integer.valueOf(richObj.get("dead").toString());
//
//            JSONObject hero_state_Obj = JSONUtil.JSONString2JSONObject(hero_state);
//            // 取出的KDA为上一帧的数据
//            int accum_pre_frame_kill = Integer.valueOf(hero_state_Obj.get("kill").toString());
//            int accum_pre_frame_assist = Integer.valueOf(hero_state_Obj.get("assist").toString());
//            int accum_pre_frame_dead = Integer.valueOf(hero_state_Obj.get("dead").toString());
//            // 得到这一帧的KDA
//            int frame_kill = accum_kill - accum_pre_frame_kill;
//            int frame_assist = accum_assist - accum_pre_frame_assist;
//            int frame_dead = accum_dead - accum_pre_frame_dead;
//
//            String playerBattle = bc.value().get("playerBattle").get(battle_id + ":" + hero_id);
//            if (StringUtils.isEmpty(playerBattle)) {
//                transObj.put("ai_type", "");
//                transObj.put("ai_base", "");
//                transObj.put("deepainum", "");
//                transObj.put("role_id", "");
//            } else {
//                JSONObject playerBattleObj = JSONUtil.JSONString2JSONObject(playerBattle);
//                transObj.put("ai_type", playerBattleObj.get("ai_type").toString());
//                transObj.put("ai_base", playerBattleObj.get("ai_base").toString());
//                transObj.put("deepainum", playerBattleObj.get("deepainum").toString());
//                transObj.put("role_id", playerBattleObj.get("role_id").toString());
//            }
//            // type: 0 为 未发生托管，1 为 deepai_host，2 为 strategyai_host，3 为 deep_ai和strategyai联合托管
//            String userArenaEndStat_deepai = bc.value().get("userArenaEndStat").get(battle_id + ":" + transObj.get("role_id").toString() + ":" + transObj.get("hosting_minute_section")+":1");
//            String userArenaEndStat_strategyai = bc.value().get("userArenaEndStat").get(battle_id + ":" + transObj.get("role_id").toString() + ":" + transObj.get("hosting_minute_section")+":2");
//            if (StringUtils.isEmpty(userArenaEndStat_deepai)) {
//                if (StringUtils.isEmpty(userArenaEndStat_strategyai)) {
//                    transObj.put("hosting_type", "0");
//                    transObj.put("hosting_minute", "");
//                } else {
//                    JSONObject userArenaEndStat_strategyai_obj = JSONUtil.JSONString2JSONObject(userArenaEndStat_strategyai);
//                    double hosting_minute = Double.valueOf(userArenaEndStat_strategyai_obj.get("hosting_minute").toString());
//                    if (hosting_minute >= 1.0) {
//                        transObj.put("hosting_type", "2");
//                    } else {
//                        transObj.put("hosting_type", "0");
//                    }
//                    transObj.put("hosting_minute", String.valueOf(hosting_minute));
//
//                }
//            } else {
//                if (StringUtils.isEmpty(userArenaEndStat_strategyai)) {
//                    JSONObject userArenaEndStat_deepai_obj = JSONUtil.JSONString2JSONObject(userArenaEndStat_deepai);
//                    double hosting_minute = Double.valueOf(userArenaEndStat_deepai_obj.get("hosting_minute").toString());
//                    if (hosting_minute >= 1.0) {
//                        transObj.put("hosting_type", "1");
//                    } else {
//                        transObj.put("hosting_type", "0");
//                    }
//                    transObj.put("hosting_minute", String.valueOf(hosting_minute));
//                } else {
//                    JSONObject userArenaEndStat_deepai_obj = JSONUtil.JSONString2JSONObject(userArenaEndStat_deepai);
//                    double deepai_hosting_minute = Double.valueOf(userArenaEndStat_deepai_obj.get("hosting_minute").toString());
//                    JSONObject userArenaEndStat_strategyai_obj = JSONUtil.JSONString2JSONObject(userArenaEndStat_strategyai);
//                    double strategyai_hosting_minute = Double.valueOf(userArenaEndStat_strategyai_obj.get("hosting_minute").toString());
//                    if (deepai_hosting_minute >= 1.0) {
//                        if (strategyai_hosting_minute >= 1.0) {
//                            transObj.put("hosting_type", "3");
//                        } else {
//                            transObj.put("hosting_type", "1");
//                        }
//                    } else {
//                        if (strategyai_hosting_minute >= 1.0) {
//                            transObj.put("hosting_type", "2");
//                        } else {
//                            transObj.put("hosting_type", "0");
//                        }
//                    }
//                    transObj.put("hosting_minute", String.valueOf(deepai_hosting_minute)+":"+String.valueOf(strategyai_hosting_minute));
//                }
//            }
//            String battle = bc.value().get("battle").get(battle_id);
//            if (StringUtils.isEmpty(battle)) {
//                transObj.put("pool_id", "");
//                transObj.put("match_elo", "");
//                transObj.put("mode_tid", "");
//                transObj.put("user_num", "");
//                transObj.put("server", "");
//            } else {
//                JSONObject battleObj = JSONUtil.JSONString2JSONObject(battle);
//                transObj.put("pool_id", battleObj.get("pool_id").toString());
//                transObj.put("match_elo", battleObj.get("match_elo").toString());
//                transObj.put("mode_tid", battleObj.get("mode_tid").toString());
//                transObj.put("user_num", battleObj.get("user_num").toString());
//                transObj.put("server", battleObj.get("server").toString());
//            }
//            String groupby_key = transObj.get("battle_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("mode_tid").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("role_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("hero_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("race_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("pool_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("match_elo").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("ai_type").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("ai_base").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("deepainum").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("user_num").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("server").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("battle_time_section").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("hosting_type").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("hosting_minute").toString();
//            return new Tuple2<String, Tuple3<Integer, Integer, Integer>>(groupby_key,
//                    new Tuple3<Integer, Integer, Integer>(frame_kill, frame_assist, frame_dead));
//        }).reduceByKey((v1, v2) -> {
//            return new Tuple3<Integer, Integer, Integer>(v1._1()+v2._1(), v1._2()+v2._2(), v1._3()+v2._3());
//        }).map(tuple -> {
//            String groupby_key = tuple._1;
//            Tuple3<Integer, Integer, Integer> kda = tuple._2;
//            return groupby_key + HiveUtils.FIELDS_TERMINATED + kda._1()+HiveUtils.FIELDS_TERMINATED+kda._2()+HiveUtils.FIELDS_TERMINATED+kda._3();
//        }).saveAsTextFile("hdfs:///staging/shuguang/rec_parse/dws/game/hosting/delta/"+ year + "/" + month + "/" + day+"/"+game_server_version+"/"+game_big_version_short+"/"+game_key_version+"/"+part);
////        hero_state_rich_RDD.filter(hero_state_rich -> {
////            JSONObject richObj = JSONUtil.JSONString2JSONObject(hero_state_rich);
////            Map<String, Map<String, String>> bc_map = bc.value();
////            Map<String, String> tower_state_bcmap = bc_map.get("tower_state");
////            String tower_state = tower_state_bcmap.get(richObj.get("battle_id").toString()
////                    +":"+richObj.get("id").toString()
////                    +":"+richObj.get("frame_no").toString());
////            if (StringUtils.isEmpty(tower_state)) {
////                return false;
////            }
////            return true;
////        }).mapToPair(hero_state_rich -> {
////            JSONObject richObj = JSONUtil.JSONString2JSONObject(hero_state_rich);
////            JSONObject transObj = new JSONObject();
////            transObj.put("area_id", richObj.get("area_id").toString());
////            transObj.put("bs_area", "");
////            transObj.put("second_col", richObj.get("second_col").toString());
////            transObj.put("camp_id", richObj.get("camp_id").toString());
////            transObj.put("hero_id", richObj.get("hero_id").toString());
////            transObj.put("appear", richObj.get("appear").toString());
////            transObj.put("hp_section", richObj.get("hp_section").toString());
////            transObj.put("mana_section", richObj.get("mana_section").toString());
////            Map<String, Map<String, String>> bc_map = bc.value();
////            Map<String, String> tower_state_bcmap = bc_map.get("tower_state");
////            String tower_state = tower_state_bcmap.get(richObj.get("battle_id").toString()
////                    +":"+richObj.get("id").toString()
////                    +":"+richObj.get("frame_no").toString());
////            JSONObject tower_state_Obj = JSONUtil.JSONString2JSONObject(tower_state);
////            transObj.put("tower_inside_id", tower_state_Obj.get("tower_inside_id").toString());
////            transObj.put("tower_camp_id", tower_state_Obj.get("tower_camp_id").toString());
////            transObj.put("type", tower_state_Obj.get("type").toString());
////            transObj.put("tower_area_id", tower_state_Obj.get("map_area_id").toString());
////            transObj.put("tower_id", tower_state_Obj.get("tower_id").toString());
////            transObj.put("aim_count", 1);
////
////            Map<String, String> playerBattle_bcmap = bc_map.get("playerBattle");
////            String playerBattle = playerBattle_bcmap.get(richObj.get("battle_id").toString() + ":" + richObj.get("hero_id").toString());
////            if (StringUtils.isEmpty(playerBattle)) {
////                transObj.put("ai_type", "");
////                transObj.put("ai_base", "");
////                transObj.put("deepainum", "");
////            } else {
////                JSONObject playerBattleObj = JSONUtil.JSONString2JSONObject(playerBattle);
////                transObj.put("ai_type", playerBattleObj.get("ai_type").toString());
////                transObj.put("ai_base", playerBattleObj.get("ai_base").toString());
////                transObj.put("deepainum", playerBattleObj.get("deepainum").toString());
////            }
////            Map<String, String> battle_bcmap = bc_map.get("battle");
////            String battle = battle_bcmap.get(richObj.get("battleId").toString());
////            if (StringUtils.isEmpty(battle)) {
////                transObj.put("pool_id", "");
////                transObj.put("match_elo", "");
////                transObj.put("mode_tid", "");
////                transObj.put("battle_start_time", "");
////            } else {
////                JSONObject battleObj = JSONUtil.JSONString2JSONObject(battle);
////                transObj.put("pool_id", battleObj.get("pool_id").toString());
////                transObj.put("match_elo", battleObj.get("match_elo").toString());
////                transObj.put("mode_tid", battleObj.get("mode_tid").toString());
////                transObj.put("battle_start_time", battleObj.get("battle_start_time").toString());
////            }
////            String groupby_key = transObj.get("second_col").toString()
////                    + HiveUtils.FIELDS_TERMINATED + transObj.get("pool_id").toString()
////                    + HiveUtils.FIELDS_TERMINATED + transObj.get("match_elo").toString()
////                    + HiveUtils.FIELDS_TERMINATED + transObj.get("deepainum").toString()
////                    + HiveUtils.FIELDS_TERMINATED + transObj.get("ai_type").toString()
////                    + HiveUtils.FIELDS_TERMINATED + transObj.get("ai_base").toString()
////                    + HiveUtils.FIELDS_TERMINATED + transObj.get("camp_id").toString()
////                    + HiveUtils.FIELDS_TERMINATED + transObj.get("hero_id").toString()
////                    + HiveUtils.FIELDS_TERMINATED + transObj.get("race_id").toString()
////                    + HiveUtils.FIELDS_TERMINATED + transObj.get("tower_inside_id").toString()
////                    + HiveUtils.FIELDS_TERMINATED + transObj.get("tower_camp_id").toString()
////                    + HiveUtils.FIELDS_TERMINATED + transObj.get("type").toString()
////                    + HiveUtils.FIELDS_TERMINATED + transObj.get("tower_area_id").toString()
////                    + HiveUtils.FIELDS_TERMINATED + transObj.get("hp_section").toString()
////                    + HiveUtils.FIELDS_TERMINATED + transObj.get("mana_section").toString();
////            return new Tuple2<String, Integer>(groupby_key, Integer.valueOf(transObj.get("aim_count").toString()));
////        }).reduceByKey((v1, v2) -> {
////            return v1 + v2;
////        }).map(tuple -> {
////            String groupby_key = tuple._1;
////            int aim_count = tuple._2;
////            return groupby_key + HiveUtils.FIELDS_TERMINATED + aim_count;
////        }).saveAsTextFile("hdfs:///staging/shuguang/rec_parse/dws/game/second_tower_aim/delta/"+ year + "/" + month + "/" + day+"/"+game_server_version+"/"+game_big_version_short+"/"+game_key_version+"/"+part);
////        tp 业务
//    }
//}
