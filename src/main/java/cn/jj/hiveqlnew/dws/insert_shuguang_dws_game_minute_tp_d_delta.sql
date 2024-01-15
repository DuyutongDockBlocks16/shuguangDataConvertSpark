insert overwrite table shuguang.shuguang_dws_game_minute_tp_d_delta PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}')
select
    dgstp.battle_id,
    dgstp.map_area_id,
    dgstp.area_id,
    dgstp.bs_area,
    dgstp.camp_id,
    dgstp.id,
    dgstp.player_type,
    dgstp.hero_id,
    dgstp.race_id,
    dgstp.hp_percent,
    dgstp.mana_percent,
    dgstp.tp_begin_minute_col,
    sum(dgstp.tp_action_sum)
from (select * from shuguang.shuguang_dws_game_second_tp_d_delta where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}') dgstp
group by
    dgstp.battle_id,
    dgstp.map_area_id,
    dgstp.area_id,
    dgstp.bs_area,
    dgstp.camp_id,
    dgstp.id,
    dgstp.player_type,
    dgstp.hero_id,
    dgstp.race_id,
    dgstp.hp_percent,
    dgstp.mana_percent,
    dgstp.tp_begin_minute_col
