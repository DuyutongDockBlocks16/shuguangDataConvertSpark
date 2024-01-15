-- 命名规范：小驼峰，关键字后面+"_col"
CREATE TABLE IF NOT EXISTS shuguang.shuguang_ods_game_killMonster_d
(
    battleId                BIGINT COMMENT '对局id',
    battleTId               BIGINT COMMENT '对局配置id',
    battletime              INT COMMENT '对局时间',
    monsterIid              BIGINT COMMENT '野怪iid',
    monsterTid              BIGINT COMMENT '野怪tid',
    monsterLevel            INT COMMENT '野怪等级',
    time                    INT COMMENT '耗时',
    limitHp                 INT COMMENT '最低血量值（击杀记录为0，攻击记录为记录的最低血量值）',
    type                    INT COMMENT '类型（从100%血开始统计，记录开始时间。记录最低血量值，不断刷新。当血量重置满时，重置最低血量值记录重置时间，并记录一次攻击。当血量为0时，记录一次击杀，重置最低血量值记录。1.击杀、2.攻击）'
)
    COMMENT '野怪击杀日志'
    PARTITIONED BY (year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;
LOAD DATA INPATH '/staging/shuguang/rec_parse/${YEAR}/${MONTH}/${DAY}/ods_killMonster'
    OVERWRITE INTO TABLE shuguang.shuguang_ods_game_killMonster_d
    PARTITION(year='${YEAR}',month='${MONTH}',day='${DAY}');