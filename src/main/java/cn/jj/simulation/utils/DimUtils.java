package cn.jj.simulation.utils;

import cn.jj.simulation.dimTasks.MapArea;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-09-28 16:37
 */
public class DimUtils {
    public static class battle_time {
        public static String get_second(String frame_no) {
            if (StringUtils.isEmpty(frame_no) || Integer.valueOf(frame_no) < 0) return "";
            return String.valueOf(Integer.valueOf(frame_no)/15+1);
        }

        public static String get_minute(String frame_no) {
            String second = get_second(frame_no);
            if (StringUtils.isEmpty(second)) return "";
            return String.valueOf(Integer.valueOf(second)/60+1);
        }

        public static String get_hosting_minute_section(String frame_no, int hosting_minute_section_interval) {
            String minute = get_minute(frame_no);
            if (StringUtils.isEmpty(minute)) return "";
            return String.valueOf(Integer.valueOf(minute)/hosting_minute_section_interval+1);
        }
    }

    public static class map_area {
        public static String get_area_id(String location_x, String location_y) {
            if (StringUtils.isEmpty(location_x) || Integer.valueOf(location_x) < 0) return "";
            if (StringUtils.isEmpty(location_y) || Integer.valueOf(location_y) < 0) return "";
            int x_section = (Integer.valueOf(location_x)/10000 -40)/12;
            int y_section = (Integer.valueOf(location_y)/10000 -40)/12;
            return String.valueOf(y_section*10 + x_section + 1);
        }

        public static Set<String> get_bs_area(String area_id) {
            return MapArea.bs_area_multi_map.get(Integer.valueOf(area_id));
        }
    }

    public static class hp_section {
        public static String get_hp_percent(String hp, String hp_max) {
            if (StringUtils.isEmpty(hp_max) || Integer.valueOf(hp_max) <= 0) return "";
            if (StringUtils.isEmpty(hp) || Integer.valueOf(hp) < 0) return "";
            return String.valueOf(Math.floor(Double.valueOf(hp)/Double.valueOf(hp_max)*100)).split("\\.")[0];
        }

        public static String get_hp_section(String hp_percent) {
            if (StringUtils.isEmpty(hp_percent)) return "";
            int hp_percent_i = Integer.valueOf(hp_percent);
            return "["+hp_percent_i/10+"0%"+(hp_percent_i==100?"]":", "+((hp_percent_i/10)+1)+"0%)");
        }
    }

    public static class mana_section {
        public static String get_mana_percent(String mana, String mana_max) {
            if (StringUtils.isEmpty(mana_max) || Integer.valueOf(mana_max) <= 0) return "";
            if (StringUtils.isEmpty(mana) || Integer.valueOf(mana) < 0) return "";
            return String.valueOf(Math.floor(Double.valueOf(mana)/Double.valueOf(mana_max)*100)).split("\\.")[0];
        }

        public static String get_mana_section(String mana_percent) {
            if (StringUtils.isEmpty(mana_percent)) return "";
            int mana_percent_i = Integer.valueOf(mana_percent);
            return "["+mana_percent_i/10+"0%"+(mana_percent_i==100?"]":", "+((mana_percent_i/10)+1)+"0%)");
        }
    }
}
