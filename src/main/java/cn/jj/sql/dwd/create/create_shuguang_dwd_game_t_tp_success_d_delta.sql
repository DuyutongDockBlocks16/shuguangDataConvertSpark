-- 3、统计AI的回城（使用回城技能，并非走回泉水区）行为。
-- 	目的：评价AI对于回城技能使用的合理性
-- 	维度：阵营、玩家类型（AI或玩家）、局内时间、分段、英雄、职业、状态（血量蓝量当前比例）、地图位置
-- 	指标：回城的平均次数/总次数、回城成功次数
-- 	展示方式：报表
drop table if exists shuguang.shuguang_dwd_game_t_tp_success_d_delta;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dwd_game_t_tp_success_d_delta
(
--     主键
    battle_id        	BIGINT COMMENT '对局id',
--     维度
    camp_id				INT,
    id               	BIGINT,
    player_type          INT,
    hero_id              INT,
    race_id              INT,
--     battle_time 退化维度
    battle_time_id      BIGINT,
    frame_no    BIGINT,
    second_col      INT,
    minute_col      INT,
    stage       STRING,
    dragon_refresh    STRING,
    base_up    STRING,
--     end
    tp_success          INT COMMENT 'tp成功次数。无事实度量，常量1'
)
    COMMENT '描述每次tp操作成功的事实，粒度为每次tp操作。主要维度有阵营、玩家类型（AI或玩家）、局内时间、英雄、职业，主要度量有tp成功次数'
    PARTITIONED BY (game_server_version STRING, game_big_version_short STRING, game_key_version STRING, year STRING, month STRING, day STRING, part STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
--     STORED AS TEXTFILE;
    STORED AS PARQUET TBLPROPERTIES('parquet.compression'='SNAPPY');