-- 表名规范：数据库名_dws_数据域_粒度_主体_自定义命名标签_刷新周期标识(d|hi|mm)_[增量标识(delta)]
-- 命名规范：小驼峰，关键字后面+"_col"
-- 属性注释参照来源表
drop table if exists shuguang.shuguang_dws_game_minute_map_appear_d_delta;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dws_game_minute_map_appear_d_delta
(
    battle_id           BIGINT,
--     主键
    map_area_id                INT,
--     维度
    camp_id				INT,
    id               	BIGINT,
    player_type          INT,
--     局内时间退化维度
    minute_col      INT COMMENT '局内分钟数',
--     局内时间退化维度--end
    elo 				INT,
    mode				INT,
    hero_id              INT,
    race_id              INT,
--     地图退化维度
    bs_area             STRING COMMENT '地图区域退化维度',
--     度量
    appear              INT
)
    COMMENT '描述地图不同区域出现次数。数据源为shuguang_dws_game_second_map_appear_d_delta。主体为地图区域，粒度为每分钟、每个分段、每个模式、每个英雄、每个职业、每个地图区域，对所有对局的出现次数做聚合。可以按照帧号、阵营、玩家类型、英雄、职业、业务区域分类进行筛选'
    PARTITIONED BY (game_server_version STRING, game_big_version_short STRING, game_key_version STRING, year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
--     STORED AS TEXTFILE;
    STORED AS PARQUET TBLPROPERTIES('parquet.compression'='SNAPPY');