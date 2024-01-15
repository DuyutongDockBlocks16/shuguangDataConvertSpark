LOAD DATA INPATH '/staging/shuguang/rec_parse/dim/${dim_version}/hpSection.txt'
    OVERWRITE INTO TABLE shuguang.shuguang_dim_game_hp_section
    PARTITION(version='${dim_version}');