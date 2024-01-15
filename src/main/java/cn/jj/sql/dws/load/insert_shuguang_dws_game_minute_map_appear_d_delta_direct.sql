insert overwrite table shuguang.shuguang_dws_game_minute_map_appear_d_delta_direct PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}',part='${part}')
select
    dgsma.battle_id,
    dgsma.map_area_id,
    dgsma.camp_id,
    dgsma.minute_col,
    dgsma.pool_id,
    dgsma.elo,
    dgsma.hero_id,
    dgsma.race_id,
    dgsma.ai_type,
    dgsma.ai_base,
    dgsma.deepai_num,
    dgsma.bs_area,
    dgsma.player_type,
    if (sum(dgsma.appear) > 0, 1, 0)
from
    (select * from shuguang.shuguang_dws_game_second_map_appear_d_delta_direct where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}' and part='${part}') dgsma
group by
    dgsma.battle_id,
    dgsma.map_area_id,
    dgsma.camp_id,
    dgsma.minute_col,
    dgsma.pool_id,
    dgsma.elo,
    dgsma.hero_id,
    dgsma.race_id,
    dgsma.ai_type,
    dgsma.ai_base,
    dgsma.deepai_num,
    dgsma.bs_area,
    dgsma.player_type