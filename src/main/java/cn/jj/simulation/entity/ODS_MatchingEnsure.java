package cn.jj.simulation.entity;

import cn.jj.simulation.utils.JSONUtil;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-11-03 10:32
 */
public class ODS_MatchingEnsure implements Serializable {

    public String matching_id;
    public String pool_id;
    public BigDecimal team_elo;
    public String server;
    public String success;
    public String ensure;
    public String role_id;

    public ODS_MatchingEnsure(String line) {
        JSONObject lineObj = JSONUtil.JSONString2JSONObject(line);
        this.matching_id = lineObj.get("matching_id")==null?"":lineObj.get("matching_id").toString();
        this.pool_id = lineObj.get("pool_id")==null?"":lineObj.get("pool_id").toString();
        this.team_elo = new BigDecimal(lineObj.get("team_elo")==null?"0":lineObj.get("team_elo").toString());
        this.server = lineObj.get("server")==null?"":lineObj.get("server").toString();
        this.success = lineObj.get("success")==null?"-100":lineObj.get("success").toString();
        this.ensure = lineObj.get("ensure")==null?"-100":lineObj.get("ensure").toString();
        this.role_id = lineObj.get("role_id")==null?"-100":lineObj.get("role_id").toString();
    }
}
