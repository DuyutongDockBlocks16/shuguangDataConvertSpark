LOAD DATA INPATH '/staging/shuguang/rec_parse/ods/game/Hero_state/delta/${YEAR}/${MONTH}/${DAY}/${game_server_version}/${game_big_version_short}/${game_key_version}'
    OVERWRITE INTO TABLE shuguang.shuguang_ods_game_hero_state_d_delta
    PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}');
