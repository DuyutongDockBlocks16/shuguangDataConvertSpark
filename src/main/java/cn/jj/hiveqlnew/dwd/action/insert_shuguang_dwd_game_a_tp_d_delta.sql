insert overwrite table shuguang.shuguang_dwd_game_a_tp_d_delta PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}')
select
    a.battle_id,
    a.map_area_id,
    a.area_id,
    a.bs_area,
    a.camp_id,
    a.id,
    a.player_type,
    a.hero_id,
    a.race_id,
    a.hp_percent,
    a.mana_percent,
    a.tp_begintime,
    a.tp_endtime,
    a.tp_begin_battle_time_id,
    b.frame_no,
    b.second_col,
    b.minute_col,
    b.stage,
    b.dragon_refresh,
    b.base_up,
    a.tp_donecount,
    a.tp_real_donecount,
    a.tp_action
from (
         select
             dgths.battle_id as battle_id,
             dgma.id as map_area_id,
             dgma.area_id as area_id,
             dgma.bs_area as bs_area,
             dgths.camp_id as camp_id,
             dgths.id as id,
             dgths.player_type as player_type,
             dgths.hero_id as hero_id,
             dgths.race_id as race_id,
             min(dgths.hp_percent) as hp_percent,
             min(dgths.mana_percent) as mana_percent,
             dgths.tp_begintime as tp_begintime,
             dgths.tp_endtime as tp_endtime,
             min(dgbt.id) as tp_begin_battle_time_id,
             cast((dgths.tp_endtime-dgths.tp_begintime)/10000*15 as decimal(10,0)) as tp_donecount,
             max(dgbt.frame_no)-min(dgbt.frame_no) as tp_real_donecount,
             1 as tp_action
         from (select * from shuguang.shuguang_dwd_game_t_hero_state_d_delta where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}' and tp_begintime != -1 and tp_endtime != -1) dgths
                  inner join shuguang.shuguang_dim_game_battle_time dgbt on dgths.battle_time_id = dgbt.id and dgbt.version = '${dim_version}'
                  inner join shuguang.shuguang_dim_game_map_area dgma on dgths.map_area_id = dgma.id and dgma.version = '${dim_version}'
         group by
             dgths.battle_id,
             dgma.id,
             dgma.area_id,
             dgma.bs_area,
             dgths.camp_id,
             dgths.id,
             dgths.player_type,
             dgths.hero_id,
             dgths.race_id,
             dgths.tp_begintime,
             dgths.tp_endtime
     ) a inner join shuguang.shuguang_dim_game_battle_time b on a.tp_begin_battle_time_id = b.id and b.version = '${dim_version}'

