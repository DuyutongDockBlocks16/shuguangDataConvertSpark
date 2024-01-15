-- 命名规范：小驼峰，关键字后面+"_col"
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dwd_game_buyEquipment_d
(
    battle_id               BIGINT COMMENT '对局id',
    game_type               INT COMMENT '游戏类型',
    mode_tid                BIGINT COMMENT '模式TID',
    match_id                BIGINT COMMENT '比赛id',
    battle_tid              BIGINT COMMENT '对局配置id',
    match_elo               INT COMMENT '对局平均elo分',
    battle_start_time       TIMESTAMP COMMENT '对局开始时间',

    battle_time             TIMESTAMP COMMENT '局内时间（可以没有，以帧号做关联）',
    frame_no                INT COMMENT '帧数',
    role_id                  BIGINT COMMENT '玩家id',
    hero_id                  BIGINT COMMENT '英雄id',
    hero_type               INT COMMENT '英雄类型（退化维度）',
    buy_equipment           INT COMMENT '装备id',
    equipment_type          INT COMMENT '装备类型',
    cost_equipment          INT COMMENT '消耗装备id',
    cost_gold               INT COMMENT '消耗金币',
    equipments              ARRAY<STRING> COMMENT '当前装备list'
)
    COMMENT '玩家获取经济日志'
    PARTITIONED BY (year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;

select
    be.battletid,
    b.game_type,
    b.mode_tid,
    b.match_id,
    b.battle_tid,
    b.match_elo,
    b.battle_start_time,
    be.battletime,
    be.frameno,
    be.roleid,
    null, -- hero_id 暂时没有关联
    null, -- hero_type 维度表没有提供
    be.buy_equipment,
    null, -- equipment_type 维度表没有提供
    be.cost_equipment,
    be.cost_gold,
    split(be.equipments, ",")
from
    shuguang_ods_game_buyequipment_d be inner join shuguang_dwd_game_battle_d b on be.battletid=b.battle_id;