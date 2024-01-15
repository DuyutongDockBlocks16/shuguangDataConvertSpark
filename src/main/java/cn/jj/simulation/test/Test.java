package cn.jj.simulation.test;

import cn.jj.simulation.utils.DataUtils;
import scala.Tuple6;

import java.util.List;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-08-10 15:12
 */
public class Test {
    public static void main(String[] args) {
        String host_secs = "{'370.03':200.15,'615.36':47.51}";
        List<Tuple6<String, String, String, String, String, String>> list = DataUtils.deal_hosting_section(host_secs, "1", 5);
        for(Tuple6<String, String, String, String, String, String> t: list) {
            System.out.println("---- v1: "+t._1());
            System.out.println("---- v2: "+t._2());
            System.out.println("---- v3: "+t._3());
            System.out.println("---- v4: "+t._4());
            System.out.println("---- v5: "+t._5());
            System.out.println("---- v6: "+t._6());
        }
    }
}
