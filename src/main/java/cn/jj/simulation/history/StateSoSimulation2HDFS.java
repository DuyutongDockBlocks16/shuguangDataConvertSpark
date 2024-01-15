package cn.jj.simulation.history;

import cn.jj.simulation.test.OutFuncI;
import cn.jj.simulation.utils.BdUtils;
import cn.jj.simulation.utils.BDRead;
import cn.jj.simulation.utils.StringUtils;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: JavaSOTest
 * @description: 模拟曙光state的so模块功能，线程安全
 * @author: wangyb04
 * @create: 2021-05-13 10:12
 */
public class StateSoSimulation2HDFS {

    public static void main(String[] args) throws Exception {
        SparkConf conf = new SparkConf().setAppName("StateSoSimulationNew");
        JavaSparkContext sc = new JavaSparkContext(conf);
        String year = args[0];
        String month = args[1];
        String day = args[2];
        int partition = Integer.valueOf(args[3]);
        // 读HDFS上的bd文件
        // TODO har文件和普通小文件读取的性能对比
        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("har:///staging/shuguang/rec/archive/"+year+"-"+month+"-"+day+".har/00/52101749947900.bd", partition);
        // 每一个bd文件，flat为pb字节流的集合
        // TODO 需要测试一下flatMap是否会重新分发key，产生网络损耗
        JavaRDD<String> dataRDD = bdBinaryFiles_pairRDD.flatMap(b_stream -> {
            String battle_id = StringUtils.getSplitIndex(b_stream._1.substring(0, b_stream._1.length() - 3), "/", -1);
            PortableDataStream dataStream = b_stream._2;
            BDRead BDRead = new BDRead();
            byte[] data = BDRead.get_data(dataStream.toArray());
            List<String> pb_bytes_list = new ArrayList<String>();
            // 游戏command解析，底层调用录像工具so
            BdUtils bdUtils = new BdUtils();
            bdUtils.analysis(data, new OutFuncI() {
                @Override
                public void OutputFunc(Pointer msg, NativeLong sz) {
                    byte[] pb_bytes = bdUtils.getPbBytes(msg, sz);
                    if (pb_bytes != null && pb_bytes.length != 0) {
                        String data = bdUtils.union(battle_id, pb_bytes);
                        pb_bytes_list.add(data);
                    } else {
                        System.out.println("---- invalid msg");
                    }
                }
            });
            return pb_bytes_list.iterator();
        });
        // 保存中间结果
        dataRDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/"+ year + "/" + month + "/" + day +"/ods_total");
    }
}
