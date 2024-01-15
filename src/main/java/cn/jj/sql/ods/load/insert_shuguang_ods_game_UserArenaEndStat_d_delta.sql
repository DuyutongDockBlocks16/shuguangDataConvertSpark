ALTER TABLE  shuguang.shuguang_ods_game_UserArenaEndStat_d_delta_tmp add if not exists PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}')
    LOCATION '/staging/shuguang/server/100055/UserArenaEndStat/${YEAR}/${MONTH}/${DAY}';

insert overwrite table shuguang.shuguang_ods_game_UserArenaEndStat_d_delta PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}') select
    get_json_object(DATA,'$.battleid'),
    get_json_object(DATA,'$.modetid'),
    get_json_object(DATA,'$.role_id'),
    get_json_object(DATA,'$.factionid'),
    get_json_object(DATA,'$.disc_secs'),
    get_json_object(DATA,'$.idle_secs'),
    get_json_object(DATA,'$.illegal'),
    get_json_object(DATA,'$.doubt_illegal'),
    get_json_object(DATA,'$.conn_tcp_times'),
    get_json_object(DATA,'$.disc_tcp_times'),
    get_json_object(DATA,'$.conn_udp_times'),
    get_json_object(DATA,'$.disc_udp_times'),
    get_json_object(DATA,'$.duration'),
    get_json_object(DATA,'$.disc_tcp_sec'),
    get_json_object(DATA,'$.disc_udp_sec'),
    get_json_object(DATA,'$.first_tcp'),
    get_json_object(DATA,'$.first_udp'),
    get_json_object(DATA,'$.last_disc_tcp'),
    get_json_object(DATA,'$.last_disc_udp'),
    get_json_object(DATA,'$.idle_first'),
    get_json_object(DATA,'$.idle_cut'),
    get_json_object(DATA,'$.idle_run')
    ,get_json_object(DATA,'$.invalid_play_secs')
    ,get_json_object(DATA,'$.deepai_host_secs')
    ,get_json_object(DATA,'$.strategyai_host_secs')
    ,get_json_object(DATA,'$.negative_secs')
    ,get_json_object(DATA,'$.logtime')
from shuguang.shuguang_ods_game_UserArenaEndStat_d_delta_tmp
where year='${YEAR}' AND month='${MONTH}' AND day='${DAY}';

