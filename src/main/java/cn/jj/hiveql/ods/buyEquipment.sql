-- 命名规范：小驼峰，关键字后面+"_col"
CREATE TABLE IF NOT EXISTS shuguang.shuguang_ods_game_buyEquipment_d
(
    battletId               BIGINT COMMENT '对局id',
    battletime              INT COMMENT '对局时间',
    frameNo                 INT COMMENT '帧数',
    roleId                  BIGINT COMMENT '玩家id',
    buy_equipment           INT COMMENT '装备id',
    cost_equipment          INT COMMENT '消耗装备id',
    cost_gold               INT COMMENT '消耗金币',
    equipments              STRING COMMENT '装备list'
)
    COMMENT '玩家获取经济日志'
    PARTITIONED BY (year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;
LOAD DATA INPATH '/staging/shuguang/rec_parse/${YEAR}/${MONTH}/${DAY}/ods_buyEquipment'
    OVERWRITE INTO TABLE shuguang.shuguang_ods_game_buyEquipment_d
    PARTITION(year='${YEAR}',month='${MONTH}',day='${DAY}');