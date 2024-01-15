LOAD DATA INPATH '/staging/shuguang/rec_parse/dim/${dim_version}/manaSection.txt'
    OVERWRITE INTO TABLE shuguang.shuguang_dim_game_mana_section
    PARTITION(version='${dim_version}');