drop table if exists shuguang.shuguang_dim_game_map_area;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dim_game_map_area
(
    id       BIGINT COMMENT '代理键id',
    x_section   BIGINT COMMENT 'x区间',
    y_section   BIGINT COMMENT 'y区间',
    area_id     BIGINT COMMENT '地图区域代码',
    bs_area     STRING COMMENT '区域业务含义（红蓝buff点，大龙点，塔点，基地点，默认为普通区域）'
)
    COMMENT '地图区域维表。包含区域的业务含义。缓慢变化维type 2，不同版本保留历史。分区字段为版本号'
--     分区字段。每个版本重新生成一次分区数据（不管维度是否变化）。
    PARTITIONED BY (version STRING COMMENT '有效版本号')
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;