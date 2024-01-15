drop table if exists shuguang.shuguang_dws_game_hero_hosting_delta_direct;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dws_game_hero_hosting_delta_direct
(
--     维度
    hero_id              INT,
    section_hosting_type      INT COMMENT '区间内托管类型，0 为 未发生托管，1 为 deepai_host，2 为 strategyai_host，3 为 deep_ai和strategyai联合托管',
    battle_time_section      INT COMMENT '局内时间区间，与业务方提前协商好',
    mode_tid                INT,
    pool_id             STRING,
    elo                 INT,
    ai_type	            INT,
    ai_base	            INT,
    deepai_num	        INT,
    user_num            INT,
    server          STRING,
--     度量
    section_battle_sum        BIGINT COMMENT '区间内出场次数，同一场对局，同一个英雄，在不同阵容分开统计',
    section_kill_sum           INT COMMENT '区间内击杀总数',
    section_assist_sum           INT COMMENT '区间内助攻总数',
    section_dead_sum           INT COMMENT '区间内死亡总数'
)
    COMMENT '描述每个英雄，每种托管行为，在每个局内时间区间的平均KDA。'
    PARTITIONED BY (game_server_version STRING, game_big_version_short STRING, game_key_version STRING, year STRING, month STRING, day STRING, part STRING)
    ROW FORMAT DELIMITED
    FIELDS TERMINATED BY '\001'
    COLLECTION ITEMS TERMINATED BY '\002'
    MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;
--     STORED AS PARQUET TBLPROPERTIES('parquet.compression'='SNAPPY');