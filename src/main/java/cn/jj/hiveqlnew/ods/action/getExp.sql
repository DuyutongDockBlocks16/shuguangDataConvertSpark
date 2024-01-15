-- 命名规范：小驼峰，关键字后面+"_col"
CREATE TABLE IF NOT EXISTS shuguang.shuguang_ods_game_getExp_d
(
    battletId               BIGINT COMMENT '对局id',
    battletime              INT COMMENT '对局时间',
    frameNo                 INT COMMENT '帧数',
    roleId                  BIGINT COMMENT '玩家id',
    oldExp                  INT COMMENT '原经验',
    exp                     INT COMMENT '获得经验',
    new_exp                 INT COMMENT '新经验',
    type_col                INT COMMENT '经验来源（1.自成长、2.小兵、3.野怪、4.防御塔、5.击杀助攻、6.装备）',
    level_col               INT COMMENT '当前等级',
    is_levelup              INT COMMENT '是否升级（0.否、1.是）'
)
    COMMENT '玩家获取经验日志'
    PARTITIONED BY (year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;
LOAD DATA INPATH '/staging/shuguang/rec_parse/${YEAR}/${MONTH}/${DAY}/ods_getExp'
    OVERWRITE INTO TABLE shuguang.shuguang_ods_game_getExp_d
    PARTITION(year='${YEAR}',month='${MONTH}',day='${DAY}');