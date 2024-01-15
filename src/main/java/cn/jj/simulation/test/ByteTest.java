package cn.jj.simulation.test;

import cn.jj.simulation.utils.ByteUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @program: JavaSOTest
 * @description:
 * @author: wangyb04
 * @create: 2021-04-26 16:23
 */
public class ByteTest {
    public static void main(String[] args) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(new File("/home/wangyb04/bdtest/byte.txt"), false);
        fileOutputStream.write(ByteUtils.int2bytes(1));
        fileOutputStream.write(ByteUtils.int2bytes(153));
        fileOutputStream.write(ByteUtils.int2bytes(1));
        fileOutputStream.write(ByteUtils.int2bytes(156));
        fileOutputStream.write(ByteUtils.int2bytes(1));
        fileOutputStream.write(ByteUtils.int2bytes(159));
        fileOutputStream.close();
//        FileInputStream fileInputStream = new FileInputStream("F:\\tmp\\byte.txt");
//        while(true) {
//            byte[] bytes = new byte[4];
//            int len = fileInputStream.read(bytes);
//            if (len==-1) break;
//            System.out.println("size: "+ bytes2int(bytes));
//            int size = bytes2int(bytes);
//            for (int i = 0; i < size; i++) {
//                byte[] contentbytes = new byte[4];
//                fileInputStream.read(contentbytes);
//                System.out.println("content: "+ bytes2int(contentbytes));
//            }
//        }
//        fileInputStream.close();
    }
}
