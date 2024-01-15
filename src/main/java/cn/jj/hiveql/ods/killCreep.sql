-- 命名规范：小驼峰，关键字后面+"_col"
CREATE TABLE IF NOT EXISTS shuguang.shuguang_ods_game_killCreep_d
(
    battleId                BIGINT COMMENT '对局id',
    battletId               BIGINT COMMENT '对局配置id',
    battletime              INT COMMENT '对局时间',
    soilderLine             INT COMMENT '分路（1.中路、2.赏金路、3.争锋路）',
    soilderIid              BIGINT COMMENT '小兵iid',
    soilderTid              BIGINT COMMENT '小兵tid',
    soilderNum              BIGINT COMMENT '小兵波数（字段修正：creep_round）',
    positionX               INT COMMENT '击杀位置坐标x',
    positionY               INT COMMENT '击杀位置坐标y'
)
    COMMENT '小兵击杀日志'
    PARTITIONED BY (year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;
LOAD DATA INPATH '/staging/shuguang/rec_parse/${YEAR}/${MONTH}/${DAY}/ods_killCreep'
    OVERWRITE INTO TABLE shuguang.shuguang_ods_game_killCreep_d
    PARTITION(year='${YEAR}',month='${MONTH}',day='${DAY}');