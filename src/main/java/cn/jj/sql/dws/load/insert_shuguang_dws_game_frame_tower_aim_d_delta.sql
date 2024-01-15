insert overwrite table shuguang.shuguang_dws_game_frame_tower_aim_d_delta PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}',part='${part}')
select
    /*+ STREAMTABLE(shuguang_dwd_game_t_hero_state_d_delta) */
    dgths.battle_id,
    dgths.complete,
    dgths.battle_time_id,
    dgbt.frame_no,
    dgbt.second_col,
    dgbt.minute_col,
    dgbt.stage,
    dgbt.dragon_refresh,
    dgbt.base_up,
    dgths.id,
    dgths.player_type,
    dgths.camp_id,
    dgtts.id,
    dgtts.camp_id,
    dgtts.type,
    dgtts.pos_x,
    dgtts.pos_y,
    dgtts.map_area_id,
    dgtts.tower_id,
    dgb.pool_id,
    dgb.match_elo,
    dgb.mode_tid,
    dgths.hero_id,
    dgths.race_id,
    dgb.battle_start_time,
    dgths.ai_type,
    dgths.ai_base,
    dgths.deepai_num,
    dgths.hp_section,
    dgths.mana_section,
    1
from (select * from shuguang.shuguang_dwd_game_t_hero_state_d_delta where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}' and part='${part}') dgths
    inner join shuguang.shuguang_dim_game_battle_time dgbt on dgths.battle_time_id = dgbt.id and dgbt.version = '${dim_version}'
    inner join (select * from shuguang.shuguang_dwd_game_Battle_d_delta where year='${YEAR}' and month='${MONTH}' and day='${DAY}') dgb on dgb.battle_id = dgths.battle_id
    inner join (select * from shuguang.shuguang_dwd_game_t_tower_state_d_delta where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}' and part='${part}') dgtts on dgths.battle_id = dgtts.battle_id and dgths.id = dgtts.target_id and dgtts.battle_time_id = dgths.battle_time_id