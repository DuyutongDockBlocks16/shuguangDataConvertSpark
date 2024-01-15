insert overwrite table shuguang.shuguang_dwd_game_t_tp_success_d_delta PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}',part='${part}')
select
    dgths.battle_id,
    dgths.camp_id,
    dgths.id,
    dgths.player_type,
    dgths.hero_id,
    dgths.race_id,
    dgths.battle_time_id,
    dgbt.frame_no,
    dgbt.second_col,
    dgbt.minute_col,
    dgbt.stage,
    dgbt.dragon_refresh,
    dgbt.base_up,
    1
from (select * from shuguang.shuguang_dwd_game_t_hero_state_d_delta where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}' and part='${part}' and spring_portal_success = TRUE) dgths
         inner join shuguang.shuguang_dim_game_battle_time dgbt on dgths.battle_time_id = dgbt.id and dgbt.version = '${dim_version}'
