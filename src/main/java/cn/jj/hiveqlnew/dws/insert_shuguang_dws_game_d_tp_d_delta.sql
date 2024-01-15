insert overwrite table shuguang.shuguang_dws_game_d_tp_d_delta PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}')
select
    dgbtp.map_area_id,
    dgbtp.bs_area,
    dgbtp.camp_id,
    dgbtp.id,
    dgbtp.match_elo,
    dgbtp.mode_tid,
    dgbtp.hero_id,
    dgbtp.race_id,
    dgbtp.hp_section,
    dgbtp.mana_section,
    sum(dgbtp.tp_action),
    sum(dgbtp.tp_success)
from
    (select * from shuguang.shuguang_dws_game_battle_tp_d_delta where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}') dgbtp
group by
    dgbtp.map_area_id,
    dgbtp.bs_area,
    dgbtp.camp_id,
    dgbtp.id,
    dgbtp.match_elo,
    dgbtp.mode_tid,
    dgbtp.hero_id,
    dgbtp.race_id,
    dgbtp.hp_section,
    dgbtp.mana_section