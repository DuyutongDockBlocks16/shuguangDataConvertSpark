package cn.jj.sotest;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.LongAccumulator;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: JavaSOTest
 * @description:
 * @author: wangyb04
 * @create: 2021-04-06 18:09
 */
public class SparkSoTest {

    public static void main(String[] args) throws InterruptedException {

        SparkConf conf = new SparkConf().setAppName("SoTest");
        JavaSparkContext sc = new JavaSparkContext(conf);
        LongAccumulator accum = sc.sc().longAccumulator();
        List<Integer> l = new ArrayList<Integer>();
        int parallel = Integer.valueOf(args[0]);
        int partitions = Integer.valueOf(args[1]);
        for (int i = 0; i < parallel; i++) {
            l.add(i);
        }
        JavaRDD<Integer> data = sc.parallelize(l, partitions);
        JavaRDD<Integer> resData = data.map(n -> {
            // 调用 Native 方法
            int res = FightSO.Instance.luafight(5, 7);
            return res;
        });
        resData.foreach(n -> accum.add(n));
        System.out.println("----------- accum duration: "+accum.value());
        System.out.println("----------- parallel: "+parallel);
        System.out.println("----------- avg duration: "+Float.valueOf(accum.value())/parallel);
    }
}
