package cn.jj.simulation.test;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface Kero extends Library {

    Kero Instance = (Kero) Native.loadLibrary("./libkero.so", Kero.class);
    void kero_a(int a);
    void kero_b();
    void kero_c();
}
