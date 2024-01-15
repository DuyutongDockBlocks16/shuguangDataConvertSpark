package cn.jj.simulation.entity;

import cn.jj.simulation.utils.JSONUtil;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-10-29 15:55
 */
public class ODS_PlayerBattle implements Serializable {

    public String battleid;
    public String ai_type;
    public String ai_base;
    public String deepainum;
    public String herotid;
    public String role_id;
    public String modetid_battle;
    public String usernum;
    public BigDecimal score;
    public String modetid;

    public ODS_PlayerBattle(String line) {
        JSONObject lineObj = JSONUtil.JSONString2JSONObject(line);
        this.battleid = lineObj.get("battleid")==null?"":lineObj.get("battleid").toString();
        this.ai_type = lineObj.get("ai_type")==null?"":lineObj.get("ai_type").toString();
        this.ai_base = lineObj.get("ai_base")==null?"":lineObj.get("ai_base").toString();
        this.deepainum = lineObj.get("deepainum")==null?"":lineObj.get("deepainum").toString();
        this.herotid = lineObj.get("herotid")==null?"":lineObj.get("herotid").toString();
        this.role_id = lineObj.get("role_id")==null?"":lineObj.get("role_id").toString();
        this.modetid_battle = lineObj.get("modetid_battle")==null?"":lineObj.get("modetid_battle").toString();
        this.usernum = lineObj.get("usernum")==null?"":lineObj.get("usernum").toString();
        this.score = new BigDecimal(lineObj.get("score")==null?"0":lineObj.get("score").toString());
        this.modetid = lineObj.get("modetid")==null?"":lineObj.get("modetid").toString();
    }

    public String get_key() {
        return this.battleid + ":" + this.herotid;
    }
}
