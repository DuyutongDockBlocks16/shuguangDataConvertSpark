LOAD DATA INPATH '/staging/shuguang/rec_parse/dim/${dim_version}/battleTime.txt'
    OVERWRITE INTO TABLE shuguang.shuguang_dim_game_battle_time
    PARTITION(version='${dim_version}');