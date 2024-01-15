package cn.jj.simulation.test;

import cn.jj.simulation.utils.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.util.SizeEstimator;
import scala.Tuple2;
import scala.Tuple3;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: JavaSOTest
 * @description:
 * @author: wangyb04
 * @create: 2021-04-06 18:09
 */
public class Insert2DWSTest {

//    public static String[] RETURN_TYPE = {"Hero","Creep","Monster","Tower","Error"};
    public static String[] RETURN_TYPE = {"Hero","Tower"};

    public static void main(String[] args) throws Exception {

        String year = args[0];
        String month = args[1];
        String day = args[2];
        int partition = Integer.valueOf(args[3]);
        String version = args[4];
        String part = args[5];
        String dws_model = args[6];
        int hosting_minute_section_interval = Integer.valueOf(args[6]);


        SparkConf conf = new SparkConf().setAppName("Insert2DWSTest"+part);
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
//        broadcast user 自定义数据结构
        Map<String, Map<String, String>> bcMap = new HashMap<String, Map<String, String>>();

//        获取小表
//        获取维表：(没有用到码表，用函数直接计算的码表数据)
//        获取server表：
        JavaRDD<String> playerBattle_RDD = sc.textFile("hdfs:///staging/shuguang/server/100055/PlayerBattle/" + year + "/" + month + "/" + day);
        bcMap.put("playerBattle", MapJoinBuilder.build_player_battle_map(playerBattle_RDD));

        JavaRDD<String> userArenaEndStat_RDD = sc.textFile("hdfs:///staging/shuguang/server/100055/UserArenaEndStat/" + year + "/" + month + "/" + day);
        bcMap.put("userArenaEndStat", MapJoinBuilder.build_userArenaEndStat_map(userArenaEndStat_RDD, hosting_minute_section_interval));

//        封装broadcast
        Broadcast<Map<String, Map<String, String>>> bc = sc.broadcast(bcMap);
//        构建 hero_state 大表
        JavaRDD<String> hero_state_RDD = sc.textFile("hdfs:///staging/shuguang/rec_parse/ods/game/Hero_state/delta/"+ year + "/" + month + "/" + day+"/"+game_server_version+"/"+game_big_version_short+"/"+game_key_version+"/"+part);
        JavaRDD<String> hero_state_rich_RDD = hero_state_RDD.coalesce(partition).map(line -> {
            String[] lines = line.split(String.valueOf(HiveUtils.FIELDS_TERMINATED));
            JSONObject transObj = new JSONObject();
            transObj.put("battle_id", lines[0]);
            transObj.put("frame_no", lines[1]);
//            相当于dim battle_time的数据
            transObj.put("second_col", DimUtils.battle_time.get_second(lines[1]));
            transObj.put("minute_col", DimUtils.battle_time.get_minute(lines[1]));
            transObj.put("id", lines[3]);
            transObj.put("camp_id", lines[4]);
//            相当于dim map_area的数据
            String area_id = DimUtils.map_area.get_area_id(lines[5], lines[6]);
            transObj.put("area_id", area_id);
//            相当于dim hp_section的数据
            String hp_percent = DimUtils.hp_section.get_hp_percent(lines[9], lines[10]);
            transObj.put("hp_percent", hp_percent);
            transObj.put("hp_section", DimUtils.hp_section.get_hp_section(hp_percent));
            String mana_percent = DimUtils.mana_section.get_mana_percent(lines[13], lines[14]);
//            System.out.println("---- mana: "+lines[13]+", mana max: "+lines[14]+", mana_percent: "+mana_percent);
            transObj.put("mana_percent", mana_percent);
            transObj.put("mana_section", DimUtils.mana_section.get_mana_section(mana_percent));
            transObj.put("level_col", lines[17]);
            transObj.put("exp", lines[18]);
            transObj.put("curGold", lines[19]);
            transObj.put("gold", lines[20]);
            transObj.put("kill", lines[30]);
            transObj.put("dead", lines[31]);
            transObj.put("assist", lines[32]);
            transObj.put("isDead", lines[58]);
            transObj.put("appear", StringUtils.isEmpty(lines[58])?1:"true".equals(lines[58])?0:1);
            transObj.put("tp_begintime", lines[59]);
            transObj.put("tp_endtime", lines[60]);
            transObj.put("portalCD", lines[62]);
            transObj.put("springPortalSuccess", lines[64]);
            transObj.put("bServerAI", lines[70]);
            transObj.put("hero_id", lines[73]);
            transObj.put("race_id", lines[74]);
            transObj.put("playerType", lines[80]);
            return JSONUtil.JSONObject2JSONString(transObj);
        });
        hero_state_rich_RDD.persist(StorageLevel.DISK_ONLY());
        hero_state_rich_RDD.count();

        // 托管业务
        if ("4".equals(dws_model) || "5".equals(dws_model)) {
            JavaPairRDD<String, String> hero_state_rich_pairRDD1 = hero_state_rich_RDD.mapToPair(hero_state_rich -> {
                JSONObject richObj = JSONUtil.JSONString2JSONObject(hero_state_rich);
                JSONObject transObj = new JSONObject();
                transObj.put("race_id", richObj.get("race_id"));
                transObj.put("kill", richObj.get("kill"));
                transObj.put("assist", richObj.get("assist"));
                transObj.put("dead", richObj.get("dead"));
                String join_Key = richObj.get("battle_id").toString()
                        + ":" + richObj.get("hero_id").toString()
                        + ":" + richObj.get("camp_id").toString()
                        + ":" + richObj.get("frame_no").toString();
                return new Tuple2<String, String>(join_Key, JSONUtil.JSONObject2JSONString(transObj));
            });

            JavaPairRDD<String, String> hero_state_rich_pairRDD2 = hero_state_rich_RDD.mapToPair(hero_state_rich -> {
                JSONObject richObj = JSONUtil.JSONString2JSONObject(hero_state_rich);
                JSONObject transObj = new JSONObject();
                transObj.put("kill", richObj.get("kill"));
                transObj.put("assist", richObj.get("assist"));
                transObj.put("dead", richObj.get("dead"));
                String join_Key = richObj.get("battle_id").toString()
                        + ":" + richObj.get("hero_id").toString()
                        + ":" + richObj.get("camp_id").toString()
                        // frame_no+1，实际数据为上一帧数据，为了join的时候用当前帧减去上一帧，得到这一帧的相对KDA
                        + ":" + String.valueOf(Integer.valueOf(richObj.get("frame_no").toString())+1);
                return new Tuple2<String, String>(join_Key, JSONUtil.JSONObject2JSONString(transObj));
            });

            JavaPairRDD<String, Tuple2<String, String>> hero_state_rich_pairRDD3 = hero_state_rich_pairRDD1.join(hero_state_rich_pairRDD2);
            hero_state_rich_pairRDD3.mapToPair(join -> {
                String join_key = join._1;
                String hero_state_rich = join._2._1;
                String hero_state = join._2._2;
                JSONObject richObj = JSONUtil.JSONString2JSONObject(hero_state_rich);
                JSONObject transObj = new JSONObject();
                String battle_id = join_key.split(":")[0];
                String hero_id = join_key.split(":")[1];
                String camp_id = join_key.split(":")[2];
                String frame_no = join_key.split(":")[3];
                transObj.put("battle_id", battle_id);
                transObj.put("hero_id", hero_id);
                transObj.put("frame_no", frame_no);
                transObj.put("battle_time_section", DimUtils.battle_time.get_hosting_minute_section(frame_no, hosting_minute_section_interval));
                transObj.put("race_id", richObj.get("race_id").toString());
                int accum_kill = Integer.valueOf(richObj.get("kill").toString());
                int accum_assist = Integer.valueOf(richObj.get("assist").toString());
                int accum_dead = Integer.valueOf(richObj.get("dead").toString());

                JSONObject hero_state_Obj = JSONUtil.JSONString2JSONObject(hero_state);
                // 取出的KDA为上一帧的数据
                int accum_pre_frame_kill = Integer.valueOf(hero_state_Obj.get("kill").toString());
                int accum_pre_frame_assist = Integer.valueOf(hero_state_Obj.get("assist").toString());
                int accum_pre_frame_dead = Integer.valueOf(hero_state_Obj.get("dead").toString());
                // 得到这一帧的KDA
                int frame_kill = accum_kill - accum_pre_frame_kill;
                int frame_assist = accum_assist - accum_pre_frame_assist;
                int frame_dead = accum_dead - accum_pre_frame_dead;

                String playerBattle = bc.value().get("playerBattle").get(battle_id + ":" + hero_id+":"+camp_id);
                if (StringUtils.isEmpty(playerBattle)) {
                    transObj.put("ai_type", "");
                    transObj.put("ai_base", "");
                    transObj.put("deepainum", "");
                    transObj.put("role_id", "");
                } else {
                    JSONObject playerBattleObj = JSONUtil.JSONString2JSONObject(playerBattle);
                    transObj.put("ai_type", playerBattleObj.get("ai_type").toString());
                    transObj.put("ai_base", playerBattleObj.get("ai_base").toString());
                    transObj.put("deepainum", playerBattleObj.get("deepainum").toString());
                    transObj.put("role_id", playerBattleObj.get("role_id").toString());
                }
                // type: 0 为 未发生托管，1 为 deepai_host，2 为 strategyai_host，3 为 deep_ai和strategyai联合托管
                String userArenaEndStat_deepai = bc.value().get("userArenaEndStat").get(battle_id + ":" + transObj.get("role_id").toString() + ":" + transObj.get("battle_time_section").toString()+":1");
                String userArenaEndStat_strategyai = bc.value().get("userArenaEndStat").get(battle_id + ":" + transObj.get("role_id").toString() + ":" + transObj.get("battle_time_section").toString()+":2");
                if (StringUtils.isEmpty(userArenaEndStat_deepai)) {
                    if (StringUtils.isEmpty(userArenaEndStat_strategyai)) {
                        transObj.put("hosting_type", "0");
                        transObj.put("hosting_minute", "");
                        transObj.put("deepai_host_secs", "");
                        transObj.put("strategyai_host_secs", "");
                    } else {
                        JSONObject userArenaEndStat_strategyai_obj = JSONUtil.JSONString2JSONObject(userArenaEndStat_strategyai);
                        double hosting_minute = Double.valueOf(userArenaEndStat_strategyai_obj.get("hosting_minute").toString());
                        if (hosting_minute >= 1.0) {
                            transObj.put("hosting_type", "2");
                        } else {
                            transObj.put("hosting_type", "0");
                        }
                        transObj.put("hosting_minute", String.valueOf(hosting_minute));
                        transObj.put("deepai_host_secs", userArenaEndStat_strategyai_obj.get("deepai_host_secs").toString());
                        transObj.put("strategyai_host_secs", userArenaEndStat_strategyai_obj.get("strategyai_host_secs").toString());
                    }
                } else {
                    if (StringUtils.isEmpty(userArenaEndStat_strategyai)) {
                        JSONObject userArenaEndStat_deepai_obj = JSONUtil.JSONString2JSONObject(userArenaEndStat_deepai);
                        double hosting_minute = Double.valueOf(userArenaEndStat_deepai_obj.get("hosting_minute").toString());
                        if (hosting_minute >= 1.0) {
                            transObj.put("hosting_type", "1");
                        } else {
                            transObj.put("hosting_type", "0");
                        }
                        transObj.put("hosting_minute", String.valueOf(hosting_minute));
                        transObj.put("deepai_host_secs", userArenaEndStat_deepai_obj.get("deepai_host_secs").toString());
                        transObj.put("strategyai_host_secs", userArenaEndStat_deepai_obj.get("strategyai_host_secs").toString());
                    } else {
                        JSONObject userArenaEndStat_deepai_obj = JSONUtil.JSONString2JSONObject(userArenaEndStat_deepai);
                        double deepai_hosting_minute = Double.valueOf(userArenaEndStat_deepai_obj.get("hosting_minute").toString());
                        JSONObject userArenaEndStat_strategyai_obj = JSONUtil.JSONString2JSONObject(userArenaEndStat_strategyai);
                        double strategyai_hosting_minute = Double.valueOf(userArenaEndStat_strategyai_obj.get("hosting_minute").toString());
                        if (deepai_hosting_minute >= 1.0) {
                            if (strategyai_hosting_minute >= 1.0) {
                                transObj.put("hosting_type", "3");
                            } else {
                                transObj.put("hosting_type", "1");
                            }
                        } else {
                            if (strategyai_hosting_minute >= 1.0) {
                                transObj.put("hosting_type", "2");
                            } else {
                                transObj.put("hosting_type", "0");
                            }
                        }
                        transObj.put("hosting_minute", String.valueOf(deepai_hosting_minute)+":"+String.valueOf(strategyai_hosting_minute));
                        transObj.put("deepai_host_secs", userArenaEndStat_deepai_obj.get("deepai_host_secs").toString()+":"+userArenaEndStat_strategyai_obj.get("deepai_host_secs").toString());
                        transObj.put("strategyai_host_secs", userArenaEndStat_deepai_obj.get("strategyai_host_secs").toString()+":"+userArenaEndStat_strategyai_obj.get("strategyai_host_secs").toString());
                    }
                }
                String groupby_key = transObj.get("battle_id").toString()
                        + HiveUtils.FIELDS_TERMINATED + "model_tid"
                        + HiveUtils.FIELDS_TERMINATED + transObj.get("role_id").toString()
                        + HiveUtils.FIELDS_TERMINATED + transObj.get("hero_id").toString()
                        + HiveUtils.FIELDS_TERMINATED + transObj.get("race_id").toString()
                        + HiveUtils.FIELDS_TERMINATED + "pool_id"
                        + HiveUtils.FIELDS_TERMINATED + "match_elo"
                        + HiveUtils.FIELDS_TERMINATED + transObj.get("ai_type").toString()
                        + HiveUtils.FIELDS_TERMINATED + transObj.get("ai_base").toString()
                        + HiveUtils.FIELDS_TERMINATED + transObj.get("deepainum").toString()
                        + HiveUtils.FIELDS_TERMINATED + "user_num"
                        + HiveUtils.FIELDS_TERMINATED + "server"
                        + HiveUtils.FIELDS_TERMINATED + transObj.get("battle_time_section").toString()
                        + HiveUtils.FIELDS_TERMINATED + transObj.get("hosting_type").toString()
                        + HiveUtils.FIELDS_TERMINATED + transObj.get("hosting_minute").toString()
                        + HiveUtils.FIELDS_TERMINATED + transObj.get("deepai_host_secs").toString()
                        + HiveUtils.FIELDS_TERMINATED + transObj.get("strategyai_host_secs").toString();
                return new Tuple2<String, Tuple3<Integer, Integer, Integer>>(groupby_key,
                        new Tuple3<Integer, Integer, Integer>(frame_kill, frame_assist, frame_dead));
            }).reduceByKey((v1, v2) -> {
                return new Tuple3<Integer, Integer, Integer>(v1._1()+v2._1(), v1._2()+v2._2(), v1._3()+v2._3());
            }).map(tuple -> {
                String groupby_key = tuple._1;
                Tuple3<Integer, Integer, Integer> kda = tuple._2;
                return groupby_key + HiveUtils.FIELDS_TERMINATED + kda._1()+HiveUtils.FIELDS_TERMINATED+kda._2()+HiveUtils.FIELDS_TERMINATED+kda._3();
            }).saveAsTextFile("hdfs:///staging/shuguang/rec_parse/dws/game/section_hosting/delta/"+ year + "/" + month + "/" + day+"/"+game_server_version+"/"+game_big_version_short+"/"+game_key_version+"/"+part);
        }
//        hero_state_rich_RDD.filter(hero_state_rich -> {
//            JSONObject richObj = JSONUtil.JSONString2JSONObject(hero_state_rich);
//            Map<String, Map<String, String>> bc_map = bc.value();
//            Map<String, String> tower_state_bcmap = bc_map.get("tower_state");
//            String tower_state = tower_state_bcmap.get(richObj.get("battle_id").toString()
//                    +":"+richObj.get("id").toString()
//                    +":"+richObj.get("frame_no").toString());
//            if (StringUtils.isEmpty(tower_state)) {
//                return false;
//            }
//            return true;
//        }).mapToPair(hero_state_rich -> {
//            JSONObject richObj = JSONUtil.JSONString2JSONObject(hero_state_rich);
//            JSONObject transObj = new JSONObject();
//            transObj.put("area_id", richObj.get("area_id").toString());
//            transObj.put("bs_area", "");
//            transObj.put("second_col", richObj.get("second_col").toString());
//            transObj.put("camp_id", richObj.get("camp_id").toString());
//            transObj.put("hero_id", richObj.get("hero_id").toString());
//            transObj.put("appear", richObj.get("appear").toString());
//            transObj.put("hp_section", richObj.get("hp_section").toString());
//            transObj.put("mana_section", richObj.get("mana_section").toString());
//            Map<String, Map<String, String>> bc_map = bc.value();
//            Map<String, String> tower_state_bcmap = bc_map.get("tower_state");
//            String tower_state = tower_state_bcmap.get(richObj.get("battle_id").toString()
//                    +":"+richObj.get("id").toString()
//                    +":"+richObj.get("frame_no").toString());
//            JSONObject tower_state_Obj = JSONUtil.JSONString2JSONObject(tower_state);
//            transObj.put("tower_inside_id", tower_state_Obj.get("tower_inside_id").toString());
//            transObj.put("tower_camp_id", tower_state_Obj.get("tower_camp_id").toString());
//            transObj.put("type", tower_state_Obj.get("type").toString());
//            transObj.put("tower_area_id", tower_state_Obj.get("map_area_id").toString());
//            transObj.put("tower_id", tower_state_Obj.get("tower_id").toString());
//            transObj.put("aim_count", 1);
//
//            Map<String, String> playerBattle_bcmap = bc_map.get("playerBattle");
//            String playerBattle = playerBattle_bcmap.get(richObj.get("battle_id").toString() + ":" + richObj.get("hero_id").toString());
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
//            Map<String, String> battle_bcmap = bc_map.get("battle");
//            String battle = battle_bcmap.get(richObj.get("battleId").toString());
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
//            String groupby_key = transObj.get("second_col").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("pool_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("match_elo").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("deepainum").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("ai_type").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("ai_base").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("camp_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("hero_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("race_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("tower_inside_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("tower_camp_id").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("type").toString()
//                    + HiveUtils.FIELDS_TERMINATED + transObj.get("tower_area_id").toString()
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
//        tp 业务
    }
}
