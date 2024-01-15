package cn.jj.simulation.test;

import cn.jj.simulation.test.OutFuncI;
import cn.jj.simulation.utils.BDRead;
import cn.jj.simulation.utils.BdUtils;
import cn.jj.simulation.utils.GameThinPbUtils;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StateSimulationPb2 {

    public static void main(String[] args) throws Exception {

        String year = args[0];
        String month = args[1];
        String day = args[2];
        // task 并发数量
        int partition = Integer.valueOf(args[3]);
        String file_name = args[4];

        SparkConf conf = new SparkConf().setAppName("StateSimulationPb");
        conf.set("mapreduce.map.output.compress", "false");
        conf.set("mapreduce.output.fileoutputformat.compress", "false");
        conf.set("mapred.map.output.compress", "false");
        conf.set("mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.map.output.compress", "false");
        conf.set("spark.hadoop.mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.output.compress", "false");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/2021-10-19/00/"+file_name);
        JavaRDD<PortableDataStream> bdBinaryFiles_repartition_pairRDD = bdBinaryFiles_pairRDD.values().repartition(partition);
        // 每一个bd文件，先用版本号过滤，再flat为pb字节流的集合，最后flat为解析数据的集合
        JavaRDD<String> state_pb_RDD = bdBinaryFiles_repartition_pairRDD.flatMap(b_stream -> {
            List<String> pb_lists = new ArrayList<String>();
            String bd_version = BDRead.get_version(b_stream.toArray());
            byte[] data = BDRead.get_data(b_stream.toArray());
            // 游戏command解析，底层调用录像工具so
            BdUtils bdUtils = new BdUtils();
            try {
                bdUtils.analysis(data, bd_version, new OutFuncI() {
                    @Override
                    public void OutputFunc(Pointer msg, NativeLong sz) {
                        byte[] pb_bytes = bdUtils.getPbBytes(msg, sz);
                        if (pb_bytes != null && pb_bytes.length != 0) {
                            String s = toHexString(pb_bytes);
                            pb_lists.add(s);
                        } else {
                            System.out.println("---- invalid msg");
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("---- exception ----");
            }
            return pb_lists.iterator();
        });

        String first = state_pb_RDD.first();

        byte[] pb_bytes = toByteArray(first);
        System.out.print("---- bytes: ");
        for (byte sb: pb_bytes) {
            System.out.print(sb);
        }
        System.out.println();
        List<String> state_list = new ArrayList<String>();
        state_list.addAll(GameThinPbUtils.getStateList("10011", pb_bytes));
        for (String state: state_list) {
            System.out.println("state2: "+state);
        }

//        File file = new File("./stateSimulation_pb.txt");
//        if(!file.exists()){
//            file.createNewFile();
//        }
//
//        //true = append file
//        FileWriter fileWritter = new FileWriter(file.getName(),true);
//        fileWritter.write(first);
//        fileWritter.flush();
//        fileWritter.close();
//        BufferedReader in = new BufferedReader(new FileReader(file.getName()));
//        String line = null;
//        List<String> state_list = new ArrayList<String>();
//        while((line = in.readLine()) !=null) {
//            byte[] pb_bytes = toByteArray(line);
//            state_list.addAll(GameThinPbUtils.getStateList("10011", pb_bytes));
//        }
//        for (String state: state_list) {
//            System.out.println("state: "+state);
//        }
    }

    public static byte[] toByteArray(String hexString) {
        hexString = hexString.toLowerCase();
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {// 因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

    public static String toHexString(byte[] byteArray) {
        String str = null;
        if (byteArray != null && byteArray.length > 0) {
            StringBuffer stringBuffer = new StringBuffer(byteArray.length);
            for (byte byteChar : byteArray) {
                stringBuffer.append(String.format("%02X", byteChar));
            }
            str = stringBuffer.toString();
        }
        return str;
    }
}
