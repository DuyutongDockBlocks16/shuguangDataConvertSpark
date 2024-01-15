package cn.jj.simulation.entity;

import cn.jj.simulation.utils.DimUtils;
import cn.jj.simulation.utils.HiveUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-10-29 15:35
 */
public class HeroState implements Serializable {

    public String battle_id;
    public String frame_no;
    public String second_col;
    public String minute_col;
    public String id;
    public String camp_id;
    public String area_id;
    public String hp_percent;
    public String hp_section;
    public String mana_percent;
    public String mana_section;
    public String level_col;
    public String exp;
    public String curGold;
    public String gold;
    public String kill;
    public String dead;
    public String assist;
    public String isDead;
    public int appear;
    public String tp_begintime;
    public String tp_endtime;
    public String portalCD;
    public String springPortalSuccess;
    public String bServerAI;
    public String hero_id;
    public String race_id;
    public String playerType;

    public HeroState(String line) {
        String[] lines = line.split(String.valueOf(HiveUtils.FIELDS_TERMINATED));
        this.battle_id = lines[0];
        this.frame_no = lines[1];
        this.second_col = DimUtils.battle_time.get_second(lines[1]);
        this.minute_col = DimUtils.battle_time.get_minute(lines[1]);
        this.id = lines[3];
        this.camp_id = lines[4];
        this.area_id = DimUtils.map_area.get_area_id(lines[5], lines[6]);
        String hp_percent = DimUtils.hp_section.get_hp_percent(lines[9], lines[10]);
        this.hp_percent = hp_percent;
        this.hp_section = DimUtils.hp_section.get_hp_section(hp_percent);
        String mana_percent = DimUtils.mana_section.get_mana_percent(lines[13], lines[14]);
        this.mana_percent = mana_percent;
        this.mana_section = DimUtils.mana_section.get_mana_section(mana_percent);
        this.level_col = lines[17];
        this.exp = lines[18];
        this.curGold = lines[19];
        this.gold = lines[20];
        this.kill = lines[30];
        this.dead = lines[31];
        this.assist = lines[32];
        this.isDead = lines[58];
        this.appear = StringUtils.isEmpty(lines[58])?1:("true".equals(lines[58])?0:1);
        this.tp_begintime = lines[59];
        this.tp_endtime = lines[60];
        this.portalCD = lines[62];
        this.springPortalSuccess = lines[64];
        this.bServerAI = lines[70];
        this.hero_id = lines[73];
        this.race_id = lines[74];
        this.playerType = lines[80];
    }
}
