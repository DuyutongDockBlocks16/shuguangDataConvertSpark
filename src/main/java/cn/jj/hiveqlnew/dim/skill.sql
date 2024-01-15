CREATE TABLE IF NOT EXISTS shuguang.shuguang_dim_game_skill_d
(
    skill_id BIGINT COMMENT '技能ID',
    level_col INT COMMENT '等级',
    cd_max INT COMMENT '最大冷却时间',
    atk_range ARRAY<STRUCT<cast_range:STRUCT<value_col:INT,next_value:INT,start_time:INT,duration:INT>>> COMMENT '攻击范围',
    hp_cost INT COMMENT '消耗血量',
    mp_cost INT COMMENT '蓝量消耗',
    xp_cost INT COMMENT '能量消耗',
    segment INT COMMENT '技能段数'
)