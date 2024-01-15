-- 表名规范：数据库名_dws_数据域_粒度_主体_自定义命名标签_刷新周期标识(d|hi|mm)_[增量标识(delta)]
-- 命名规范：小驼峰，关键字后面+"_col"
-- 属性注释参照来源表
drop table if exists shuguang.shuguang_dws_game_minute_map_appear_d_delta_direct;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dws_game_minute_map_appear_d_delta_direct
(
    -- 主键（自然键）
    battle_id        	BIGINT COMMENT '对局id',
--     主键
    map_area_id                INT,
--     维度
    camp_id				INT,
--     局内时间退化维度
    minute_col      INT COMMENT '局内分钟数',
--     局内时间退化维度--end
    pool_id             STRING,
    elo 				INT,
    hero_id              INT,
    race_id              INT,
    ai_type	            INT,
    ai_base	            INT,
    deepai_num	        INT,
--     地图退化维度
    bs_area             STRING COMMENT '地图区域退化维度',
    player_type         INT COMMENT '0是真人，非0是AI',
--     度量
    appear              INT
)
    COMMENT '描述地图不同区域每分钟出现次数。数据来源自Spark任务直接加工数据'
    PARTITIONED BY (game_server_version STRING, game_big_version_short STRING, game_key_version STRING, year STRING, month STRING, day STRING, part STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;
--     STORED AS PARQUET TBLPROPERTIES('parquet.compression'='SNAPPY');