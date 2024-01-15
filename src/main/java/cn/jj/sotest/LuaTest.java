package cn.jj.sotest;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 * @program: JavaSOTest
 * @description:
 * @author: wangyb04
 * @create: 2021-04-06 16:03
 */
public class LuaTest {
    public static void main(String[] args) {
        Globals globals = JsePlatform.standardGlobals();
//        LuaJC.install(globals);
        globals.loadfile("fight.lua").call();
        LuaValue func = globals.get(LuaValue.valueOf("fight"));
        System.out.println(func.isclosure());  // Will be false when LuaJC is working.
        long startTime = System.currentTimeMillis();
        for(int i = 0; i < 1; i++) {
            String res = func.invoke(LuaValue.varargsOf(new LuaValue[]{LuaValue.valueOf(3), LuaValue.valueOf(5)})).tojstring();
            System.out.println("invoke: "+res);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
//        Globals globals = JsePlatform.standardGlobals();
//        LuaJC.install(globals);
//        LuaValue chunk = globals.load( "print('hello, world')", "main.lua");
//        System.out.println(chunk.isclosure());  // Will be false when LuaJC is working.
//        int count = 100;
//        for (int i = 0; i < count; i++) {
//            chunk.call();
//        }
    }
}
