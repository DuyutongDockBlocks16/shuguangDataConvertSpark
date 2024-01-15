CREATE TABLE IF NOT EXISTS shuguang.shuguang_dwd_game_Battle_d
(
    battle_id               BIGINT COMMENT '对局id',
    game_type               INT COMMENT '游戏类型',
    mode_tid                BIGINT COMMENT '模式TID',
    match_id                BIGINT COMMENT '赛事id',
    stage_id                BIGINT COMMENT '赛事阶段id',
    battle_tid              BIGINT COMMENT '对局配置id',
    match_elo               INT COMMENT '对局平均elo分',
    pool_id                 STRING COMMENT '匹配池编号',
    protect_pool_id         BIGINT COMMENT '保护匹配池',
    origin_pool_id          BIGINT COMMENT '原始匹配池编号',
    start_col               TIMESTAMP COMMENT '开始匹配时间',
    duration                INT COMMENT '匹配持续时间',
    battle_start_time       TIMESTAMP COMMENT '对局开始时间',
    play_time               INT COMMENT '游戏时长',
    players_num             INT COMMENT '对局人数',
    user_num                INT COMMENT '真玩家人数',
    fake_user_num           INT COMMENT '填充电脑人数',
    robot_num               INT COMMENT '电脑人数',
    gather_report_sec       FLOAT COMMENT '结算消耗时长（从收到第一个玩家结算数据开始计算到出结算数据发送前端截止）',
    afk_player_num          INT COMMENT '挂机人数',
    runaway_player_num      INT COMMENT '逃跑人数（逃跑的不算挂机）',
    server                  STRING COMMENT '服务器名',
    key_version             INT COMMENT '版本md5'
)
    COMMENT '对局信息'
    partitioned by (year string, month string, day string)
    stored as TEXTFILE ;

select distinct
    b.battleid,
    b.gametype,
    b.modetid,
    ti.match_id,
    ti.stage_id,
    b.battletid,
    me.avg_match_elo,
    me.pool_id,
    me.protect_pool_id,
    me.origin_pool_id,
    me.start_col,
    me.duration,
    b.begintime,
    b.playtime,
    b.playersnum,
    b.usernum,
    b.fakeusernum,
    b.robotnum,
    b.gatherreportsec,
    b.illegalplayer_1,
    b.illegalplayer_3,
    me.server,
    me.keyversion
from
    shuguang_ods_game_battle_d b inner join (
        select
            mee.matching_id,
            mee.pool_id,
            mee.protect_pool_id,
            mee.origin_pool_id,
            mee.start_col,
            mee.duration,
            mee.server,
            mee.keyversion,
            avg(DISTINCT mee.elo) as avg_match_elo
        from
            shuguang_ods_game_matchingensure_d mee
        where
                mee.success=1
          and mee.ensure=1
        group by mee.matching_id, mee.pool_id, mee.protect_pool_id,mee.origin_pool_id,mee.start_col,mee.duration,mee.server,mee.keyversion
    ) me on b.battleid = me.matching_id
                                 inner join shuguang_ods_game_tournamentintegral_d ti on ti.battleid = b.battleid;
