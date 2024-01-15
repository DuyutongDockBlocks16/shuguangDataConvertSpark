package cn.jj.simulation.test;

import cn.jj.simulation.utils.ByteUtils;
import com.sun.jna.Memory;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;


/**
 * @program: JavaSOTest
 * @description:
 * @author: wangyb04
 * @create: 2021-04-28 14:55
 */
public class ByteReadTest {
    public static void main(String[] args) throws Exception {
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream("/home/wangyb04/bdtest/byte.txt"));
//        System.setProperty("jna.library.path", "./");
//        System.out.println(System.getProperty("jna.library.path"));
        while(true) {
            byte[] bytes = new byte[4];
            int len = inputStream.read(bytes);
            if (len==-1) break;
            int size = ByteUtils.bytes2int(0, bytes)*4;
            System.out.println("size: "+ size);
            byte[] contentbytes = new byte[size];
            inputStream.read(contentbytes);
            Pointer p = new Memory(size);
            p.write(0, contentbytes, 0, size);
            int content = ByteRead.Instance.input(p, new NativeLong(size));
            System.out.println("content: "+ content);
        }
        inputStream.close();
    }
}
