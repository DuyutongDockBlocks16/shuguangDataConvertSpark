insert overwrite table shuguang.shuguang_dws_game_frame_map_appear_d_delta PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}')
select
    dgths.battle_id,
    dgths.map_area_id,
    dgths.camp_id,
    dgths.id,
    dgths.player_type,
    dgths.battle_time_id,
    dgbt.frame_no,
    dgbt.second_col,
    dgbt.minute_col,
    dgbt.stage,
    dgbt.dragon_refresh,
    dgbt.base_up,
    dgb.match_elo,
    dgb.mode_tid,
    dgths.hero_id,
    dgths.race_id,
    dgma.area_id,
    dgma.bs_area,
    sum(dgths.appear)
from
    (select * from shuguang.shuguang_dwd_game_t_hero_state_d_delta where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}') dgths
        inner join shuguang.shuguang_dim_game_battle_time dgbt on dgths.battle_time_id = dgbt.id and dgbt.version = '${dim_version}'
        inner join shuguang.shuguang_dim_game_map_area dgma on dgths.map_area_id = dgma.id and dgma.version = '${dim_version}'
        inner join (select * from shuguang.shuguang_dwd_game_Battle_d_delta where year='${YEAR}' and month='${MONTH}' and day='${DAY}') dgb on dgb.battle_id = dgths.battle_id
group by
    dgths.battle_id,
    dgths.map_area_id,
    dgths.camp_id,
    dgths.id,
    dgths.player_type,
    dgths.battle_time_id,
    dgbt.frame_no,
    dgbt.second_col,
    dgbt.minute_col,
    dgbt.stage,
    dgbt.dragon_refresh,
    dgbt.base_up,
    dgb.match_elo,
    dgb.mode_tid,
    dgths.hero_id,
    dgths.race_id,
    dgma.area_id,
    dgma.bs_area