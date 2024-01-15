LOAD DATA INPATH '/staging/shuguang/rec_parse/ods/game/version/${YEAR}/${MONTH}/${DAY}'
    OVERWRITE INTO TABLE shuguang.shuguang_ods_game_bd_version_count_delta
    PARTITION(year='${YEAR}',month='${MONTH}',day='${DAY}');


