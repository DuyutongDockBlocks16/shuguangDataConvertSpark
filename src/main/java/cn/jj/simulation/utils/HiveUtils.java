package cn.jj.simulation.utils;

import java.util.List;

/**
 * @program: ShuGuangGAGSimu
 * @description:
 * @author: wangyb04
 * @create: 2021-05-28 10:25
 */
public class HiveUtils {

    public static char FIELDS_TERMINATED = '\001';
    public static char COLLECTION_ITEMS_TERMINATED = '\002';
    public static char MAP_KEYS_TERMINATED = '\003';
    public static String emptyValue = "empty";

    /**
     * 只能加工基础类型的list
     * @author wangyb04
     * @date 2021/5/28 14:49
     * @params
     * @return
     * @throws
    */
    public static String buildARRAY(List list) {
        StringBuilder sb = new StringBuilder();
        for(Object e: list) {
            sb.append(e).append(MAP_KEYS_TERMINATED);
        }
        return sb.length()==0?null:sb.substring(0, sb.length()-1);
    }
}
