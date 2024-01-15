CREATE TABLE IF NOT EXISTS shuguang.shuguang_dwd_game_killHero_d
(
    battle_id               BIGINT COMMENT '对局id',
    game_type               INT COMMENT '游戏类型',
    mode_tid                BIGINT COMMENT '模式TID',
    match_id                BIGINT COMMENT '比赛id',
    battle_tid              BIGINT COMMENT '对局配置id',
    match_elo               INT COMMENT '对局平均elo分',
    battle_start_time       TIMESTAMP COMMENT '对局开始时间',

    battle_time             TIMESTAMP COMMENT '局内时间（可以没有，以帧号做关联）',
    frame_no                INT COMMENT '帧号',
    rel_id                  INT COMMENT '关联id（帧号+死亡角色id）',
    is_last_hit             BOOLEAN COMMENT '是否造成击杀',
    kill_take_time          INT COMMENT '击杀耗时',
    kill_role_id                 BIGINT COMMENT '击杀玩家id',
    kill_hero_id                 BIGINT COMMENT '击杀英雄id（和PlayerBattle关联）',
    kill_position_x              INT COMMENT '击杀坐标x',
    kill_position_y              INT COMMENT '击杀坐标y',
    kill_gold                    INT COMMENT '源金钱变化',
    kill_exp                     INT COMMENT '源经验变化',
    die_role_id          BIGINT COMMENT '死亡玩家id',
    die_hero_id          BIGINT COMMENT '死亡英雄id',
    die_position_x       INT COMMENT '死亡坐标x',
    die_position_y       INT COMMENT '死亡坐标y',
    die_gold             INT COMMENT '死亡金钱变化',
    die_exp              INT COMMENT '死亡经验变化',
    assist_role_ids         ARRAY<STRING> COMMENT '助攻者玩家idlist',
    assist_num              INT COMMENT '参与击杀的助攻数',
    partake_num             INT COMMENT '参团人数',
    opt_type                INT COMMENT '操作类型',
    opt_type_sub            INT COMMENT '操作子类型',
    opt_value               INT COMMENT '操作产生的具体数值'

)
    COMMENT '英雄击杀日志'
    PARTITIONED BY (year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;


select
    kh.battleid,
    kh.battletid,
    kh.battletime,
    kh.frameno,
    concat(kh.frameno,"_",kh.bekillroleid),
    true,
    null,
    kh.killroleid,
    null,
    sh.locationx,
    sh.locationy,
    null,
    null,
    kh.bekillroleid,
    null,
    kh.positionx,
    kh.positiony,
    null,
    null,
    split(kh.assistsroleids, ","),
    size(split(kh.assistsroleids, ",")),
    kh.partakenum,
    null,
    null,
    null
from
    shuguang_ods_game_killhero_d kh inner join shuguang_ods_game_statehero_d sh on kh.battleid=sh.battleid and kh.frameno=sh.frameno and kh.killroleid=sh.role_id -- ods层的stateHero表暂时没有role_id，可以用dwd层的stateHero表