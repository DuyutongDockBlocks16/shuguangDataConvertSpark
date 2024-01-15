LOAD DATA INPATH '/staging/shuguang/rec_parse/dws/game/second_map_appear/delta/${YEAR}/${MONTH}/${DAY}/${game_server_version}/${game_big_version_short}/${game_key_version}/${part}'
    OVERWRITE INTO TABLE shuguang.shuguang_dws_game_second_map_appear_d_delta_direct
    PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}',part='${part}');
