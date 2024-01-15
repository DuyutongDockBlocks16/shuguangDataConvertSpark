package cn.jj.simulation.history;

import cn.jj.simulation.test.OutFuncI;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

public interface GAGSIMU extends Library {

    GAGSIMU Instance = (GAGSIMU) Native.loadLibrary("./libgagsimuc.so", GAGSIMU.class);
    // 初始化相关环境
    void init();
    // 初始化战斗环境，创建input函数的回调函数
    void createGag(OutFuncI outFunc);
    // 销毁战斗环境
    void destroyGag();
    /**
     * input函数对应着n个outFunc回调函数，input和outFunc调用是同步串行的，等多个outFunc都返回结果，input再返回
     * @author wangyb04
     * @date 2021/5/13 11:56
     * @params frame:字节流的头指针；size:字节流的长度；frameSeq:帧号（自增）
     * @return
     * @throws
    */
    void input(Pointer frame, NativeLong size, long frameSeq);
}
