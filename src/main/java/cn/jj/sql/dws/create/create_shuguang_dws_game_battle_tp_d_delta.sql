-- 3、统计AI的回城（使用回城技能，并非走回泉水区）行为。
-- 	目的：评价AI对于回城技能使用的合理性
-- 	维度：阵营、玩家类型（AI或玩家）、局内时间、分段、英雄、职业、状态（血量蓝量当前比例）、地图位置
-- 	指标：回城的平均次数/总次数、回城成功次数
-- 	展示方式：报表
drop table if exists shuguang.shuguang_dws_game_battle_tp_d_delta;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dws_game_battle_tp_d_delta
(
--     主键
    battle_id        	BIGINT COMMENT '对局id',
--     维度
    map_area_id                BIGINT,
--     地图退化维度
    area_id                BIGINT,
    bs_area             STRING COMMENT '地图区域退化维度',
--     ----
    camp_id				INT,
    id               	BIGINT,
    player_type          INT,
    hero_id              INT,
    race_id              INT,
    pool_id             INT,
    elo                 INT,
    battle_start_time   TIMESTAMP,
    ai_type	            INT,
    ai_base	            INT,
    deepai_num	        INT,
    hp_percent          INT,
    mana_percent         INT,
--     度量
    tp_action_sum           INT COMMENT '触发tp次数累加，无事实度量'
)
    COMMENT '描述每次tp操作的累加事实，粒度为每分钟tp操作聚合。主要维度有阵营、玩家类型（AI或玩家）、局内时间、英雄、职业、状态（血量蓝量当前比例）、地图位置，主要度量有tp触发次数等'
    PARTITIONED BY (game_server_version STRING, game_big_version_short STRING, game_key_version STRING, year STRING, month STRING, day STRING, part STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
--     STORED AS TEXTFILE;
    STORED AS PARQUET TBLPROPERTIES('parquet.compression'='SNAPPY');