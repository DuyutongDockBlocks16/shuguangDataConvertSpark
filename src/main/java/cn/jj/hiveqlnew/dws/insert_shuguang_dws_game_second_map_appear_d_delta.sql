insert overwrite table shuguang.shuguang_dws_game_second_map_appear_d_delta PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}')
select
    dgfma.battle_id,
    dgfma.map_area_id,
    dgfma.camp_id,
    dgfma.id,
    dgfma.player_type,
    dgfma.second_col,
    dgfma.minute_col,
    dgfma.elo,
    dgfma.mode,
    dgfma.hero_id,
    dgfma.race_id,
    dgfma.bs_area,
    if (sum(dgfma.appear) > 0, 1, 0)
from
    (select * from shuguang.shuguang_dws_game_frame_map_appear_d_delta where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}') dgfma
group by
    dgfma.battle_id,
    dgfma.map_area_id,
    dgfma.camp_id,
    dgfma.id,
    dgfma.player_type,
    dgfma.second_col,
    dgfma.minute_col,
    dgfma.elo,
    dgfma.mode,
    dgfma.hero_id,
    dgfma.race_id,
    dgfma.bs_area