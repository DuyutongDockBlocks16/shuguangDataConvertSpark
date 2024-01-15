-- dwd层数据由24GB减少到837MB，未压缩
-- 数据量8543727
-- tower和hero的对局交集1376
drop table if exists shuguang.shuguang_dwd_game_t_tower_state_d_delta;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dwd_game_t_tower_state_d_delta
(
-- 主键（自然键）
    battle_id        	BIGINT COMMENT '对局id',
--     date             	STRING COMMENT '对局日期',
    complete           	BOOLEAN COMMENT '是否全量帧',
-- 维度
    battle_time_id         	BIGINT COMMENT '局内时间id。局内时间维度外键',
    Id                 	BIGINT COMMENT '局内唯一的ID (必填)',
    camp_id				INT COMMENT '阵营id。阵营维度外键',
    type                INT COMMENT '塔的类型（业务补充有哪些种类）。塔维度退化维度',
    pos_x                INT COMMENT '位置x',
    pos_y                INT COMMENT '位置y',
    map_area_id         BIGINT COMMENT '地图区域id。地图区域维度外键',
    tower_id           INT COMMENT '塔的ID。塔维度外键',
-- 度量
    hp                  INT COMMENT '血量',
    hp_max               INT COMMENT '最大血量',
    atk                 INT COMMENT '攻击力',
    def                 INT COMMENT '防御能力',
    is_visiable          INT COMMENT '是否可见',
    target_id            INT COMMENT '攻击目标。和hero的id字段相匹配',
    atk_range            INT COMMENT '攻击范围'
)
    COMMENT '描述每帧的英雄状态，粒度为每一帧。主要维度有局内时间、玩家类型、阵营、地图区域、英雄等，主要度量有朝向、血量蓝量占比、金钱、经验、无事实度量在此位置出现等。'
    PARTITIONED BY (game_server_version STRING, game_big_version_short STRING, game_key_version STRING, year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
    STORED AS PARQUET TBLPROPERTIES('parquet.compression'='SNAPPY');
--     STORED AS TEXTFILE ;