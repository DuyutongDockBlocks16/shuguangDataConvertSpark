package cn.jj.simulation.utils;

/**
 * @program: JavaSOTest
 * @description:
 * @author: wangyb04
 * @create: 2021-04-28 14:51
 */
public class ByteUtils {
    public static byte[] int2bytes(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }


    public static int bytes2int(int offset, byte[] b){
        int res = 0;
        for(int i=0;i<b.length-offset;i++){
            res += (b[i+offset] & 0xff) << (i*8);
        }
        return res;
    }
    // 纯数字转换
    @Deprecated
    public static String bytes2string(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length+":");
        for (byte b: bytes) {
            sb.append(Integer.valueOf(b));
            sb.append("|");
        }
        return sb.toString();
    }

    // 纯数字转换
    @Deprecated
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

    // 字符串转16进制byte数组
    @Deprecated
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

    // 16进制byte数组转字符串
    @Deprecated
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
     * @Description:字节数组转16进制字符串，python中用binascii库的a2b_hex方法解析字符串，还原字节数组
     * @param b 字节数组
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
     * @param src 16进制字符串
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


    public static String getBattleId(String o_data) {
        byte[] o_data_bytes = o_data.getBytes();
        int length = new Byte(o_data_bytes[0]).intValue();
        byte[] battle_id_bytes = new byte[length];
        System.arraycopy(o_data_bytes, 1, battle_id_bytes, 0, battle_id_bytes.length);
        String battle_id = new String(battle_id_bytes);
        return battle_id;
    }

    public static byte[] getData(String o_data) {
        byte[] o_data_bytes = o_data.getBytes();
        int length = new Byte(o_data_bytes[0]).intValue();
        byte[] data_bytes = new byte[o_data_bytes.length-length-1];
        System.arraycopy(o_data_bytes, 1+length, data_bytes, 0, data_bytes.length);
        return data_bytes;
    }
}
