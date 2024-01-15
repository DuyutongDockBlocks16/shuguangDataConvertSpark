-- 命名规范：小驼峰，关键字后面+"_col"
CREATE TABLE IF NOT EXISTS shuguang.shuguang_ods_game_killTower_d
(
    battleId                BIGINT COMMENT '对局id',
    battleTId               BIGINT COMMENT '对局配置id',
    battletime              INT COMMENT '对局时间',
    towerIid                BIGINT COMMENT '防御塔iid',
    towerTid                BIGINT COMMENT '防御塔tid',
    towerLevel              INT COMMENT '防御塔等级'
)
    COMMENT '防御塔日志'
    PARTITIONED BY (year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;
LOAD DATA INPATH '/staging/shuguang/rec_parse/${YEAR}/${MONTH}/${DAY}/ods_killTower'
    OVERWRITE INTO TABLE shuguang.shuguang_ods_game_killTower_d
    PARTITION(year='${YEAR}',month='${MONTH}',day='${DAY}');