drop table if exists shuguang.shuguang_dwd_game_bd_version_count_d_delta;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dwd_game_bd_version_count_d_delta
(
    game_server               STRING COMMENT '对局服务区（formal为远航服，early为先锋服）',
    game_big_version               STRING COMMENT '对局大版本号（3位，“.”分隔）',
    game_keyversion                STRING COMMENT '对局keyversion',
    version                 STRING    comment 'bd文件版本',
    count_col                   BIGINT    comment '版本计数'
)
    COMMENT 'bd文件版本统计。可以查询每天的bd文件对应的版本号。用于小版本更新时，补数。'
    partitioned by (year string, month string, day string, part STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    stored as TEXTFILE ;