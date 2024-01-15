insert overwrite table shuguang.shuguang_dws_game_second_tower_aim_d_delta PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}',part='${part}')
select
    dgfta.battle_id,
    dgfta.complete,
    dgfta.second_col,
    dgfta.minute_col,
    dgfta.id,
    dgfta.player_type,
    dgfta.camp_id,
    dgfta.tower_inside_id,
    dgfta.tower_camp_id,
    dgfta.type,
    dgfta.pos_x,
    dgfta.pos_y,
    dgfta.map_area_id,
    dgfta.tower_id,
    dgfta.pool_id,
    dgfta.elo,
    dgfta.mode,
    dgfta.hero_id,
    dgfta.race_id,
    dgfta.battle_start_time,
    dgfta.ai_type,
    dgfta.ai_base,
    dgfta.deepai_num,
    dgfta.hp_section,
    dgfta.mana_section,
    sum(dgfta.aim_count)
from (select * from shuguang.shuguang_dws_game_frame_tower_aim_d_delta where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}' and part='${part}') dgfta
group by
    dgfta.battle_id,
    dgfta.complete,
    dgfta.second_col,
    dgfta.minute_col,
    dgfta.id,
    dgfta.player_type,
    dgfta.camp_id,
    dgfta.tower_inside_id,
    dgfta.tower_camp_id,
    dgfta.type,
    dgfta.pos_x,
    dgfta.pos_y,
    dgfta.map_area_id,
    dgfta.tower_id,
    dgfta.pool_id,
    dgfta.elo,
    dgfta.mode,
    dgfta.hero_id,
    dgfta.race_id,
    dgfta.battle_start_time,
    dgfta.ai_type,
    dgfta.ai_base,
    dgfta.deepai_num,
    dgfta.hp_section,
    dgfta.mana_section