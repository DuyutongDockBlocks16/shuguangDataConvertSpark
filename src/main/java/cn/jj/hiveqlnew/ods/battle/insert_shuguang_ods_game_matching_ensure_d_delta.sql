ALTER TABLE  shuguang.shuguang_ods_game_matching_ensure_d_delta_tmp add if not exists PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}')
    LOCATION '/staging/shuguang/server/100055/MatchingEnsure/${YEAR}/${MONTH}/${DAY}';

insert overwrite table shuguang.shuguang_ods_game_matching_ensure_d_delta PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}') select
    get_json_object(DATA,'$.matching_id'),
    get_json_object(DATA,'$.role_id'),
    get_json_object(DATA,'$.pool_id'),
    get_json_object(DATA,'$.protect_pool_id'),
    get_json_object(DATA,'$.origin_pool_id'),
    get_json_object(DATA,'$.elo'),
    get_json_object(DATA,'$.match_elo'),
    get_json_object(DATA,'$.team_elo'),
    get_json_object(DATA,'$.battle_sum'),
    get_json_object(DATA,'$.leader_id'),
    get_json_object(DATA,'$.team_mem_num'),
    get_json_object(DATA,'$.camp_tids'),
    get_json_object(DATA,'$.success'),
    get_json_object(DATA,'$.factionid'),
    get_json_object(DATA,'$.ensure'),
    get_json_object(DATA,'$.modetid'),
    get_json_object(DATA,'$.start'),
    get_json_object(DATA,'$.duration'),
    get_json_object(DATA,'$.reason'),
    get_json_object(DATA,'$.server'),
    get_json_object(DATA,'$.keyversion')
from shuguang.shuguang_ods_game_matching_ensure_d_delta_tmp
where year='${YEAR}' AND month='${MONTH}' AND day='${DAY}';

