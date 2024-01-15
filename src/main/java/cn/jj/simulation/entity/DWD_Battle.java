package cn.jj.simulation.entity;

import cn.jj.simulation.utils.HiveUtils;

/**
 * @program: sgods
 * @description:
 * @author: wangyb04
 * @create: 2021-10-29 17:49
 */
public class DWD_Battle {

    public String battle_id;
    public String pool_id;
    public String match_elo;
    public String battle_start_time;
    public String mode_tid;
    public String user_num;
    public String server;

    public DWD_Battle(String line) {
        String[] lines = line.split(String.valueOf(HiveUtils.FIELDS_TERMINATED));
        this.battle_id = lines[0];
        this.pool_id = lines[5];
        this.match_elo = lines[4];
        this.battle_start_time = lines[6];
        this.mode_tid = lines[2];
        this.user_num = lines[9];
        this.server = lines[15];
    }

    public DWD_Battle(String line, boolean is_full_fields) {
        this(line);
        if (is_full_fields) {
            String[] lines = line.split(String.valueOf(HiveUtils.FIELDS_TERMINATED));
            this.battle_id = lines[0];
            this.pool_id = lines[5];
            this.match_elo = lines[4];
            this.battle_start_time = lines[6];
            this.mode_tid = lines[2];
            this.user_num = lines[9];
            this.server = lines[15];
        }
    }
    //            key = battle_id
    public String get_key() {
        return this.battle_id;
    }
}
