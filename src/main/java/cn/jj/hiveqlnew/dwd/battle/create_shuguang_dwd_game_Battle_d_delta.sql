drop table if exists shuguang.shuguang_dwd_game_Battle_d_delta;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dwd_game_Battle_d_delta
(
--     主键（自然键）
    battle_id               BIGINT COMMENT '对局id',
--     维度
    game_type               INT COMMENT '游戏类型',
    mode_tid                BIGINT COMMENT '模式TID',
    battle_tid              BIGINT COMMENT '对局配置id',
    match_elo               INT COMMENT '对局平均elo分',
--     pool_id                 STRING COMMENT '匹配池编号',
--     protect_pool_id         BIGINT COMMENT '保护匹配池',
--     origin_pool_id          BIGINT COMMENT '原始匹配池编号',
--     start_col               TIMESTAMP COMMENT '开始匹配时间',
--     duration                INT COMMENT '匹配持续时间',
    battle_start_time       TIMESTAMP COMMENT '对局开始时间',
    play_time               INT COMMENT '游戏时长',
    players_num             INT COMMENT '对局人数',
    user_num                INT COMMENT '真玩家人数',
    fake_user_num           INT COMMENT '填充电脑人数',
    robot_num               INT COMMENT '电脑人数',
    gather_report_sec       FLOAT COMMENT '结算消耗时长（从收到第一个玩家结算数据开始计算到出结算数据发送前端截止）',
    afk_player_num          INT COMMENT '挂机人数',
    runaway_player_num      INT COMMENT '逃跑人数（逃跑的不算挂机）',
--     度量
    battle_num              INT COMMENT '战斗局数。无事实度量，常量1'
)
    COMMENT '对局信息'
    partitioned by (year string, month string, day string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    stored as TEXTFILE ;