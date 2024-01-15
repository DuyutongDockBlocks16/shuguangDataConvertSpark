-- 命名规范：小驼峰，关键字后面+"_col"
CREATE TABLE IF NOT EXISTS shuguang.shuguang_ods_game_stateMonster_d
(
    battleId        	BIGINT COMMENT '对局id',
    frameNo         	BIGINT COMMENT '帧号',
--     date             	STRING COMMENT '对局日期',
    complete           	BOOLEAN COMMENT '是否全量帧',
    Id                 	BIGINT COMMENT '对局内的唯一ID,用于区分不同玩家或者AI等（必填）',
    campId              INT COMMENT '阵营',
    type                INT COMMENT '类型 （业务补充野怪类型）',
    posX                INT COMMENT '位置x',
    posY                INT COMMENT '位置y',
    hp                  INT COMMENT '血量',
    hpMax               INT COMMENT '最大血量',
    atk                 INT COMMENT '攻击力',
    def                 INT COMMENT '防御能力',
    isVisiable          INT COMMENT '是否可见',
    targetId            INT COMMENT '攻击目标',
    atkRange            INT COMMENT '攻击范围',
    monsterId           INT COMMENT '怪物ID'
)
    COMMENT '野怪的基础属性'
    PARTITIONED BY (year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;

LOAD DATA INPATH '/staging/shuguang/rec_parse/${YEAR}/${MONTH}/${DAY}/ods_stateMonster'
    OVERWRITE INTO TABLE shuguang.shuguang_ods_game_stateMonster_d
    PARTITION(year='${YEAR}',month='${MONTH}',day='${DAY}');