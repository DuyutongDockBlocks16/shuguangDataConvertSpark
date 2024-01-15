package cn.jj.simulation.entity;

import cn.jj.simulation.utils.JSONUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import scala.Tuple6;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-10-29 16:02
 */
public class UserArenaEndStat {

    public String battleid;
    public String role_id;
    // TODO 数据可能为"null"
    public String strategyai_host_secs;
    // TODO 数据可能为"null"
    public String deepai_host_secs;
    public List<Hosting> hosting_list;
    public class Hosting {
        public String battleid;
        public String role_id;
        public String hosting_minute_section_interval;
        public String hosting_type;
        public String hosting_minute_section;
        public String hosting_begin_sec;
        public String hosting_end_sec;
        public String hosting_second;
        public String hosting_minute;

        public Hosting(String battleid, String role_id,
                       String hosting_minute_section_interval, String hosting_type,
                       String hosting_minute_section, String hosting_begin_sec, String hosting_end_sec,
                       String hosting_second, String hosting_minute) {
            this.battleid = battleid;
            this.role_id = role_id;
            this.hosting_minute_section_interval = hosting_minute_section_interval;
            this.hosting_type = hosting_type;
            this.hosting_minute_section = hosting_minute_section;
            this.hosting_begin_sec = hosting_begin_sec;
            this.hosting_end_sec = hosting_end_sec;
            this.hosting_second = hosting_second;
            this.hosting_minute = hosting_minute;
        }
//        key = battleid:role_id:hosting_minute_section:hosting_type
        public String get_key() {
            return this.battleid+":"+this.role_id+":"+this.hosting_minute_section+":"+this.hosting_type;
        }
    }

    public UserArenaEndStat(String line, int hosting_minute_section_interval) {
        JSONObject lineObj = JSONUtil.JSONString2JSONObject(line);
        this.battleid = lineObj.get("battleid")==null?"":lineObj.get("battleid").toString();
        this.role_id = lineObj.get("battleid")==null?"":lineObj.get("battleid").toString();
        this.deepai_host_secs = lineObj.get("battleid")==null?"":lineObj.get("battleid").toString();
        this.strategyai_host_secs = lineObj.get("battleid")==null?"":lineObj.get("battleid").toString();
        this.hosting_list = get_Hosting_list(hosting_minute_section_interval);
    }

    // "370.03":100.15
    public List<Hosting> deal_hosting_section(String host_secs, String type, int hosting_minute_section_interval) {
        List<Hosting> res_list = new ArrayList<Hosting>();
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
                res_list.add(new Hosting(this.battleid, this.role_id, String.valueOf(hosting_minute_section_interval),
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

    public List<Hosting> get_Hosting_list(int hosting_minute_section_interval) {
        List<Hosting> res_list = new ArrayList<Hosting>();
        List<Hosting> deepai_host_list = new ArrayList<Hosting>();
        List<Hosting> strategyai_host_list = new ArrayList<Hosting>();

        if (!StringUtils.isEmpty(deepai_host_secs) && !"null".equals(deepai_host_secs) && !"Null".equals(deepai_host_secs)) {
            deepai_host_list = deal_hosting_section(deepai_host_secs, "1", hosting_minute_section_interval);
            if (!StringUtils.isEmpty(strategyai_host_secs) && !"null".equals(strategyai_host_secs) && !"Null".equals(strategyai_host_secs)) {
                strategyai_host_list = deal_hosting_section(strategyai_host_secs, "1", hosting_minute_section_interval);
            }
        } else {
            if (!StringUtils.isEmpty(strategyai_host_secs) && !"null".equals(strategyai_host_secs) && !"Null".equals(strategyai_host_secs)) {
                strategyai_host_list = deal_hosting_section(strategyai_host_secs, "1", hosting_minute_section_interval);
            }
        }
        res_list.addAll(deepai_host_list);
        res_list.addAll(strategyai_host_list);
        return res_list;
    }
}
