package cn.jj.simulation.utils;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapred.lib.MultipleTextOutputFormat;

/**
 * @program: sgods
 * @description: 多文件输出，确保每个key的value都保存在一个文件中，并且可以对文件重命名
 * @author: wangyb04
 * @create: 2021-11-03 22:16
 */
public class RDDMultipleTextOutputFormat extends MultipleTextOutputFormat {
    @Override
    protected Object generateActualKey(Object key, Object value) {
        return NullWritable.get();
    }

    //    用battle_id命名文件
    @Override
    protected String generateFileNameForKeyValue(Object key, Object value, String name) {
        String battle_id = key.toString();
        return battle_id.split(":")[0]+".bpb";
    }

}
