package cn.jj.sotest;

import com.sun.jna.Library;
import com.sun.jna.Native;

import java.io.Serializable;

/**
 * @program: LuaSpark
 * @description:
 * @author: wangyb04
 * @create: 2021-04-02 16:38
 */
public interface Testso extends Library, Serializable {
    Testso Instance = (Testso) Native.loadLibrary("fight", Testso.class);
//        Testso Instance = (Testso)Native.loadLibrary("E:\\LuaSpark\\target\\classes\\libsayhello.so", Testso.class);
    int luafight(int x, int y);
    int luasum();
}
