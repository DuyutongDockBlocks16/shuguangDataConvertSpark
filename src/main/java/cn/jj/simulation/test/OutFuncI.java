package cn.jj.simulation.test;

import com.sun.jna.Callback;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

/**
 * @program: JavaSOTest
 * @description: 函数指针
 * @author: wangyb04
 * @create: 2021-05-08 15:37
 */
public interface OutFuncI extends Callback {
    void OutputFunc(Pointer msg, NativeLong sz);
}
