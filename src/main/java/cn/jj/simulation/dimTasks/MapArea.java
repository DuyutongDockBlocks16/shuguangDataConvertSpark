package cn.jj.simulation.dimTasks;

import cn.jj.simulation.utils.HiveUtils;
import com.google.common.collect.HashMultimap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

/**
 * @program: sgods
 * @description: 地图区域维表生成任务
 * @author: wangyb04
 * @create: 2021-07-29 16:01
 */
public class MapArea {

    public static HashMultimap<Integer, String> bs_area_multi_map = HashMultimap.create();

    static {
        bs_area_multi_map.put(-1, "普通区域");
        bs_area_multi_map.put(45, "水晶巨人");
        bs_area_multi_map.put(46, "水晶巨人");
        bs_area_multi_map.put(55, "水晶巨人");
        bs_area_multi_map.put(56, "水晶巨人");
        bs_area_multi_map.put(1, "蓝方高地");
        bs_area_multi_map.put(2, "蓝方高地");
        bs_area_multi_map.put(3, "蓝方高地");
        bs_area_multi_map.put(11, "蓝方高地");
        bs_area_multi_map.put(12, "蓝方高地");
        bs_area_multi_map.put(13, "蓝方高地");
        bs_area_multi_map.put(21, "蓝方高地");
        bs_area_multi_map.put(22, "蓝方高地");
        bs_area_multi_map.put(23, "蓝方高地");
        bs_area_multi_map.put(78, "红方高地");
        bs_area_multi_map.put(79, "红方高地");
        bs_area_multi_map.put(80, "红方高地");
        bs_area_multi_map.put(88, "红方高地");
        bs_area_multi_map.put(89, "红方高地");
        bs_area_multi_map.put(90, "红方高地");
        bs_area_multi_map.put(98, "红方高地");
        bs_area_multi_map.put(99, "红方高地");
        bs_area_multi_map.put(100, "红方高地");
        bs_area_multi_map.put(63, "大龙");
        bs_area_multi_map.put(64, "大龙");
        bs_area_multi_map.put(37, "小龙");
        bs_area_multi_map.put(38, "小龙");
    }

    public static void main(String[] args) {
        char fieldsTerminated = HiveUtils.FIELDS_TERMINATED;
        String file_name = args[1];
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file_name, false);
            bw = new BufferedWriter(fw);
            int surrogate_key = 1;
            int area_id = 1;
            for (int y_section=0; y_section < 10; y_section++) {
                for (int x_section=0; x_section < 10; x_section++) {
                    Set<String> bs_areas = bs_area_multi_map.get(-1);;
                    if (bs_area_multi_map.containsKey(area_id)) bs_areas = bs_area_multi_map.get(area_id);
                    for (String bs_area: bs_areas) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(surrogate_key)
                                .append(fieldsTerminated)
                                .append(x_section)
                                .append(fieldsTerminated)
                                .append(y_section)
                                .append(fieldsTerminated)
//                    TODO area_id
                                .append(area_id)
                                .append(fieldsTerminated)
//                    TODO bs_area
                                .append(bs_area);
                        bw.write(sb.toString());
                        bw.newLine();
                        surrogate_key++;
                    }
                    area_id++;
                }
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw!=null) {
                    bw.close();
                }
                if (fw!=null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
