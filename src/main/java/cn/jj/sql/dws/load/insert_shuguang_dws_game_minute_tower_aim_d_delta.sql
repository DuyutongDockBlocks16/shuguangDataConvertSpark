insert overwrite table shuguang.shuguang_dws_game_minute_tower_aim_d_delta PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}',part='${part}')
select
    dgsta.battle_id,
    dgsta.complete,
    dgsta.minute_col,
    dgsta.id,
    dgsta.player_type,
    dgsta.camp_id,
    dgsta.tower_inside_id,
    dgsta.tower_camp_id,
    dgsta.type,
    dgsta.pos_x,
    dgsta.pos_y,
    dgsta.map_area_id,
    dgsta.tower_id,
    dgsta.pool_id,
    dgsta.elo,
    dgsta.mode,
    dgsta.hero_id,
    dgsta.race_id,
    dgsta.battle_start_time,
    dgsta.ai_type,
    dgsta.ai_base,
    dgsta.deepai_num,
    dgsta.hp_section,
    dgsta.mana_section,
    sum(dgsta.aim_count_sum)
from (select * from shuguang.shuguang_dws_game_second_tower_aim_d_delta where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}' and part='${part}') dgsta
group by
    dgsta.battle_id,
    dgsta.complete,
    dgsta.minute_col,
    dgsta.id,
    dgsta.player_type,
    dgsta.camp_id,
    dgsta.tower_inside_id,
    dgsta.tower_camp_id,
    dgsta.type,
    dgsta.pos_x,
    dgsta.pos_y,
    dgsta.map_area_id,
    dgsta.tower_id,
    dgsta.pool_id,
    dgsta.elo,
    dgsta.mode,
    dgsta.hero_id,
    dgsta.race_id,
    dgsta.battle_start_time,
    dgsta.ai_type,
    dgsta.ai_base,
    dgsta.deepai_num,
    dgsta.hp_section,
    dgsta.mana_section