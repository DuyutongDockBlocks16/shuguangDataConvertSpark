insert overwrite table shuguang.shuguang_dws_game_hero_hosting_delta_direct PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}',part='${part}')
select
    hero_id,
    hosting_type,
    battle_time_section,
    mode_tid,
    pool_id,
    elo,
    ai_type,
    ai_base,
    deepai_num,
    user_num,
    server,
    count(battle_id),
    sum(section_kill),
    sum(section_assist),
    sum(section_dead)
from
    shuguang.shuguang_dws_game_section_hosting_delta_direct
where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}' and part='${part}'
group by
    hero_id,
    hosting_type,
    battle_time_section,
    mode_tid,
    pool_id,
    elo,
    ai_type,
    ai_base,
    deepai_num,
    user_num,
    server