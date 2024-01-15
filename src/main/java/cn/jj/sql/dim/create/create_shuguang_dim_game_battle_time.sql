-- 表名规范：数据库名_dim_数据域_维度标识
drop table if exists shuguang.shuguang_dim_game_battle_time;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dim_game_battle_time
(
    id          BIGINT COMMENT '代理键id',
    frame_no    BIGINT COMMENT '帧号。1秒等于15帧',
    second_col      INT COMMENT '局内秒数',
    minute_col      INT COMMENT '局内分钟数',
    stage       STRING COMMENT '业务定义：局内阶段（前期、中期、后期、结束帧）',
    dragon_refresh    STRING COMMENT '业务定义：是否为刷龙时间点，刷龙之后的所有帧都为1',
    base_up    STRING COMMENT '业务定义：是否为基地起身时间点，基地起身之后的所有帧都为1',
    hosting_minute_section INT COMMENT '托管筛选局内时间区间'

)
    COMMENT '局内时间维表。帧号，秒数，分钟数的全量组合。包含业务自定义的局内时间点，可以新增属性。缓慢变化维type 2，不同版本保留历史。分区字段为版本号'
--     分区字段。每个版本重新生成一次分区数据（不管维度是否变化）。
    PARTITIONED BY (version STRING COMMENT '有效版本号')
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;