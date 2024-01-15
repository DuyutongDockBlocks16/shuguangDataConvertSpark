//package cn.jj.simulation.utils;
//
//import cn.jj.simulation.entity.Battle;
//import cn.jj.simulation.entity.PlayerBattle;
//import cn.jj.simulation.entity.UserArenaEndStat;
//import net.sf.json.JSONObject;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.util.SizeEstimator;
//import scala.Tuple6;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @program: sgods
// * @description:
// * @author: wangyb04
// * @create: 2021-10-29 15:12
// */
//public class MapJoinBuilderNew {
//
//    //        获取playerBattle表
//    public static Map<String, PlayerBattle> build_player_battle_map(JavaRDD<String> playerBattle_RDD) {
//        List<PlayerBattle> playerBattle_RDD_collect = playerBattle_RDD.map(line->{
//            return new PlayerBattle(line);
//        }).collect();
//        Map<String, PlayerBattle> player_battle_map = new HashMap<String, PlayerBattle>();
//        for (PlayerBattle playerBattle: playerBattle_RDD_collect) {
////            key = battleid:herotid
//            player_battle_map.put(playerBattle.get_key(), playerBattle.tos);
//        }
//        System.out.println("---- player_battle_map size: "+ SizeEstimator.estimate(player_battle_map)+"bytes");
//        return player_battle_map;
//    }
//
//    //        获取UserArenaEndStat表
//    public static Map<String, UserArenaEndStat.Hosting> build_userArenaEndStat_map(JavaRDD<String> userArenaEndStat_RDD, int hosting_minute_section_interval) {
//        List<UserArenaEndStat.Hosting> userArenaEndStat_RDD_collect = userArenaEndStat_RDD.flatMap(line->{
//            return new UserArenaEndStat(line, hosting_minute_section_interval).hosting_list.iterator();
//        }).collect();
//        Map<String, UserArenaEndStat.Hosting> userArenaEndStat_map = new HashMap<String, UserArenaEndStat.Hosting>();
//        for (UserArenaEndStat.Hosting hosting: userArenaEndStat_RDD_collect) {
////            key = battleid:role_id:hosting_minute_section:hosting_type
//            userArenaEndStat_map.put(hosting.get_key(), hosting);
//        }
//        System.out.println("---- userArenaEndStat_map size: "+ SizeEstimator.estimate(userArenaEndStat_map)+"bytes");
//        return userArenaEndStat_map;
//    }
//
//    //        获取battle表
//    public static Map<String, Battle> build_battle_map(JavaRDD<String> battle_RDD) {
//        List<Battle> battle_RDD_collect = battle_RDD.map(line -> {
//            return new Battle(line);
//        }).collect();
//        Map<String, Battle> battle_map = new HashMap<String, Battle>();
//        for (Battle battle: battle_RDD_collect) {
////            key = battle_id
//            battle_map.put(battle.get_key(), battle);
//        }
//        System.out.println("---- battle_map size: "+ SizeEstimator.estimate(battle_map)+"bytes");
//        return battle_map;
//    }
//}
