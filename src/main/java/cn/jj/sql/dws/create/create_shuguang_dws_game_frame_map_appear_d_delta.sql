-- 1、统计AI在地图中的位置分布，以类似热力图的形式呈现；
-- 	目的：评价AI的位置及路线合理性
-- 	维度：阵营、玩家类型（AI或玩家）、局内时间、分段、模式、英雄、职业、地图区域、关键区域
-- 	指标：出现次数、朝向
-- 	展示方式：时间切片热力图
-- 	问题：需要确认热力图接入mis的实现方式；

-- 表名规范：数据库名_dws_数据域_粒度_主体_自定义命名标签_刷新周期标识(d|hi|mm)_[增量标识(delta)]
-- 命名规范：小驼峰，关键字后面+"_col"
-- 属性注释参照来源表。规范：dws层包含的源字段注释都不标记，使用源表注释
drop table if exists shuguang.shuguang_dws_game_frame_map_appear_d_delta;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dws_game_frame_map_appear_d_delta
(
    battle_id        	BIGINT,
--     主键
    map_area_id                BIGINT,
--     维度
    camp_id				INT,
    id               	BIGINT,
    player_type          INT,
    battle_time_id         	BIGINT,
--     局内时间退化维度
    frame_no    BIGINT COMMENT '局内时间退化维度',
    second_col      INT COMMENT '局内秒数',
    minute_col      INT COMMENT '局内分钟数',
    stage       STRING COMMENT '局内时间退化维度',
    dragon_refresh    STRING COMMENT '局内时间退化维度',
    base_up    STRING COMMENT '局内时间退化维度',
--     局内时间退化维度--end
    pool_id             INT,
    elo 				INT,
    mode				INT,
    hero_id              INT,
    race_id              INT,
    battle_start_time   TIMESTAMP,
    ai_type	            INT,
    ai_base	            INT,
    deepai_num	        INT,
--     地图退化维度
    area_id             BIGINT,
    bs_area             STRING COMMENT '地图区域退化维度',
--     度量
    appear              INT
)
    COMMENT '描述地图不同区域出现次数。数据源为shuguang_dwd_game_t_hero_state_d_delta。主体为地图区域，粒度为每帧、每个分段、每个模式、每个英雄、每个职业、每个地图区域，对所有对局的出现次数做聚合。可以按照帧号、阵营、玩家类型、业务时间点、英雄、职业、业务区域分类进行筛选'
    PARTITIONED BY (game_server_version STRING, game_big_version_short STRING, game_key_version STRING, year STRING, month STRING, day STRING, part STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
--     STORED AS TEXTFILE;
    STORED AS PARQUET TBLPROPERTIES('parquet.compression'='SNAPPY');