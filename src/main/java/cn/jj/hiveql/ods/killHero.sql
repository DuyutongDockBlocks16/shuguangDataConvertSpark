CREATE TABLE IF NOT EXISTS shuguang.shuguang_ods_game_killHero_d
(
    battleId                BIGINT COMMENT '对局id',
    battleTId               BIGINT COMMENT '对局配置id',
    battletime              INT COMMENT '对局时间',
    frameNo                 INT COMMENT '帧号',
    bekillRoleId            BIGINT COMMENT '被击杀玩家id',
    killRoleId              BIGINT COMMENT '击杀玩家id',
    assistsRoleIds          STRING COMMENT '助攻者玩家idlist',
    partakeNum              INT COMMENT '参团人数',
    positionX               INT COMMENT '击杀位置坐标x',
    positionY               INT COMMENT '击杀位置坐标y'
)
    COMMENT '英雄击杀日志'
    PARTITIONED BY (year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;
LOAD DATA INPATH '/staging/shuguang/rec_parse/${YEAR}/${MONTH}/${DAY}/ods_killHero'
    OVERWRITE INTO TABLE shuguang.shuguang_ods_game_killHero_d
    PARTITION(year='${YEAR}',month='${MONTH}',day='${DAY}');