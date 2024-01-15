package cn.jj.simulation.utils;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.util.SizeEstimator;
import scala.Tuple6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-10-29 15:12
 */
public class MapJoinBuilder {

    //        获取playerBattle表
//    TODO 相关用到player_battle的地方未同步
    public static Map<String, String> build_player_battle_map(JavaRDD<String> playerBattle_RDD) {
        List<String> playerBattle_RDD_collect = playerBattle_RDD.map(line->{
            JSONObject lineObj = JSONUtil.JSONString2JSONObject(line);
            JSONObject transObj = new JSONObject();
            transObj.put("battleid", lineObj.get("battleid")==null?"":lineObj.get("battleid").toString());
            transObj.put("ai_type", lineObj.get("ai_type")==null?"":lineObj.get("ai_type").toString());
            transObj.put("ai_base", lineObj.get("ai_base")==null?"":lineObj.get("ai_base").toString());
            transObj.put("deepainum", lineObj.get("deepainum")==null?"":lineObj.get("deepainum").toString());
            transObj.put("herotid", lineObj.get("herotid")==null?"":lineObj.get("herotid").toString());
            transObj.put("role_id", lineObj.get("role_id")==null?"":lineObj.get("role_id").toString());
            transObj.put("factionid", lineObj.get("factionid")==null?"":lineObj.get("factionid").toString());
            return JSONUtil.JSONObject2JSONString(transObj);
        }).collect();
        Map<String, String> player_battle_map = new HashMap<String, String>();
        for (String playerBattle: playerBattle_RDD_collect) {
            JSONObject jsonObj = JSONUtil.JSONString2JSONObject(playerBattle);
//            key = battleid:herotid
            player_battle_map.put(jsonObj.get("battleid").toString()+":"+jsonObj.get("herotid").toString()+":"+jsonObj.get("factionid").toString(), playerBattle);
        }
        System.out.println("---- player_battle_map size: "+ SizeEstimator.estimate(player_battle_map)+"bytes");
        return player_battle_map;
    }

    //        获取UserArenaEndStat表
    public static Map<String, String> build_userArenaEndStat_map(JavaRDD<String> userArenaEndStat_RDD, int hosting_minute_section_interval) {
        List<String> userArenaEndStat_RDD_collect = userArenaEndStat_RDD.flatMap(line->{
            JSONObject lineObj = JSONUtil.JSONString2JSONObject(line);
            List<String> res_list = new ArrayList<String>();
            List<Tuple6<String,String,String,String,String,String>> deepai_host_list = new ArrayList<Tuple6<String,String,String,String,String,String>>();
            List<Tuple6<String,String,String,String,String,String>> strategyai_host_list = new ArrayList<Tuple6<String,String,String,String,String,String>>();
            // TODO 数据可能为"null"
            String deepai_host_secs = lineObj.get("deepai_host_secs") == null ? "" : lineObj.get("deepai_host_secs").toString();
            // TODO 数据可能为"null"
            String strategyai_host_secs = lineObj.get("strategyai_host_secs") == null ? "" : lineObj.get("strategyai_host_secs").toString();

            if (!org.apache.commons.lang3.StringUtils.isEmpty(deepai_host_secs) && !"null".equals(deepai_host_secs) && !"Null".equals(deepai_host_secs) && !"\"null\"".equals(deepai_host_secs) && !"”null“".equals(deepai_host_secs)) {
                deepai_host_list = DataUtils.deal_hosting_section(deepai_host_secs, "1", hosting_minute_section_interval);
                if (!org.apache.commons.lang3.StringUtils.isEmpty(strategyai_host_secs) && !"null".equals(strategyai_host_secs) && !"Null".equals(strategyai_host_secs) && !"\"null\"".equals(strategyai_host_secs) && !"”null“".equals(strategyai_host_secs)) {
                    strategyai_host_list = DataUtils.deal_hosting_section(strategyai_host_secs, "2", hosting_minute_section_interval);
                }
            } else {
                if (!StringUtils.isEmpty(strategyai_host_secs) && !"null".equals(strategyai_host_secs) && !"Null".equals(strategyai_host_secs) && !"\"null\"".equals(strategyai_host_secs) && !"”null“".equals(strategyai_host_secs)) {
                    strategyai_host_list = DataUtils.deal_hosting_section(strategyai_host_secs, "2", hosting_minute_section_interval);
                }
            }
            for (Tuple6<String,String,String,String,String,String> deepai_host_tuple: deepai_host_list) {
                JSONObject transObj = new JSONObject();
                transObj.put("battleid", lineObj.get("battleid")==null?"":lineObj.get("battleid").toString());
                transObj.put("role_id", lineObj.get("role_id")==null?"":lineObj.get("role_id").toString());
                transObj.put("deepai_host_secs", deepai_host_secs);
                transObj.put("strategyai_host_secs", strategyai_host_secs);
                transObj.put("hosting_minute_section_interval", hosting_minute_section_interval);
                transObj.put("hosting_type", deepai_host_tuple._1());
                transObj.put("hosting_minute_section", deepai_host_tuple._2());
                transObj.put("hosting_begin_sec", deepai_host_tuple._3());
                transObj.put("hosting_end_sec", deepai_host_tuple._4());
                transObj.put("hosting_second", deepai_host_tuple._5());
                transObj.put("hosting_minute", deepai_host_tuple._6());
                res_list.add(JSONUtil.JSONObject2JSONString(transObj));
            }
            for (Tuple6<String,String,String,String,String,String> strategyai_host_tuple: strategyai_host_list) {
                JSONObject transObj = new JSONObject();
                transObj.put("battleid", lineObj.get("battleid")==null?"":lineObj.get("battleid").toString());
                transObj.put("role_id", lineObj.get("role_id")==null?"":lineObj.get("role_id").toString());
                transObj.put("deepai_host_secs", deepai_host_secs);
                transObj.put("strategyai_host_secs", strategyai_host_secs);
                transObj.put("hosting_minute_section_interval", hosting_minute_section_interval);
                transObj.put("hosting_type", strategyai_host_tuple._1());
                transObj.put("hosting_minute_section", strategyai_host_tuple._2());
                transObj.put("hosting_begin_sec", strategyai_host_tuple._3());
                transObj.put("hosting_end_sec", strategyai_host_tuple._4());
                transObj.put("hosting_second", strategyai_host_tuple._5());
                transObj.put("hosting_minute", strategyai_host_tuple._6());
                res_list.add(JSONUtil.JSONObject2JSONString(transObj));
            }
            return res_list.iterator();
        }).collect();
        Map<String, String> userArenaEndStat_map = new HashMap<String, String>();
        for (String userArenaEndStat: userArenaEndStat_RDD_collect) {
            JSONObject jsonObj = JSONUtil.JSONString2JSONObject(userArenaEndStat);
//            key = battleid:role_id:hosting_minute_section:hosting_type
            userArenaEndStat_map.put(jsonObj.get("battleid").toString()+":"+jsonObj.get("role_id").toString()+":"+jsonObj.get("hosting_minute_section").toString()+":"+jsonObj.get("hosting_type").toString(), userArenaEndStat);
//            System.out.println("---- userArenaEndStat: "+jsonObj.get("battleid").toString()+":"+jsonObj.get("role_id").toString()+":"+jsonObj.get("hosting_minute_section").toString()+":"+jsonObj.get("hosting_type").toString());
        }
        System.out.println("---- userArenaEndStat_map size: "+ SizeEstimator.estimate(userArenaEndStat_map)+"bytes");
        return userArenaEndStat_map;
    }

    //        获取battle表
    public static Map<String, String> build_battle_map(JavaRDD<String> battle_RDD) {
        List<String> battle_RDD_collect = battle_RDD.map(line -> {
            String[] lines = line.split(String.valueOf(HiveUtils.FIELDS_TERMINATED));
            JSONObject transObj = new JSONObject();
            transObj.put("battle_id", lines[0]);
            transObj.put("pool_id", lines[5]);
            transObj.put("match_elo", lines[4]);
            transObj.put("battle_start_time", lines[6]);
            transObj.put("mode_tid", lines[2]);
            transObj.put("user_num", lines[9]);
            transObj.put("server", lines[15]);
            return JSONUtil.JSONObject2JSONString(transObj);
        }).collect();
        Map<String, String> battle_map = new HashMap<String, String>();
        for (String battle: battle_RDD_collect) {
            JSONObject jsonObj = JSONUtil.JSONString2JSONObject(battle);
//            key = battle_id
            battle_map.put(jsonObj.get("battle_id").toString(), battle);
        }
        System.out.println("---- battle_map size: "+ SizeEstimator.estimate(battle_map)+"bytes");
        return battle_map;
    }
//
//    public static void main(String[] args) {
//        int hosting_minute_section_interval = 5;
//        String line = "{\"first_udp\":1635763166.43,\"last_disc_tcp\":1635763419.64,\"busdata_id\":\"511_UserArenaEndStat\",\"server\":\"zone205\",\"disc_udp_sec\":439.91,\"play_sec\":666.65,\"init_stamp\":1635763117,\"negative_secs\":\"[11,0,0]\",\"odsfilename\":\"/data/gagserver/logs/tkdhl/gameserver_100055_zone205_arena12_20211101.dhl\",\"disc_secs\":\"{\\\"1635763419\\\":443.69}\",\"duration\":666.65,\"role_id\":16821113205,\"doubt_illegal\":\"null\",\"start_stamp\":1635763196,\"disc_udp_times\":1,\"disc_tcp_sec\":443.88,\"disc_tcp_times\":1,\"idle_first\":66.93,\"billguid\":1309290518000203421,\"first_tcp\":1635763117.19,\"last_disc_udp\":1635763423.42,\"conn_udp_times\":1,\"battleid\":51013046270304,\"conn_tcp_times\":1,\"fileuniqueid\":1309290518,\"illegal\":\"[null,null,true]\",\"modetid\":163,\"idle_cnt\":0,\"idle_run\":3,\"invalid_play_secs\":\"{\\\"62\\\":173,\\\"235\\\":432}\",\"odsdomainid\":511,\"deepai_host_secs\":\"null\",\"appid\":100055,\"dtcclientip\":\"10.110.98.23\",\"logtime\":\"2021-11-01 18:51:03\",\"operation\":\"UserArenaEndStat\",\"strategyai_host_secs\":\"{\\\"252.96\\\":413.71}\",\"idle_secs\":\"{\\\"66.93\\\":133.99}\"}";
//        JSONObject lineObj = JSONUtil.JSONString2JSONObject(line);
//        List<String> res_list = new ArrayList<String>();
//        List<Tuple6<String,String,String,String,String,String>> deepai_host_list = new ArrayList<Tuple6<String,String,String,String,String,String>>();
//        List<Tuple6<String,String,String,String,String,String>> strategyai_host_list = new ArrayList<Tuple6<String,String,String,String,String,String>>();
//        // TODO 数据可能为"null"
//        String deepai_host_secs = lineObj.get("deepai_host_secs") == null ? "" : lineObj.get("deepai_host_secs").toString();
//        // TODO 数据可能为"null"
//        String strategyai_host_secs = lineObj.get("strategyai_host_secs") == null ? "" : lineObj.get("strategyai_host_secs").toString();
//
//        if (!org.apache.commons.lang3.StringUtils.isEmpty(deepai_host_secs) && !"null".equals(deepai_host_secs) && !"Null".equals(deepai_host_secs) && !"\"null\"".equals(deepai_host_secs) && !"”null“".equals(deepai_host_secs)) {
//            deepai_host_list = DataUtils.deal_hosting_section(deepai_host_secs, "1", hosting_minute_section_interval);
//            if (!org.apache.commons.lang3.StringUtils.isEmpty(strategyai_host_secs) && !"null".equals(strategyai_host_secs) && !"Null".equals(strategyai_host_secs) && !"\"null\"".equals(strategyai_host_secs) && !"”null“".equals(strategyai_host_secs)) {
//                strategyai_host_list = DataUtils.deal_hosting_section(strategyai_host_secs, "2", hosting_minute_section_interval);
//            }
//        } else {
//            if (!StringUtils.isEmpty(strategyai_host_secs) && !"null".equals(strategyai_host_secs) && !"Null".equals(strategyai_host_secs) && !"\"null\"".equals(strategyai_host_secs) && !"”null“".equals(strategyai_host_secs)) {
//                strategyai_host_list = DataUtils.deal_hosting_section(strategyai_host_secs, "2", hosting_minute_section_interval);
//            }
//        }
//        for (Tuple6<String,String,String,String,String,String> deepai_host_tuple: deepai_host_list) {
//            JSONObject transObj = new JSONObject();
//            transObj.put("battleid", lineObj.get("battleid")==null?"":lineObj.get("battleid").toString());
//            transObj.put("role_id", lineObj.get("role_id")==null?"":lineObj.get("role_id").toString());
//            transObj.put("deepai_host_secs", deepai_host_secs);
//            transObj.put("strategyai_host_secs", strategyai_host_secs);
//            transObj.put("hosting_minute_section_interval", hosting_minute_section_interval);
//            transObj.put("hosting_type", deepai_host_tuple._1());
//            transObj.put("hosting_minute_section", deepai_host_tuple._2());
//            transObj.put("hosting_begin_sec", deepai_host_tuple._3());
//            transObj.put("hosting_end_sec", deepai_host_tuple._4());
//            transObj.put("hosting_second", deepai_host_tuple._5());
//            transObj.put("hosting_minute", deepai_host_tuple._6());
//            res_list.add(JSONUtil.JSONObject2JSONString(transObj));
//        }
//        for (Tuple6<String,String,String,String,String,String> strategyai_host_tuple: strategyai_host_list) {
//            JSONObject transObj = new JSONObject();
//            transObj.put("battleid", lineObj.get("battleid")==null?"":lineObj.get("battleid").toString());
//            transObj.put("role_id", lineObj.get("role_id")==null?"":lineObj.get("role_id").toString());
//            transObj.put("deepai_host_secs", deepai_host_secs);
//            transObj.put("strategyai_host_secs", strategyai_host_secs);
//            transObj.put("hosting_minute_section_interval", hosting_minute_section_interval);
//            transObj.put("hosting_type", strategyai_host_tuple._1());
//            transObj.put("hosting_minute_section", strategyai_host_tuple._2());
//            transObj.put("hosting_begin_sec", strategyai_host_tuple._3());
//            transObj.put("hosting_end_sec", strategyai_host_tuple._4());
//            transObj.put("hosting_second", strategyai_host_tuple._5());
//            transObj.put("hosting_minute", strategyai_host_tuple._6());
//            res_list.add(JSONUtil.JSONObject2JSONString(transObj));
//        }
//        for (String l: res_list) {
//            System.out.println(l);
//        }
//    }
}
