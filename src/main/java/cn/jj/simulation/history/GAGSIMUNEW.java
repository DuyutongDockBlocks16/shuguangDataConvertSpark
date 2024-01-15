package cn.jj.simulation.history;

import cn.jj.simulation.test.OutFuncI;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

public interface GAGSIMUNEW extends Library {

    GAGSIMUNEW Instance = (GAGSIMUNEW) Native.loadLibrary("./gagsimu.so", GAGSIMUNEW.class);
    // 初始化相关环境
    void init();
    void simulateGag(Pointer data, NativeLong datasize, OutFuncI outfunc);
}
