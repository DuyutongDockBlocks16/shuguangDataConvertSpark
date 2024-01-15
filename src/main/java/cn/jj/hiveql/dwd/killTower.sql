CREATE TABLE IF NOT EXISTS shuguang.shuguang_dwd_game_killTower_d
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
    tower_iid               BIGINT COMMENT '防御塔iid',
    tower_tid               BIGINT COMMENT '防御塔tid',
    camp_id                 INT COMMENT '阵营id',
    tower_level             INT COMMENT '防御塔等级',
    tower_position_x        INT COMMENT '防御塔坐标x',
    tower_position_y        INT COMMENT '防御塔坐标y',
    opt_type                INT COMMENT '操作类型',
    opt_type_sub            INT COMMENT '操作子类型',
    opt_value               INT COMMENT '操作产生的具体数值'
)
    COMMENT '防御塔击杀日志'
    PARTITIONED BY (year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;

select
    kt.battleid,
    b.game_type,
    b.mode_tid,
    b.match_id,
    kt.battletid,
    b.match_elo,
    b.battle_start_time,
    kt.battletime,
    null,
    null,
    false,
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    kt.toweriid,
    kt.towertid,
    st.campid,
    kt.towerlevel,
    st.posx,
    st.posy,
    null,
    null,
    null
from
    shuguang_ods_game_killtower_d kt inner join shuguang_dwd_game_battle_d b on kt.battleid=b.battle_id
                                     inner join shuguang_ods_game_statetower_d st on kt.battleid=st.battleid and kt.frameno=st.frameno and kt.toweriid=st.towerid;
