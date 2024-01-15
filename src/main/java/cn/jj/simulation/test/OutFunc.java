package cn.jj.simulation.test;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

/**
 * @program: JavaSOTest
 * @description:
 * @author: wangyb04
 * @create: 2021-05-08 15:43
 */
public class OutFunc implements OutFuncI {
    @Override
    public void OutputFunc(Pointer msg, NativeLong sz) {
//        byte[] bytes = new byte[sz.intValue()];
//        msg.read(0, bytes, 0, sz.intValue());
//        String msgStr = null;
//        try {
//            msgStr = new String(bytes, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        System.out.println("------ msg: "+ msgStr);
        System.out.println("--- msg size: "+sz.intValue());
    }
}
