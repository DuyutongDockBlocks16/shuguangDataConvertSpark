-- 表名规范：数据库名_dim_数据域_维度标识
drop table if exists shuguang.shuguang_dim_game_hp_section;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dim_game_hp_section
(
    id                  INT COMMENT '代理键id',
    hp_percent          INT COMMENT '血量占比。百分数，保留两位小数。',
    hp_section          STRING COMMENT '血量区间'
)
    COMMENT 'hp区间维表。缓慢变化维type 2，不同版本保留历史。分区字段为版本号'
--     分区字段。每个版本重新生成一次分区数据（不管维度是否变化）。
    PARTITIONED BY (version STRING COMMENT '有效版本号')
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;