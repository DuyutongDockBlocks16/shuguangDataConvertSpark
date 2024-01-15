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
public interface FightSO extends Library, Serializable {
    // fight会直接在JNA的library.path中搜索libfight.so这个模块（windows下是搜索libfight.dll）
    // 可以通过设置JVM选项-jna.library.path来设置JNA的搜索路径，默认可以包含当前路径。Spark中通过--conf spark.driver/executor.extraJavaOptions=-Djna.library.path来配置
    FightSO Instance = (FightSO) Native.loadLibrary("fight", FightSO.class);
    int luafight(int x, int y);
}
