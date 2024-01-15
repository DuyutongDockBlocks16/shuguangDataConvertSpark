insert overwrite table shuguang.shuguang_dws_game_battle_tp_d_delta PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}')
select
    dgmtp.battle_id,
    dgmtp.map_area_id,
    dgmtp.area_id,
    dgmtp.bs_area,
    dgmtp.camp_id,
    dgmtp.id,
    dgmtp.player_type,
    dgmtp.hero_id,
    dgmtp.race_id,
    dgmtp.hp_percent,
    dgmtp.mana_percent,
    sum(dgmtp.tp_action_sum)
from (select * from shuguang.shuguang_dws_game_minute_tp_d_delta where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}') dgmtp
group by
    dgmtp.battle_id,
    dgmtp.map_area_id,
    dgmtp.area_id,
    dgmtp.bs_area,
    dgmtp.camp_id,
    dgmtp.id,
    dgmtp.player_type,
    dgmtp.hero_id,
    dgmtp.race_id,
    dgmtp.hp_percent,
    dgmtp.mana_percent
