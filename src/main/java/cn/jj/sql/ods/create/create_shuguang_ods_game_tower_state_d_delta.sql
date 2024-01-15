-- 命名规范：小驼峰，关键字后面+"_col"
-- 数据源24GB，未压缩
-- 数据量261503981
drop table if exists shuguang.shuguang_ods_game_tower_state_d_delta;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_ods_game_tower_state_d_delta
(
    battleId        	BIGINT COMMENT '对局id',
    frameNo         	BIGINT COMMENT '帧号',
--     date             	STRING COMMENT '对局日期',
    complete           	BOOLEAN COMMENT '是否全量帧',

    Id                 	BIGINT COMMENT '局内唯一的ID (必填)',
    campId              INT COMMENT '阵营',
    type                INT COMMENT '塔的类型（业务补充有哪些种类）',
    posX                INT COMMENT '位置x',
    posY                INT COMMENT '位置y',
    hp                  INT COMMENT '血量',
    hpMax               INT COMMENT '最大血量',
    atk                 INT COMMENT '攻击力',
    def                 INT COMMENT '防御能力',
    isVisiable          INT COMMENT '是否可见',
    targetId            INT COMMENT '攻击目标',
    atkRange            INT COMMENT '攻击范围',
    towerId           INT COMMENT '塔的ID'
)
    COMMENT '塔的基础属性'
    PARTITIONED BY (game_server_version STRING, game_big_version_short STRING, game_key_version STRING, year STRING, month STRING, day STRING, part string)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;
--     location '/staging/shuguang/rec_parse/ods/game/Tower_state/delta';