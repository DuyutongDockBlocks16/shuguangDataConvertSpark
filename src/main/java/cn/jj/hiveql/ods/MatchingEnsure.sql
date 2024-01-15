-- 命名规范：小驼峰，关键字后面+"_col"，数字用_
--- 对局信息
create external table if not exists shuguang.shuguang_ods_game_MatchingEnsure_d_tmp (data string)
partitioned by (year string, month string, day string)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '|'  ESCAPED BY '\\'
STORED AS TEXTFILE location '/staging/shuguang/server/100055/MatchingEnsure';

ALTER TABLE  shuguang.shuguang_ods_game_MatchingEnsure_d_tmp add if not exists PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}')
    LOCATION '/staging/shuguang/server/100055/MatchingEnsure/${YEAR}/${MONTH}/${DAY}';

create table if not exists shuguang.shuguang_ods_game_MatchingEnsure_d (
    matching_id    bigint    comment '匹配局唯一ID（字段歧义：batch_id）',
    role_id    bigint    comment '玩家的UID',
    pool_id    string    comment '匹配池编号',
    protect_pool_id    string    comment '保护匹配池',
    origin_pool_id    string    comment '原始匹配池编号',
    elo    float    comment 'elo分当前',
    match_elo    float    comment '匹配elo分',
    team_elo    float    comment '队伍elo分',
    battle_sum    int    comment '对局总数 现个人界面处对局场次（当前完成的对局数，开始匹配，对局，对局结束后，都为前一场；当开始下一场匹配时+1）',
    leader_id    bigint    comment '队长的UID',
    team_mem_num    int    comment '队伍人数',
    camp_tids    string    comment '成局人数类型 返回双方队伍组成配置id',
    success    int    comment '是否成局',
    factionid    int    comment '玩家阵营id（字段歧义：camp_id）',
    ensure    int    comment '是否确认准备',
    modetid    int    comment '模式TID',
    start_col    double    comment '开始匹配时间',
    duration    int    comment '匹配持续时间',
    reason    string    comment '成局失败原因',
    server      string comment '服务器名',
    keyversion  int comment '版本md5'
)
    COMMENT '对局信息'
    partitioned by (year string, month string, day string)
    stored as TEXTFILE ;

insert overwrite table shuguang.shuguang_ods_game_MatchingEnsure_d PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}') select
    get_json_object(DATA,'$.matching_id'),
    get_json_object(DATA,'$.role_id'),
    get_json_object(DATA,'$.pool_id'),
    get_json_object(DATA,'$.protect_pool_id'),
    get_json_object(DATA,'$.origin_pool_id'),
    get_json_object(DATA,'$.elo'),
    get_json_object(DATA,'$.match_elo'),
    get_json_object(DATA,'$.team_elo'),
    get_json_object(DATA,'$.battle_sum'),
    get_json_object(DATA,'$.leader_id'),
    get_json_object(DATA,'$.team_mem_num'),
    get_json_object(DATA,'$.camp_tids'),
    get_json_object(DATA,'$.success'),
    get_json_object(DATA,'$.factionid'),
    get_json_object(DATA,'$.ensure'),
    get_json_object(DATA,'$.modetid'),
    get_json_object(DATA,'$.start'),
    get_json_object(DATA,'$.duration'),
    get_json_object(DATA,'$.reason'),
    get_json_object(DATA,'$.server'),
    get_json_object(DATA,'$.keyversion')
from shuguang.shuguang_ods_game_MatchingEnsure_d_tmp
where year='${YEAR}' AND month='${MONTH}' AND day='${DAY}';

