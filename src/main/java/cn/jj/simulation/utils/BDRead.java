package cn.jj.simulation.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.input.PortableDataStream;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @program: JavaSOTest
 * @description: 字节读取工具类
 * @author: wangyb04
 * @create: 2021-05-13 11:57
 */
public class BDRead {

    public static final String UNKNOWN_VERSION = "UNKNOWN_VERSION";

    /**
     * 将字节流的头数据读掉
     * @author wangyb04
     * @date 2021/5/13 12:00
     * @params inputStream: 字节流
     * @return 
     * @throws
    */
    public static boolean read_head(InputStream inputStream) {
        int cbyte = -1;
        int lbyte = -1;
        try {
            while((cbyte = inputStream.read()) != -1) {
                // 连续两个换行符则到达数据体部分
                if (cbyte==10) {
                    if (lbyte==10) {
                        System.out.println("---- read head end. ----");
                        return true;
                    }
                }
                lbyte=cbyte;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取bd文件的数据体
     * @author wangyb04
     * @date 2021/7/15 10:14
     * @params
     * @return
     * @throws
     */
    public static byte[] get_data(byte[] b_stream) {
        byte lbyte = -1;
        int data_index = 0;
        for (int i=0; i < b_stream.length; i++) {
            byte cbyte = b_stream[i];
            if (cbyte==10) {
                if (lbyte==10) {
                    data_index = i+1;
                    break;
                }
            }
            lbyte=cbyte;
        }
        return Arrays.copyOfRange(b_stream, data_index, b_stream.length);
    }

    /**
     * 获取bd文件头的第n行数据
     * @author wangyb04
     * @date 2021/7/15 10:14
     * @params
     * @return
     * @throws
    */
    public static String get_head_line(byte[] b_stream, int get_line_num) {
        int start_index = 0;
        int end_index = 0;
        int line_num = 1;
        boolean start_get = false;
        for (int i=0; i < b_stream.length; i++) {
            byte cbyte = b_stream[i];
            // 遇到换行符，行数+1
            if (cbyte==10) {
                line_num++;
                if (start_get) {
                    start_get = false;
                    break;
                }
                continue;
            }
            if (line_num==get_line_num && !start_get) {
                start_get = true;
                start_index=i;
            }
            if (start_get) {
                end_index=i+1;
            }
        }
        return start_index >= end_index?null:new String(Arrays.copyOfRange(b_stream, start_index, end_index));
    }

//    public static void main(String[] args) {
//        String s = new String("51011750014327\n" +
//                "5434560\n" +
//                "49c13004d5878f6d72e4dd8bac3f2f56\n" +
//                "32b7e19420fa059a33a6e48a7d5014ca\n" +
//                "20c18b87b118f7aab44873dfe6376c77\n" +
//                "1b2c402dac38001174f05d25a9dc3d4a\n" +
//                "{\"7200\":485017381,\"900\":25045599,\"4500\":69872741,\"1800\":115203683,\"5400\":256013322,\"-99\":3554233061441151546,\"2700\":86656003,\"3600\":130152710,\"6300\":245389046,\"-3\":65930853255974318,\"-2\":3275234904481238355,\"-1\":-3925309171355417484}\n" +
//                "20210601144922_formal_1.0.6.2_6001_ce347f5d_2b00d8c1_cd81619b\n" +
//                "---\n" +
//                "\n");
//        byte[] bytes = s.getBytes();
//        String head_line = get_version(bytes);
//        System.out.println(head_line);
//        System.out.println(1);
//    }

    /**
     * 获得bd文件版本，服务区类型（formal|early）_大版本_keyversion
     * @author wangyb04
     * @date 2021/8/13 14:38
     * @params
     * @return
     * @throws
    */
    public static String get_version(byte[] b_array) {
        try {
            String game_server_version = get_game_server_version(b_array);
            String game_big_version_short = get_game_big_version_short(b_array);
            String game_key_version = get_game_key_version(b_array);
            if (game_server_version!=null && game_big_version_short!=null && game_key_version!=null) return game_server_version + "_" + game_big_version_short + "_" + game_key_version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return UNKNOWN_VERSION;
    }

    public static String get_game_server_version(byte[] b_array) {
        return get_game_server_version(b_array, null);
    }

    public static String get_game_server_version(String version) {
        return get_game_server_version(null, version);
    }

    public static String get_game_server_version(byte[] b_array, String version) {
        String game_server_version = null;
        try {
            if (StringUtils.isEmpty(version)) {
                String bd_version = get_head_line(b_array, 8);
                String[] bd_versions = bd_version.split("_");
    //            游戏服务版本，分为远航（formal）和先锋（early）
                game_server_version = bd_versions[1];
            } else {
                game_server_version = version.split("_")[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return game_server_version;
    }

    public static String get_game_big_version_short(byte[] b_array) {
        return get_game_big_version_short(b_array, null);
    }

    public static String get_game_big_version_short(String version) {
        return get_game_big_version_short(null, version);
    }

    public static String get_game_big_version_short(byte[] b_array, String version) {
        String game_big_version_short = null;
        try {
            if (StringUtils.isEmpty(version)) {
                String bd_version = get_head_line(b_array, 8);
                String[] bd_versions = bd_version.split("_");
                //            游戏服务版本，分为远航（formal）和先锋（early）
                String game_big_version = bd_versions[2];
                game_big_version_short = game_big_version.substring(0, game_big_version.indexOf(".", game_big_version.indexOf(".", game_big_version.indexOf(".") + 1) + 1));
            } else {
                game_big_version_short = version.split("_")[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return game_big_version_short;
    }

    public static String get_game_key_version(byte[] b_array) {
        return get_game_key_version(b_array, null);
    }

    public static String get_game_key_version(String version) {
        return get_game_key_version(null, version);
    }

    public static String get_game_key_version(byte[] b_array, String version) {
        String game_key_version = null;
        try {
            if (StringUtils.isEmpty(version)) {
                game_key_version = get_head_line(b_array, 6);
            } else {
                game_key_version = version.split("_")[2];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return game_key_version;
    }
}
