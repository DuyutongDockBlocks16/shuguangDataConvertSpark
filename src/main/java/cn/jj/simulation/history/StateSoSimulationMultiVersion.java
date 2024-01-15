package cn.jj.simulation.history;

import cn.jj.simulation.test.OutFuncI;
import cn.jj.simulation.utils.BdUtils;
import cn.jj.simulation.utils.DateUtils;
import cn.jj.simulation.utils.GamePbUtils;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.input.PortableDataStream;
import org.apache.spark.storage.StorageLevel;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: JavaSOTest
 * @description: 可以处理多个版本的问题，目前的方案是通过filter过滤不同版本的bd文件，启动多个job来执行。
 * problem: Lua脚本一个进程只能处理一个版本的bd文件
 * 方案: 1. 每个job静态初始化自己的战斗环境；2. Lua脚本全量初始化所有版本的战斗环境，通过参数来指定需要哪个版本的so来处理，可能会用到动态加载类；
 * @author: wangyb04
 * @create: 2021-05-13 10:12
 */
public class StateSoSimulationMultiVersion {

    public static String[] versions = new String[]{"1.0","1.1"};

    public static void main(String[] args) throws Exception {
        SparkConf conf = new SparkConf().setAppName("StateSoSimulation");
        JavaSparkContext sc = new JavaSparkContext(conf);
        String battle_date = args[0];
        for (String s_ver: versions) {
            // 读HDFS上的bd文件
            // TODO har文件和普通小文件读取的性能对比
            JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("har:///staging/shuguang/rec/archive/" + battle_date+".har/*/52081545987200.bd");
            JavaPairRDD<String, PortableDataStream> versionPairRDD = bdBinaryFiles_pairRDD.filter(pair -> {
                DataInputStream inputStream = pair._2.open();
                BdUtils bdUtils = new BdUtils();
                String version = bdUtils.getVersion(inputStream);
                if (s_ver.equals(version)) return true;
                return false;
            });
            // 每一个bd文件，flat为pb字节流的集合，并且cache，供多个action使用
            JavaPairRDD<String, byte[]> pb_bytes_pairRDD = versionPairRDD.flatMapValues(b_stream -> {
                InputStream input_stream = b_stream.open();
                BdUtils bdUtils = new BdUtils();
                List<byte[]> pb_bytes_list = new ArrayList<byte[]>();
                // 游戏command解析，底层调用录像工具so
                bdUtils.analysis(input_stream, new OutFuncI() {
                    @Override
                    public void OutputFunc(Pointer msg, NativeLong sz) {
                        byte[] pb_bytes = bdUtils.getPbBytes(msg, sz);
                        if (pb_bytes != null && pb_bytes.length != 0) {
                            pb_bytes_list.add(pb_bytes);
                        } else {
                            System.out.println("---- invalid msg");
                        }
                    }
                });
                return pb_bytes_list;
            });
            // 缓存二进制RDD，避免多个action重复调用录像工具so
            pb_bytes_pairRDD.persist(StorageLevel.MEMORY_ONLY_2());
            // TODO 需要测试一下flatMap是否会重新分发key，产生网络损耗
            // heroState_RDD
            JavaRDD<String> heroState_RDD = pb_bytes_pairRDD.flatMap(pair-> {
                String battle_id = pair._1;
                byte[] pb_bytes = pair._2;
                List<String> heroState_list = GamePbUtils.getHeroStateList(battle_id, pb_bytes);
                return heroState_list.iterator();
            });
            List<String> res = heroState_RDD.collect();
            for(String r: res) {
                System.out.println("heroState: "+r);
            }
            // TODO 待规范hdfs路径，以及设置压缩格式
            heroState_RDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/"+ DateUtils.getCurrentDate()+"/"+s_ver+"/ods_heroState.txt");

            pb_bytes_pairRDD.unpersist();
        }
    }
}
