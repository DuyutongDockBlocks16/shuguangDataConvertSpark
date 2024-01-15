ALTER TABLE  shuguang.shuguang_ods_game_battle_d_delta_tmp add if not exists PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}')
    LOCATION '/staging/shuguang/server/100055/Battle/${YEAR}/${MONTH}/${DAY}';

insert overwrite table shuguang.shuguang_ods_game_battle_d_delta PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}') select
    get_json_object(data,'$.battleid'),
    get_json_object(data,'$.gametype'),
    get_json_object(data,'$.modetid'),
    get_json_object(data,'$.battletid'),
    cast(get_json_object(data,'$.begintime') as int),
    get_json_object(data,'$.playtime'),
    get_json_object(data,'$.playersnum'),
    get_json_object(data,'$.usernum'),
    get_json_object(data,'$.fakeusernum'),
    get_json_object(data,'$.robotnum'),
    get_json_object(data,'$.gather_report_sec'),
    get_json_object(data,'$.illegal_player_1'),
    get_json_object(data,'$.illegal_player_3')
from shuguang.shuguang_ods_game_battle_d_delta_tmp
where year='${YEAR}' AND month='${MONTH}' AND day='${DAY}';

