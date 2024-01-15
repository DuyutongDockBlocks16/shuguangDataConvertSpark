package cn.jj.simulation.history;

import cn.jj.simulation.test.OutFuncI;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

public interface GAGSIMUMUITY extends Library {

    // 初始化相关环境
    void init(String respath);
    void simulateGag(Pointer data, NativeLong datasize, OutFuncI outfunc);
}
