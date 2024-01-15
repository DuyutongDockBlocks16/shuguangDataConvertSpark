drop table if exists shuguang.shuguang_ods_game_bd_version_count_delta;
create table if not exists shuguang.shuguang_ods_game_bd_version_count_delta (
     version    STRING    comment 'bd文件版本',
     count_col    BIGINT    comment '版本计数'
)
    COMMENT 'bd文件版本统计'
    partitioned by (year string, month string, day string, part string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    stored as TEXTFILE;
