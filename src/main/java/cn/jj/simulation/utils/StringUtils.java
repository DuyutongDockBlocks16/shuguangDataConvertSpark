package cn.jj.simulation.utils;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-06-03 09:47
 */
public class StringUtils {

    public static String getSplitIndex(String str, String split, int index) {
        String[] ss = str.split(split);
        if (index <0) {
            return ss[ss.length+index];
        } else {
            return ss[index];
        }
    }
}
