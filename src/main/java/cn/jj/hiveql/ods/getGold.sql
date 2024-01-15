-- 命名规范：小驼峰，关键字后面+"_col"
CREATE TABLE IF NOT EXISTS shuguang.shuguang_ods_game_getGold_d
(
    battletId               BIGINT COMMENT '对局id',
    battletime              INT COMMENT '对局时间',
    frameNo                 INT COMMENT '帧数',
    roleId                  BIGINT COMMENT '玩家id',
    oldGold                 INT COMMENT '原总金币',
    gold                    INT COMMENT '获得金币',
    new_gold                INT COMMENT '新总金币',
    type_col                INT COMMENT '金币来源（1.自成长、2.小兵、3.野怪、4.防御塔、5.击杀助攻、6.装备）'
)
    COMMENT '玩家获取经济日志'
    PARTITIONED BY (year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;
LOAD DATA INPATH '/staging/shuguang/rec_parse/${YEAR}/${MONTH}/${DAY}/ods_getGold'
    OVERWRITE INTO TABLE shuguang.shuguang_ods_game_getGold_d
    PARTITION(year='${YEAR}',month='${MONTH}',day='${DAY}');