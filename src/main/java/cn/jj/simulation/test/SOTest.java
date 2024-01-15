package cn.jj.simulation.test;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface SOTest extends Library {

    SOTest Instance = (SOTest) Native.loadLibrary("./libgagsimu_c.so", SOTest.class);
    void input();
}
