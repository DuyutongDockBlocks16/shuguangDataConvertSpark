CREATE TABLE IF NOT EXISTS shuguang.shuguang_dwd_game_killMonster_d
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
    role_id                 BIGINT COMMENT '源玩家id（和PlayerBattle关联）',
    hero_id                 BIGINT COMMENT '源英雄id',
    position_x              INT COMMENT '源坐标x',
    position_y              INT COMMENT '源坐标y',
    gold                    INT COMMENT '源金钱变化',
    exp                     INT COMMENT '源经验变化',
    monster_iid             BIGINT COMMENT '野怪iid',
    monster_tid             BIGINT COMMENT '野怪tid',
    camp_id                 INT COMMENT '阵营id',
    monster_level           INT COMMENT '野怪等级',
    monster_type            INT COMMENT '野怪类型',
    monster_position_x      INT COMMENT '野怪坐标x',
    monster_position_y      INT COMMENT '野怪坐标y',
    opt_type                INT COMMENT '操作类型',
    opt_type_sub            INT COMMENT '操作子类型',
    opt_value               INT COMMENT '操作产生的具体数值'
)
    COMMENT '野怪击杀日志'
    PARTITIONED BY (year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;

select
    km.battleid,
    b.game_type,
    b.mode_tid,
    b.match_id,
    km.battletid,
    b.match_elo,
    b.battle_start_time,
    km.battletime,
    null, -- frame 缺少字段
    null,
    true,
    km.`time`,
    null,
    null,
    null,
    null,
    null,
    null,
    km.monsteriid,
    km.monstertid,
    sm.campid,
    km.monsterlevel,
    sm.type,
    sm.posx,
    sm.posy,
    null,
    null,
    null
from
    shuguang_ods_game_killmonster_d km inner join shuguang_dwd_game_battle_d b on km.battleid=b.battle_id
                                       inner join shuguang_ods_game_statemonster_d sm on km.battleid=sm.battleid and km.frameno=sm.frameno and km.monsteriid=sm.monsterid;