insert overwrite table shuguang.shuguang_dws_game_minute_tower_aim_d_delta_direct PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}',part='${part}')
select
    dgsta.battle_id,
    dgsta.minute_col,
    dgsta.camp_id,
    dgsta.tower_inside_id,
    dgsta.tower_camp_id,
    dgsta.type,
    dgsta.tower_area_id,
    dgsta.pool_id,
    dgsta.elo,
    dgsta.hero_id,
    dgsta.race_id,
    dgsta.ai_type,
    dgsta.ai_base,
    dgsta.deepai_num,
    dgsta.hp_section,
    dgsta.mana_section,
    dgsta.player_type,
    sum(dgsta.aim_count_sum)
from (select * from shuguang.shuguang_dws_game_second_tower_aim_d_delta_direct where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}' and part='${part}') dgsta
group by
    dgsta.battle_id,
    dgsta.minute_col,
    dgsta.camp_id,
    dgsta.tower_inside_id,
    dgsta.tower_camp_id,
    dgsta.type,
    dgsta.tower_area_id,
    dgsta.pool_id,
    dgsta.elo,
    dgsta.hero_id,
    dgsta.race_id,
    dgsta.ai_type,
    dgsta.ai_base,
    dgsta.deepai_num,
    dgsta.hp_section,
    dgsta.mana_section,
    dgsta.player_type