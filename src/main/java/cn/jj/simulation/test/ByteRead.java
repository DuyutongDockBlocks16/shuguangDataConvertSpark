package cn.jj.simulation.test;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;

public interface ByteRead extends Library {

    ByteRead Instance = (ByteRead) Native.loadLibrary("./byteread.so", ByteRead.class);
    int input(Pointer frame, NativeLong size);

}
