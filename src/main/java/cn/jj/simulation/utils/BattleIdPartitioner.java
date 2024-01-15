package cn.jj.simulation.utils;

import org.apache.spark.Partitioner;
import scala.Int;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: sgods
 * @description: 每个battle_id都携带一个唯一、连续的partition_index，通过partition_index来进行repartition，确保数据不产生倾斜
 * @author: wangyb04
 * @create: 2021-09-13 16:35
 */
public class BattleIdPartitioner extends Partitioner {

    private int partitions;

    public BattleIdPartitioner(int partitions) {
        this.partitions = partitions;
    }

    @Override
    public int numPartitions() {
        return this.partitions;
    }

    @Override
    public int getPartition(Object key) {
        String battle_id = key.toString();
        String partition = battle_id.split(":")[1];
        System.out.println("---- partition: "+partition);
        return Integer.valueOf(partition);
    }
}
