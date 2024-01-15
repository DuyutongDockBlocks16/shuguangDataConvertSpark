LOAD DATA INPATH '/staging/shuguang/rec_parse/dws/game/second_tower_aim/delta/${YEAR}/${MONTH}/${DAY}/${game_server_version}/${game_big_version_short}/${game_key_version}/${part}'
    OVERWRITE INTO TABLE shuguang.shuguang_dws_game_second_tower_aim_d_delta_direct
    PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}',part='${part}');
