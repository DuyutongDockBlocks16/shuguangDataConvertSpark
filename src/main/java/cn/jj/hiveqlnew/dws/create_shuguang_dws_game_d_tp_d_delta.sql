-- TODO 暂时没用
-- 3、统计AI的回城（使用回城技能，并非走回泉水区）行为。
-- 	目的：评价AI对于回城技能使用的合理性
-- 	维度：阵营、玩家类型（AI或玩家）、局内时间、分段、英雄、职业、状态（血量蓝量当前比例）、地图位置
-- 	指标：回城的平均次数/总次数、回城成功次数
-- 	展示方式：报表
drop table if exists shuguang.shuguang_dws_game_d_tp_d_delta;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dws_game_d_tp_d_delta
(
--     维度
    map_area_id                INT,
--     地图退化维度
    bs_area             STRING COMMENT '地图区域退化维度',
--     ----
    camp_id				INT,
    id               	BIGINT,
    elo 				INT,
    mode				INT,
    hero_id              INT,
    race_id              INT,
    hp_section          STRING,
    mana_section          STRING,
    tp_count            INT COMMENT 'tp数量',
    tp_success_count    INT COMMENT 'tp成功数量'
)
    COMMENT '描述每局tp次数和tp成功次数。数据源为shuguang_dws_game_battle_tp_d_delta。主体为所有维度，粒度为每天、每个分段、每个模式、每个英雄、每个职业、每个地图区域，对所有对局的tp及成功tp做聚合。可以按照阵营、玩家类型、英雄、职业、业务区域分类、hp/mana区间进行筛选'
    PARTITIONED BY (game_server_version STRING, game_big_version_short STRING, game_key_version STRING, year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;