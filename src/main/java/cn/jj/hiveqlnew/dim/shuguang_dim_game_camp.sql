-- 表名规范：数据库名_dim_数据域_维度标识
-- TODO 暂时未启用
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dim_game_camp
(
    id          BIGINT COMMENT '代理键id',
    camp        STRING COMMENT '阵营（红方、蓝方）'
)
    COMMENT '阵营维表。分为红方和蓝方。缓慢变化维type 2。'
--     分区字段。每个版本重新生成一次分区数据（不管维度是否变化）。
    PARTITIONED BY (version STRING COMMENT '有效版本号')
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;