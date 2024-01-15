package cn.jj.simulation.history;

import cn.jj.simulation.utils.BdUtils;
import cn.jj.simulation.utils.ByteUtils;
import cn.jj.simulation.utils.GamePbUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.List;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-06-29 15:41
 */
public class Split2Hive {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Split2Hive");
        JavaSparkContext sc = new JavaSparkContext(conf);
        String year = args[0];
        String month = args[1];
        String day = args[2];
        int partition = Integer.valueOf(args[3]);
        JavaRDD<String> o_dataRDD = sc.textFile("hdfs:///staging/shuguang/rec_parse/" + year + "/" + month + "/" + day + "/ods_total", partition);
        o_dataRDD.flatMap(o_data->{
            String battle_id = ByteUtils.getBattleId(o_data);
            byte[] data = ByteUtils.getData(o_data);
            List<String> heroState_list = GamePbUtils.getHeroStateList(battle_id, data);
            return heroState_list.iterator();
        }).saveAsTextFile("hdfs:///staging/shuguang/rec_parse/"+ year + "/" + month + "/" + day +"/ods_stateHero");
        o_dataRDD.flatMap(o_data->{
            String battle_id = ByteUtils.getBattleId(o_data);
            byte[] data = ByteUtils.getData(o_data);
            List<String> creepState_list = GamePbUtils.getCreepStateList(battle_id, data);
            return creepState_list.iterator();
        }).saveAsTextFile("hdfs:///staging/shuguang/rec_parse/"+ year + "/" + month + "/" + day +"/ods_stateCreep");
        o_dataRDD.flatMap(o_data->{
            String battle_id = ByteUtils.getBattleId(o_data);
            byte[] data = ByteUtils.getData(o_data);
            List<String> monsterState_list = GamePbUtils.getMonsterStateList(battle_id, data);
            return monsterState_list.iterator();
        }).saveAsTextFile("hdfs:///staging/shuguang/rec_parse/"+ year + "/" + month + "/" + day +"/ods_stateMonster");
        o_dataRDD.flatMap(o_data->{
            String battle_id = ByteUtils.getBattleId(o_data);
            byte[] data = ByteUtils.getData(o_data);
            List<String> towerStateList = GamePbUtils.getTowerStateList(battle_id, data);
            return towerStateList.iterator();
        }).saveAsTextFile("hdfs:///staging/shuguang/rec_parse/"+ year + "/" + month + "/" + day +"/ods_stateTower");
    }
}
