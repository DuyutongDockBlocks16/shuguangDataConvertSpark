package cn.jj.simulation.utils;

import org.apache.lucene.util.RamUsageEstimator;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-09-28 14:53
 */
public class MemorySizeUtil {
    public static long getMb(Object obj) {

        if (obj == null) {
            return 0l;
        }
        //计算指定对象本身在堆空间的大小，单位字节
        long byteCount = RamUsageEstimator.shallowSizeOf(obj);
        if (byteCount == 0l) {
            return 0l;
        }
        long oneMb = 1 * 1024 * 1024;

        if (byteCount < oneMb) {
            return 1l;
        }

        Double v = Double.valueOf(byteCount) / oneMb;
        return v.longValue();
    }
}
