-- 表名规范：数据库名_dwd_数据域_t(事务)|p(周期)|a(累加)_主体_业务过程_刷新周期标识(d|hh|mm)_[增量标识(delta)]
-- 命名规范：小驼峰，关键字后面+"_col"
-- 需要注意几个问题：属性名字的规范性一致性，数值单位的一致性，属性完备的业务理解，维度和度量的区分，退化维度的标记，无事实度量的标记，衍生指标的标记，非空字段的标记，udf的标记，表的注释（说明业务过程的意义，主要维度和主要度量）
-- 数据导入dws，从128GB减少到43GB，未压缩
-- 数据量127447457
-- 对局数1382
drop table if exists shuguang.shuguang_dwd_game_t_hero_state_d_delta;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_dwd_game_t_hero_state_d_delta
(
-- 主键（自然键）
    battle_id        	BIGINT COMMENT '对局id',
--     TODO 可以去掉，暂时没用，ods层保留。如果以后需要可以重新加进来
    complete           	BOOLEAN COMMENT '是否全量帧',
-- 维度
    battle_time_id         	BIGINT COMMENT '局内时间id。局内时间维度外键',
    id               	BIGINT COMMENT '对局内的唯一ID,用于区分不同玩家或者AI等（必填）。玩家类型退化维度',
    account             BIGINT COMMENT '账号信息。账号维度外键',
    camp_id				INT COMMENT '阵营id。阵营维度外键',
    location_x			INT COMMENT '位置x坐标',
    location_y			INT COMMENT '位置y坐标',
--     TODO 通过udf: getArea(location_x, location_y)计算。
    map_area_id                BIGINT COMMENT '地图区域id。地图区域维度外键',
    cur_skill_id          ARRAY<INT> COMMENT '当前正在释放的技能ID。技能维度外键',
    ex_state             ARRAY<INT> COMMENT '异常状态（处于眩晕、减速、击飞，无敌、免控等）。异常状态外键。既是维度，也是度量',
    equip_ids           ARRAY<INT> COMMENT '当前的英雄的装备列表。装备维度外键',
--     equip_id_recommend    ARRAY<INT> COMMENT '业务提供的装备列表。装备维度外键',
    hero_id              INT COMMENT '英雄ID(必填)。英雄维度外键',
    race_id              INT COMMENT '职业ID(必填)',
    playerLevel         INT COMMENT '等级(必填)',
    ai_type	            INT COMMENT 'AI类型：1超参数AI（from playerbattle）',
    ai_base	            INT COMMENT 'AI强度（from playerbattle）',
    deepai_num	        INT COMMENT '超参数AI数量（from playerbattle）',
-- 度量
    direction_x			INT COMMENT '朝向x坐标',
    direction_y			INT COMMENT '朝向y坐标',
--     TODO 通过udf: getAngle(direction_x, direction_y)计算。
    direction_angle		INT COMMENT '朝向角度。衍生指标，',
    hp                  INT COMMENT '血量',
    hp_max               INT COMMENT '最大血量',
    hp_percent          INT COMMENT '血量占比。百分数，保留两位小数之后*100。衍生指标',
    hp_section          STRING COMMENT '血量区间',
    energy              INT COMMENT '能量',
    energy_max           INT COMMENT '能量最大值',
    energy_percent         INT COMMENT '能量占比。百分数，保留两位小数之后*100。衍生指标',
    mana                INT COMMENT '蓝量',
    mana_max             INT COMMENT '蓝量最大值',
    mana_percent         INT COMMENT '蓝量占比。百分数，保留两位小数之后*100。衍生指标',
    mana_section        STRING COMMENT '蓝量区间',
    anger               INT COMMENT '怒气值',
    anger_max            INT COMMENT '怒气最大值',
    anger_percent         INT COMMENT '怒气占比。百分数，保留两位小数之后*100。衍生指标',
    level_col           INT COMMENT '等级',
    exp                 INT COMMENT '经验',
    cur_gold             INT COMMENT '当前金钱',
    gold                INT COMMENT '累积金钱',
    phy_atk              INT COMMENT '物理攻击',
    phy_def              INT COMMENT '物理防御',
    mgc_atk              INT COMMENT '魔法攻击',
    mgc_def              INT COMMENT '魔法防御',
    toughness           INT COMMENT '韧性',
    cdr                 INT COMMENT '冷却缩减',
    respawn             INT COMMENT '复活时间',
    is_visiable          BOOLEAN COMMENT '是否能被敌人可见_1可见0不可见',
    move_speed           INT COMMENT '移动速度',
    kill                INT COMMENT '击杀数（event也可以统计指标，统一用state的指标）',
    dead                INT COMMENT '死亡数（event也可以统计指标，统一用state的指标）',
    assist              INT COMMENT '助攻数（event也可以统计指标，统一用state的指标）',
    hp_recover           INT COMMENT '生命恢复',
    energy_recover       INT COMMENT '魔法恢复',
    atk_speed            INT COMMENT '攻击速度',
    crt                 INT COMMENT '暴击概率',
    crt_times            INT COMMENT '暴击倍数',
    phy_through          INT COMMENT '物理护甲穿透',
    mag_through          INT COMMENT '魔法护甲穿透',
--     TODO 格式未知
    phy_through_percent   INT COMMENT '物理护甲穿透百分比',
--     TODO 格式未知
    mag_through_percent   INT COMMENT '魔法护甲穿透百分比',
    spell_sorb           INT COMMENT '魔法吸血',
    atk_sorb             INT COMMENT '物理吸血',
    shield              INT COMMENT '护盾最大值',
    shield_phy           INT COMMENT '物理护盾',
    shield_mgc           INT COMMENT '魔法护盾',
    shield_all           INT COMMENT '通用护盾',
    shield_phy_max        INT COMMENT '物理护盾最大值',
    shield_mgc_max        INT COMMENT '魔法护盾最大值',
    shield_all_max        INT COMMENT '通用护盾最大值',
    head_icon_lockOn_target_iid  BIGINT COMMENT '正在锁定目标中',
    chase_target_iid      INT COMMENT '追随目标Iid，与lockOnTargetIid不同，可以是友方',
    is_chase_move         BOOLEAN COMMENT '正在追击移动',
    is_auto_attacking     BOOLEAN COMMENT '是否在自动攻击中',
    lockOn_target_iid     INT COMMENT '锁定攻击目标，一定是敌人',
    is_dead              BOOLEAN COMMENT '是否死亡（event也可以统计指标，统一用state的指标）',
--     TODO 需要判定是否为整数秒
    tp_begintime        BIGINT COMMENT 'tp开始时间。局内秒数*10000。无tp为-1',
    tp_endtime        BIGINT COMMENT 'tp应该结束的时间，不是实际结束的时间。局内秒数*10000。无tp为-1',
    spring_portal_success BOOLEAN COMMENT '回城技能使用成功',
    appear              INT COMMENT '无事实度量，有数据就说明在此位置出现，常量1。过滤掉死亡',
-- 未知字段（TODO 不懂）
    delcurSkillID       ARRAY<INT>,
    delexState          ARRAY<INT>,
--     如果STRUCT或者STRUCT集合有字段的增删，使用 TODO udf: structTrans(struct/sturctARRAY, field...)
--     TODO 两个超大的ARRAY字段，先去掉了，对统计结果没有影响，而且会有大量的null值，导致数据量暴增
--     skill_state        ARRAY<STRUCT<
--         Id:BIGINT COMMENT '技能ID',
--         level_col:INT COMMENT '等级',
--         slot:INT COMMENT '类型(0:代表普攻, 其它数值:代表技能1~n)',
--         type_col:INT COMMENT '技能属于哪种类型',
--         usable:BOOLEAN COMMENT '能否使用',
--         cd:INT COMMENT '当前冷却时间',
--         cdMax:INT COMMENT '最大冷却时间',
--         atkRange:ARRAY<STRUCT<castRange:STRUCT<value_col:INT,nextValue:INT,startTime:INT,duration:INT>>> COMMENT '攻击范围',
--         hpCost:INT COMMENT '消耗血量',
--         mpCost:INT COMMENT '蓝量消耗',
--         xpCost:INT COMMENT '能量消耗',
--         segment:INT COMMENT '当前属于第几段',
--         progress:INT COMMENT '技能进展',
--         isOutofMana:INT COMMENT '是否没有蓝'
--     >> COMMENT '技能列表。FNC(Field Name Correct)：技能的英文统一修正为skill',
--     commands            ARRAY<STRUCT<
--         type_col:INT COMMENT '命令类型(必填)',
--         abilityID:INT COMMENT '技能ID',
--         targetPosX:INT COMMENT '目标移动或者移动施法的位置x',
--         targetPosY:INT COMMENT '目标移动或者移动施法的位置y',
--         targeID:BIGINT COMMENT '攻击的目标ID',
--         degreeX:INT COMMENT '施法角度x',
--         degreeY:INT COMMENT '施法角度y',
--         equiptId:INT COMMENT '装备ID',
--         angle:INT COMMENT 'move角度',
--         skillLevel:INT COMMENT '技能等级'
--     >> COMMENT '操作命令',
    b_server_AI           BOOLEAN COMMENT '是否托管英雄',
--     TODO 对局属性吧？不懂
    elo                 INT COMMENT 'elo值 （隐藏分）',
--     TODO 不懂
    score               INT COMMENT '评分（必填）',
    difficulty          INT COMMENT '难度 (以后再定，暂时可以不填)',
    player_type          INT COMMENT '0是真人，非0是AI',
--     TODO 暂时不用
    ai_type          INT,
--     TODO 未知字段，不懂
    delabilityState     ARRAY<BIGINT>
)
    COMMENT '描述每帧的英雄状态，粒度为每一帧。主要维度有局内时间、玩家类型、阵营、地图区域、英雄等，主要度量有朝向、血量蓝量占比、金钱、经验、无事实度量在此位置出现等。'
    PARTITIONED BY (game_server_version STRING, game_big_version_short STRING, game_key_version STRING, year STRING, month STRING, day STRING)
    ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '\001'
        COLLECTION ITEMS TERMINATED BY '\002'
        MAP KEYS TERMINATED BY '\003'
--     STORED AS TEXTFILE ;
    STORED AS PARQUET TBLPROPERTIES('parquet.compression'='SNAPPY');