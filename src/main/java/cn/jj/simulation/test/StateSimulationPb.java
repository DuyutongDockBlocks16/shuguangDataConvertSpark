package cn.jj.simulation.test;

import cn.jj.simulation.test.OutFuncI;
import cn.jj.simulation.utils.BDRead;
import cn.jj.simulation.utils.BdUtils;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.apache.hadoop.io.BytesWritable;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.input.PortableDataStream;

import java.util.ArrayList;
import java.util.List;

public class StateSimulationPb {


    public static String bytes2string(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length+":");
        for (byte b: bytes) {
            sb.append(Integer.valueOf(b));
            sb.append("|");
        }
        return sb.toString();
    }

    public static byte[] string2bytes(String str) {
        int length = Integer.valueOf(str.split(":")[0]);
        String bytes_str = str.split(":")[1];
        byte[] bytes = new byte[length];
        int index = 0;
        for (String byte_str: bytes_str.split("\\|")) {
            bytes[index++] = Integer.valueOf(byte_str).byteValue();
        }
        return bytes;
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

    /**

     * @Title:bytes2HexString

     * @Description:字节数组转16进制字符串

     * @param b

     *            字节数组

     * @return 16进制字符串

     * @throws

     */

    public static String bytes2HexString(byte[] b) {
        StringBuffer result = new StringBuffer();
        String hex;

        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);

            if (hex.length() == 1) {
                hex = '0' + hex;

            }

            result.append(hex.toUpperCase());

        }
        return result.toString();

    }

    /**

     * @Title:hexString2Bytes

     * @Description:16进制字符串转字节数组

     * @param src

     *            16进制字符串

     * @return 字节数组

     * @throws

     */

    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;

        byte[] ret = new byte[l];

        for (int i = 0; i < l; i++) {
            ret[i] = (byte) Integer

                    .valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();

        }

        return ret;

    }

    public static void main(String[] args) throws Exception {

        String year = args[0];
        String month = args[1];
        String day = args[2];
        // task 并发数量
        int partition = Integer.valueOf(args[3]);
        String part = args[4];
        String file_name = args[5];

        SparkConf conf = new SparkConf().setAppName("StateSimulationPb");
        conf.set("mapreduce.map.output.compress", "false");
        conf.set("mapreduce.output.fileoutputformat.compress", "false");
        conf.set("mapred.map.output.compress", "false");
        conf.set("mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.map.output.compress", "false");
        conf.set("spark.hadoop.mapred.output.fileoutputformat.compress", "false");
        conf.set("spark.hadoop.mapred.output.compress", "false");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaPairRDD<String, PortableDataStream> bdBinaryFiles_pairRDD = sc.binaryFiles("hdfs:///staging/shuguang/rec/"+year+"-"+month+"-"+day+"/"+part+"/"+file_name);
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
                            String s = bytes2HexString(pb_bytes);
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
        state_pb_RDD.saveAsTextFile("hdfs:///staging/shuguang/rec_parse/pb/game/state/delta/"+year+"/"+month+"/"+day+"/"+part+"/test/");
    }
}
