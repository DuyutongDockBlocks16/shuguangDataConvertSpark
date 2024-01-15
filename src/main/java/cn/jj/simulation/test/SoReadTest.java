package cn.jj.simulation.test;

import cn.jj.simulation.utils.ByteUtils;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

/**
 * @program: JavaSOTest
 * @description:
 * @author: wangyb04
 * @create: 2021-04-28 14:55
 */
public class SoReadTest {
    public static void main(String[] args) throws Exception {
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream("./51010377287341.bd"));
//        NativeLibrary.addSearchPath("gagsimu.so", "./");
        System.setProperty("jna.debug_load", "true");
//        System.setProperty("jna.library.path", "./");
        while(true) {
            long frameSeq = 1;
            byte[] bytes = new byte[8];
            int len = inputStream.read(bytes);
            if (len==-1) break;
            int size = ByteUtils.bytes2int(4, bytes);
            System.out.println("size: "+ size);
            byte[] contentbytes = new byte[size];
            inputStream.read(contentbytes);
            Pointer frame = new Memory(size);
            frame.write(0, contentbytes, 0, size);
            SOTest.Instance.input();
            System.out.println("input ok. ");
        }
        inputStream.close();
    }
}
