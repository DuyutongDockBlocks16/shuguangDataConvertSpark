package cn.jj.simulation.entity;

import cn.jj.simulation.utils.HiveUtils;
import cn.jj.simulation.utils.JSONUtil;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-10-29 17:49
 * @editor: duyt
 */
public class ODS_Battle implements Serializable {

    public String battleid;
    public String gametype;
    public String modetid;
    public String battletid;
    public String begintime;
    public String playtime;
    public String playersnum;
    public String usernum;
    public String fakeusernum;
    public String robotnum;
    public String gather_report_sec;
    public String illegal_player_1;
    public String illegal_player_3;

    public ODS_Battle(String line) {
        JSONObject lineObj = JSONUtil.JSONString2JSONObject(line);
        this.battleid = lineObj.get("battleid")==null?"":lineObj.get("battleid").toString();
        this.gametype = lineObj.get("gametype")==null?"":lineObj.get("gametype").toString();
        this.modetid = lineObj.get("modetid")==null?"":lineObj.get("modetid").toString();
        this.battletid = lineObj.get("battletid")==null?"":lineObj.get("battletid").toString();
        this.begintime = lineObj.get("begintime")==null?"":lineObj.get("begintime").toString();
        this.playtime = lineObj.get("playtime")==null?"":lineObj.get("playtime").toString();
        this.playersnum = lineObj.get("playersnum")==null?"":lineObj.get("playersnum").toString();
        this.usernum = lineObj.get("usernum")==null?"-1":lineObj.get("usernum").toString();
        this.fakeusernum = lineObj.get("fakeusernum")==null?"":lineObj.get("fakeusernum").toString();
        this.robotnum = lineObj.get("robotnum")==null?"":lineObj.get("robotnum").toString();
        this.gather_report_sec = lineObj.get("gather_report_sec")==null?"":lineObj.get("gather_report_sec").toString();
//        没有挂机，会为Null，手动转成0
        this.illegal_player_1 = lineObj.get("illegal_player_1")==null?"0":lineObj.get("illegal_player_1").toString();
        this.illegal_player_3 = lineObj.get("illegal_player_3")==null?"":lineObj.get("illegal_player_3").toString();
    }
}
