package cn.jj.simulation.utils;

import net.sf.json.JSONObject;
import scala.Tuple6;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-10-29 14:54
 */
public class DataUtils {
    // "370.03":100.15
    public static List<Tuple6<String,String,String,String,String,String>> deal_hosting_section(String host_secs, String type, int hosting_minute_section_interval) {
        System.out.println("---- host_secs: "+host_secs);
        List<Tuple6<String,String,String,String,String,String>> res_list = new ArrayList<Tuple6<String,String,String,String,String,String>>();
        JSONObject host_secs_obj = JSONUtil.JSONString2JSONObject(host_secs);
        for (Object key: host_secs_obj.keySet()) {
            double hosting_begin_sec = new Double(key.toString());
            double hosting_sec = new Double(host_secs_obj.get(key).toString());
            double hosting_end_sec = new Double(hosting_begin_sec+hosting_sec);
            int begin_section = new Double(hosting_begin_sec).intValue() / (hosting_minute_section_interval * 60) + 1;
            int end_section = new Double(hosting_end_sec).intValue() / (hosting_minute_section_interval * 60) + 1;
            // 将section横跨的几个区间分开
            for (int section=begin_section; section <= end_section; section++) {
                double section_begin_sec = (section-1)*hosting_minute_section_interval * 60;
                double section_end_sec = section*hosting_minute_section_interval * 60;
                if (section_begin_sec < hosting_begin_sec) section_begin_sec = hosting_begin_sec;
                if (section_end_sec > hosting_end_sec) section_end_sec = hosting_end_sec;
                double section_second = section_end_sec-section_begin_sec;
                double section_minute = section_second / 60;
                res_list.add(new Tuple6<String,String,String,String,String,String>(
                        type,
                        String.valueOf(section),
                        String.valueOf(section_begin_sec),
                        String.valueOf(section_end_sec),
                        String.valueOf(section_second),
                        String.valueOf(section_minute)));
            }
        }
        return res_list;
    }
}
