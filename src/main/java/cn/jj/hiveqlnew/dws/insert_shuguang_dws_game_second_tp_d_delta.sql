insert overwrite table shuguang.shuguang_dws_game_second_tp_d_delta PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}')
select
    dgatp.battle_id,
    dgatp.map_area_id,
    dgatp.area_id,
    dgatp.bs_area,
    dgatp.camp_id,
    dgatp.id,
    dgatp.player_type,
    dgatp.hero_id,
    dgatp.race_id,
    dgatp.hp_percent,
    dgatp.mana_percent,
    dgatp.tp_begin_second_col,
    dgatp.tp_begin_minute_col,
    sum(dgatp.tp_action)
from (select * from shuguang.shuguang_dwd_game_a_tp_d_delta where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}') dgatp
group by
    dgatp.battle_id,
    dgatp.map_area_id,
    dgatp.area_id,
    dgatp.bs_area,
    dgatp.camp_id,
    dgatp.id,
    dgatp.player_type,
    dgatp.hero_id,
    dgatp.race_id,
    dgatp.hp_percent,
    dgatp.mana_percent,
    dgatp.tp_begin_second_col,
    dgatp.tp_begin_minute_col
