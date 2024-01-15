drop table if exists shuguang.shuguang_dws_game_section_hosting_delta_direct;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dws_game_section_hosting_delta_direct
(
--     主键
    battle_id        	BIGINT COMMENT '对局id',
--     维度
    mode_tid                INT,
    role_id              BIGINT,
    hero_id              INT,
    race_id              INT,
    pool_id             STRING,
    elo                 INT,
    ai_type	            INT,
    ai_base	            INT,
    deepai_num	        INT,
    user_num            INT,
    server          STRING,
    battle_time_section      INT COMMENT '局内时间区间，与业务方提前协商好',
    hosting_type      INT COMMENT '托管类型，0 为 未发生托管，1 为 deepai_host，2 为 strategyai_host，3 为 deep_ai和strategyai联合托管',
    hosting_minute      DOUBLE COMMENT '托管分钟数',
    deepai_host_secs	string	comment '玩家被超参数ai托管的时间集合（数据示例：{"370.03":100.15,"615.36":47.51}，表示第一次托管从370.03秒开始，持续100.15s）',
    strategyai_host_secs	string	comment '玩家被行为树ai托管的时间集合（数据格式与deepai_host_secs一致）',
--     度量
    section_kill           INT COMMENT '区间内击杀',
    section_assist           INT COMMENT '区间内助攻',
    section_dead           INT COMMENT '区间内死亡'
)
    COMMENT '描述每场对局，每个role_id，在每个局内时间区间的kda，以及在这个区间的托管信息'
    PARTITIONED BY (game_server_version STRING, game_big_version_short STRING, game_key_version STRING, year STRING, month STRING, day STRING, part STRING)
    ROW FORMAT DELIMITED
    FIELDS TERMINATED BY '\001'
    COLLECTION ITEMS TERMINATED BY '\002'
    MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;
--     STORED AS PARQUET TBLPROPERTIES('parquet.compression'='SNAPPY');