package cn.jj.simulation.dimTasks;

import cn.jj.simulation.utils.HiveUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @program: sgods
 * @description: 局内时间维表生成任务
 * @author: wangyb04
 * @create: 2021-07-28 20:57
 */
public class BattleTime {
    public static void main(String[] args) {
        char fieldsTerminated = HiveUtils.FIELDS_TERMINATED;
        int minute_max = Integer.valueOf(args[0]);
        String file_name = args[1];
        int hosting_minute_section_interval = Integer.valueOf(args[2]);
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file_name, false);
            bw = new BufferedWriter(fw);
            int surrogate_key = 1;
            int frame_no = 1;
            int second = 1;
            int minute = 1;
            for(int x=1; x<=minute_max; x++) {
                for(int y=1; y<=60; y++) {
                    for (int z=1; z<=15; z++) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(surrogate_key++)
                                .append(fieldsTerminated)
                                .append(frame_no++)
                                .append(fieldsTerminated)
                                .append(second)
                                .append(fieldsTerminated)
                                .append(minute)
                                .append(fieldsTerminated)
                                // TODO stage
                                .append("")
                                .append(fieldsTerminated)
                                // TODO dragon_refresh
                                .append("")
                                .append(fieldsTerminated)
                                // TODO base_up
                                .append("")
                                .append(fieldsTerminated)
                                .append(minute/hosting_minute_section_interval);
                        bw.write(sb.toString());
                        bw.newLine();
                    }
                    second++;
                }
                minute++;
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
