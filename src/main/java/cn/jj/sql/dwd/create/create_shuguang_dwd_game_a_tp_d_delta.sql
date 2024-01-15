-- 3、统计AI的回城（使用回城技能，并非走回泉水区）行为。
-- 	目的：评价AI对于回城技能使用的合理性
-- 	维度：阵营、玩家类型（AI或玩家）、局内时间、分段、英雄、职业、状态（血量蓝量当前比例）、地图位置
-- 	指标：回城的平均次数/总次数、回城成功次数
-- 	展示方式：报表
drop table if exists shuguang.shuguang_dwd_game_a_tp_d_delta;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dwd_game_a_tp_d_delta
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
    pool_id             STRING,
    elo                 INT,
    battle_start_time   TIMESTAMP,
    ai_type	            INT,
    ai_base	            INT,
    deepai_num	        INT,
    hp_percent          INT,
    mana_percent         INT,
    tp_begintime        BIGINT,
    tp_endtime        BIGINT,

    tp_begin_battle_time_id       BIGINT COMMENT 'tp开始时的对局时间id，可能会有一帧的误差，只做时间筛选使用',
    tp_begin_frame_no    BIGINT,
    tp_begin_second_col      INT,
    tp_begin_minute_col      INT,
    tp_begin_stage       STRING,
    tp_begin_dragon_refresh    STRING,
    tp_begin_base_up    STRING,

    tp_donecount        BIGINT COMMENT 'tp应该持续的时间。通过begintime和endtime相减得到。单位为帧',
--     度量
    tp_real_donecount   BIGINT COMMENT 'tp实际持续的时间。帧数之和，可能会有一帧的误差，目前不用做计算口径来使用',
    tp_action           INT COMMENT '触发tp次数，无事实度量，常量1'
)
    COMMENT '描述每次tp操作的累加事实，粒度为每次tp操作。主要维度有阵营、玩家类型（AI或玩家）、局内时间、英雄、职业、状态（血量蓝量当前比例）、地图位置，主要度量有tp触发次数等'
    PARTITIONED BY (game_server_version STRING, game_big_version_short STRING, game_key_version STRING, year STRING, month STRING, day STRING, part STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
--     STORED AS TEXTFILE;
    STORED AS PARQUET TBLPROPERTIES('parquet.compression'='SNAPPY');