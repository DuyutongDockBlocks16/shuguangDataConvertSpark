-- 2、统计AI受防御塔进攻的次数和承受的伤害；
-- 	目的：评价AI对于受到防御塔攻击的反应
-- 	维度：阵营、防御塔类型、玩家类型（AI或玩家）、局内时间、分段、模式、英雄、职业、状态（血量蓝量当前比例）
-- 	指标：受防御塔瞄准的平均次数/总次数，被塔连续瞄准的平均次数/总次数
-- 	展示方式：报表
-- 	问题：目前数据支撑不了伤害的统计；
drop table if exists shuguang.shuguang_dws_game_second_tower_aim_d_delta;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dws_game_second_tower_aim_d_delta
(
    -- 主键（自然键）
    battle_id        	BIGINT COMMENT '对局id',
--     date             	STRING COMMENT '对局日期',
    complete           	BOOLEAN COMMENT '是否全量帧',
-- 维度
    --     局内时间退化维度
    second_col      INT COMMENT '局内秒数',
    minute_col      INT COMMENT '局内分钟数',
--     局内时间退化维度--end
    id               	BIGINT,
    player_type          INT,
    camp_id				INT,
    --     tower
    tower_inside_id     BIGINT,
    tower_camp_id       INT,
    type                INT,
    pos_x                INT,
    pos_y                INT,
    map_area_id         BIGINT,
    tower_id           INT,
--     tower end
    elo 				INT,
    mode				INT,
    hero_id              INT,
    race_id              INT,
    hp_section          STRING,
    mana_section          STRING,
    aim_count_sum           INT COMMENT '瞄准次数。'
)
    COMMENT '描述每局每个英雄每秒被塔瞄准的次数和。数据源为shuguang_dws_game_frame_tower_aim_d_delta。主体为英雄，粒度为每局、每秒、每个分段、每个模式、每个英雄、每个职业。可以按照阵营、玩家类型、英雄、职业、防御塔类型、hp/mana区间进行筛选'
    PARTITIONED BY (game_server_version STRING, game_big_version_short STRING, game_key_version STRING, year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
--     STORED AS TEXTFILE;
    STORED AS PARQUET TBLPROPERTIES('parquet.compression'='SNAPPY');