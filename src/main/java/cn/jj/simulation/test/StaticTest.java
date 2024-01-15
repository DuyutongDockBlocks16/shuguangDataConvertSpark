package cn.jj.simulation.test;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-06-02 12:08
 */
public class StaticTest {

    static {
        System.out.println("---- StaticTest init! ----");
    }

    public static void test(Integer e) {
        System.out.println("---- call test!"+e+" ----");
    }
}
