package cn.jj.simulation.dimTasks;

import cn.jj.simulation.utils.HiveUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @program: sgods
 * @description: hp区间维表生成任务
 * @author: wangyb04
 * @create: 2021-07-29 11:09
 */
public class HpSection {
    public static void main(String[] args) {
        char fieldsTerminated = HiveUtils.FIELDS_TERMINATED;
        int hp_max = Integer.valueOf(args[0]);
        String file_name = args[1];
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file_name, false);
            bw = new BufferedWriter(fw);
            int surrogate_key = 1;
            for(int hp_percent=0; hp_percent<=hp_max; hp_percent++) {
                StringBuilder sb = new StringBuilder();
                sb.append(surrogate_key++)
                        .append(fieldsTerminated)
                        .append(hp_percent)
                        .append(fieldsTerminated)
                        .append("["+hp_percent/10+"0%"+(hp_percent==100?"]":", "+((hp_percent/10)+1)+"0%)"));
                bw.write(sb.toString());
                bw.newLine();
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
