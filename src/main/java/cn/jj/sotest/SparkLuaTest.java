package cn.jj.sotest;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.LongAccumulator;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: JavaSOTest
 * @description:
 * @author: wangyb04
 * @create: 2021-04-06 16:03
 */
public class SparkLuaTest {
    public static void main(String[] args) throws InterruptedException {
        SparkConf conf = new SparkConf().setAppName("SparkLuaTest");
        JavaSparkContext sc = new JavaSparkContext(conf);
        LongAccumulator accum = sc.sc().longAccumulator();
        List<Integer> l = new ArrayList<Integer>();
        int parallel = Integer.valueOf(args[0]);
        int partitions = Integer.valueOf(args[1]);
        for (int i = 0; i < parallel; i++) {
            l.add(i);
        }
        JavaRDD<Integer> data = sc.parallelize(l, partitions);
        JavaRDD<Integer> results = data.mapPartitions(n -> { // 不用关注输入的具体值，只是为了循环
            // 生成 lua 运行时环境
            Globals globals = JsePlatform.standardGlobals();
            // 使用 lua-bytecode to Java-bytecode 的 LuaJC 模式
            // LuaJC.install(globals);
            // 调用 fight.lua 脚本，fight.lua 在工程的 resources 根目录
            globals.loadfile("fight.lua").call();
            // 调用 fight.lua 中的 fight 方法
            LuaValue func = globals.get(LuaValue.valueOf("fight"));
//            System.out.println(func.isclosure());  // Will be false when LuaJC is working.
            List<Integer> ress = new ArrayList<Integer>();
            while (n.hasNext()) {
                Integer next = n.next();
                // 随便定义的输入，跟方法没什么关联。可以自行修改。返回的是脚本的执行时间
                int duratn = func.invoke(LuaValue.varargsOf(new LuaValue[]{LuaValue.valueOf(3), LuaValue.valueOf(5)})).toint(1);
                System.out.println("duration: "+duratn);
                ress.add(duratn);
            }
            return ress.iterator();
        });
        // 累加脚本的执行总时长
        results.foreach(n -> accum.add(n));
        System.out.println("----------- accum duration: "+accum.value());
        System.out.println("----------- parallel: "+parallel);
        System.out.println("----------- avg duration: "+Float.valueOf(accum.value())/parallel);
//        Thread.sleep(100000);
    }
}
