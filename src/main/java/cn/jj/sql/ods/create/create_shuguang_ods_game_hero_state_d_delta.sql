drop table if exists shuguang.shuguang_ods_game_hero_state_d_delta;
CREATE TABLE IF NOT EXISTS shuguang.shuguang_ods_game_hero_state_d_delta
(
    battleId        	BIGINT COMMENT '对局id',
    frameNo         	BIGINT COMMENT '帧号',
--     date             	STRING COMMENT '对局日期',
    complete           	BOOLEAN COMMENT '是否全量帧',

    Id               	BIGINT COMMENT '对局内的唯一ID,用于区分不同玩家或者AI等（必填）',
    campId				INT COMMENT '阵营',
    locationX			INT COMMENT '位置x坐标',
    locationY			INT COMMENT '位置y坐标',
    directionX			INT COMMENT '朝向x坐标',
    directionY			INT COMMENT '朝向y坐标',
    hp                  INT COMMENT '血量',
    hpMax               INT COMMENT '最大血量',
    energy              INT COMMENT '能量',
    energyMax           INT COMMENT '能量最大值',
    mana                INT COMMENT '蓝量',
    manaMax             INT COMMENT '蓝量最大值',
    anger               INT COMMENT '怒气值',
    angerMax            INT COMMENT '怒气最大值',
    level_col           INT COMMENT '等级',
    exp                 INT COMMENT '经验',
    curGold             INT COMMENT '当前金钱',
    gold                INT COMMENT '累积金钱',
    phyAtk              INT COMMENT '物理攻击',
    phyDef              INT COMMENT '物理防御',
    mgcAtk              INT COMMENT '魔法攻击',
    mgcDef              INT COMMENT '魔法防御',
    toughness           INT COMMENT '韧性',
    cdr                 INT COMMENT '冷却缩减',
    respawn             INT COMMENT '复活时间',
    isVisiable          BOOLEAN COMMENT '是否能被敌人可见_1可见0不可见',
    moveSpeed           INT COMMENT '移动速度',
    kill                INT COMMENT '击杀数',
    dead                INT COMMENT '死亡数',
    assist              INT COMMENT '助攻数',
    curSkillID          ARRAY<INT> COMMENT '当前正在释放的技能ID',
    hpRecover           INT COMMENT '生命恢复',
    energyRecover       INT COMMENT '魔法恢复',
    atkSpeed            INT COMMENT '攻击速度',
    crt                 INT COMMENT '暴击概率',
    crtTimes            INT COMMENT '暴击倍数',
    phyThrough          INT COMMENT '物理护甲穿透',
    magThrough          INT COMMENT '魔法护甲穿透',
    phyThroughPercent   INT COMMENT '物理护甲穿透百分比',
    magThroughPercent   INT COMMENT '魔法护甲穿透百分比',
    spellSorb           INT COMMENT '魔法吸血',
    atkSorb             INT COMMENT '物理吸血',
    exState             ARRAY<INT> COMMENT '异常状态（处于眩晕、减速、击飞，无敌、免控等）',
    shield              INT COMMENT '护盾最大值',
    shieldPhy           INT COMMENT '物理护盾',
    shieldMgc           INT COMMENT '魔法护盾',
    shieldAll           INT COMMENT '通用护盾',
    shieldPhyMax        INT COMMENT '物理护盾最大值',
    shieldMgcMax        INT COMMENT '魔法护盾最大值',
    shieldAllMax        INT COMMENT '通用护盾最大值',
    headIconLockOnTargetIid  BIGINT COMMENT '正在锁定目标中',
    chaseTargetIid      INT COMMENT '追随目标Iid，与lockOnTargetIid不同，可以是友方',
    isChaseMove         BOOLEAN COMMENT '正在追击移动',
    isAutoAttacking     BOOLEAN COMMENT '是否在自动攻击中',
    lockOnTargetIid     INT COMMENT '锁定攻击目标，一定是敌人',
    isDead              BOOLEAN COMMENT '是否死亡',
--         新增字段
    tp_begintime        BIGINT COMMENT 'tp开始时间。局内秒数*10000。无tp为-1',
    tp_endtime        BIGINT COMMENT 'tp应该结束的时间，不是实际结束的时间。局内秒数*10000。无tp为-1',
--         ----
    bulletNum           INT COMMENT '子弹数目',
    portalCD            BIGINT COMMENT '传送阵CD',
    canEatCoin          BOOLEAN COMMENT '是否可以拾取金币',
    springPortalSuccess BOOLEAN COMMENT '回城技能使用成功',
    delcurSkillID       ARRAY<INT>,
    delexState          ARRAY<INT>,
    delbuffs          ARRAY<INT>,
    abilityState        ARRAY<STRUCT<
        Id:BIGINT COMMENT '技能ID',
        level_col:INT COMMENT '等级',
        slot:INT COMMENT '类型(0:代表普攻, 其它数值:代表技能1~n)',
        type_col:INT COMMENT '技能属于哪种类型',
        usable:BOOLEAN COMMENT '能否使用',
        cd:INT COMMENT '当前冷却时间',
        cdMax:INT COMMENT '最大冷却时间',
        atkRange:ARRAY<STRUCT<chaseRange:STRUCT<value_col:INT,nextValue:INT,startTime:INT,duration:INT>,castRange:STRUCT<value_col:INT,nextValue:INT,startTime:INT,duration:INT>,castRangeWithoutTarget:STRUCT<value_col:INT,nextValue:INT,startTime:INT,duration:INT>>> COMMENT '追击范围，最大半径，空挥范围',
        hpCost:INT COMMENT '消耗血量',
        mpCost:INT COMMENT '蓝量消耗',
        xpCost:INT COMMENT '能量消耗',
        segment:INT COMMENT '当前属于第几段',
        progress:INT COMMENT '技能进展',
        isOutofMana:INT COMMENT '是否没有蓝',
        storage:INT COMMENT '叠层剩余使用次数'
    >> COMMENT '技能列表',
    commands            ARRAY<STRUCT<
        type_col:INT COMMENT '命令类型(必填)',
        abilityID:INT COMMENT '技能ID',
        targetPosX:INT COMMENT '目标移动或者移动施法的位置x',
        targetPosY:INT COMMENT '目标移动或者移动施法的位置y',
        targeID:BIGINT COMMENT '攻击的目标ID',
        degreeX:INT COMMENT '施法角度x',
        degreeY:INT COMMENT '施法角度y',
        equiptId:INT COMMENT '装备ID',
        angle:INT COMMENT 'move角度',
        skillLevel:INT COMMENT '技能等级'
    >> COMMENT '操作命令',
    bServerAI           BOOLEAN COMMENT '是否托管英雄',
    equiptIDs           ARRAY<INT> COMMENT '当前的英雄的装备列表',
    equipIDRecommend    ARRAY<STRUCT<
        Id:INT,
        equips:ARRAY<INT>,
        lanes:ARRAY<INT>,
        weight:INT
    >> COMMENT '业务提供的装备列表',
    heroId              INT COMMENT '英雄ID(必填)',
    raceId              INT COMMENT '职业ID(必填)',
    account             BIGINT COMMENT '账号信息',
    playerLevel         INT COMMENT '等级(必填)',
    elo                 INT COMMENT 'elo值 （隐藏分）',
    score               INT COMMENT '评分（必填）',
    difficulty          INT COMMENT '难度 (以后再定，暂时可以不填)',
    playerType          INT COMMENT '0是真人，非0是AI',
    delabilityState     ARRAY<BIGINT>
)
    COMMENT '英雄的基础/技能/装备属性'
    PARTITIONED BY (game_server_version STRING, game_big_version_short STRING, game_key_version STRING, year STRING, month STRING, day STRING, part string)
    ROW FORMAT DELIMITED
    FIELDS TERMINATED BY '\001'
    COLLECTION ITEMS TERMINATED BY '\002'
    MAP KEYS TERMINATED BY '\003'
    STORED AS TEXTFILE;
--     STORED AS PARQUET TBLPROPERTIES('parquet.compression'='SNAPPY');
