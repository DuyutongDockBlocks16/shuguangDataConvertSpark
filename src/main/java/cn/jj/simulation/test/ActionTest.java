package cn.jj.simulation.test;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: sgods
 * @description: 测试由action操作触发的多个job，是否重启executor，执行进程方法
 * @author: wangyb04
 * @create: 2021-06-02 11:20
 */
public class ActionTest {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("ActionTest");
        JavaSparkContext sc = new JavaSparkContext(conf);
        int parallize = Integer.valueOf(args[0]);
        List<Integer> list = new ArrayList<Integer>();
        int[] params = new int[]{1,2};
        for (int m = 0; m < parallize; m++) {
            list.add(m);
        };
        for (int p: params) {
            JavaRDD<Integer> javaRDD = sc.parallelize(list);
            JavaRDD<Integer> filterRDD = javaRDD.filter(e -> {
                if (p == 1) {
                    if (e.intValue() < parallize / 2) return true;
                    return false;
                } else {
                    if (e.intValue() >= parallize / 2) return true;
                    return false;
                }
            });
            JavaRDD<Integer> map = filterRDD.map(e -> {
                StaticTest.test(e);
                return e + 1;
            });
            List<Integer> collect = map.collect();
            for (int c: collect) {
                System.out.println("---- res: "+c);
            }
            System.out.println("------ first ------");
        }
    }
}
