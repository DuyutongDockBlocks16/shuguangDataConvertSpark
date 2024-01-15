-- 20210601144922_formal_1.0.6.2_6001_ce347f5d_2b00d8c1_cd81619b
LOAD DATA INPATH '/staging/shuguang/rec_parse/dim/${dim_version}/mapArea.txt'
    OVERWRITE INTO TABLE shuguang.shuguang_dim_game_map_area
    PARTITION(version='${dim_version}');