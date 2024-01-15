-- 命名规范：小驼峰，关键字后面+"_col"，数字用_
--- 对局信息
create external table if not exists shuguang.shuguang_ods_game_TournamentIntegral_d_tmp (data string)
partitioned by (year string, month string, day string)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '|'  ESCAPED BY '\\'
STORED AS TEXTFILE location '/staging/shuguang/server/100055/TournamentIntegral';

ALTER TABLE  shuguang.shuguang_ods_game_TournamentIntegral_d_tmp add if not exists PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}')
    LOCATION '/staging/shuguang/server/100055/TournamentIntegral/${YEAR}/${MONTH}/${DAY}';

create table if not exists shuguang.shuguang_ods_game_TournamentIntegral_d (
    server    string    comment '游戏服 服务器名称或id：',
    appid    int    comment 'appid 游戏的appid。',
    account_id    string    comment '账号唯一标识符 游戏账号的唯一标示符，用于统计用户数量。',
    app_channel    string    comment '运营渠道 运营渠道由平台分配，直接配置到游戏包的Manifest/info.plist文件中，游戏可直接读取，如：app_store、91_assistant、netease等。',
    platform    string    comment '游戏平台',
    role_id    int    comment '角色唯一标识符 角色唯一标识，在所有服唯一。',
    role_name    string    comment '角色名称 游戏角色名称，多服可重复，单服唯一。',
    role_level    int    comment '角色等级',
    role_stage    int    comment '角色段位',
    master_score    int    comment '大师分 玩家赛事大师分',
    master_level    int    comment '大师等级 玩家赛事大师等级',
    mac_addr    string    comment 'MAC地址 设备mac地址，如：08:00:20:0A:8C:6D，ios系统如果无法获取，可以将该字段置空。',
    ip    string    comment '如果是ipv4网络，将IP地址记录到该字段下',
    device_id    string    comment '设备唯一标识符 通过sdk提供的设备id，与前端日志一致。',
    recordid    int    comment '战斗条数',
    Tournament_id    int    comment 'Tournament_id',
    match_id    int    comment '赛事match_id（字段类型修正：BIGINT）',
    stage_id    int    comment '赛事Stage_id（字段类型修正：BIGINT）',
    battleid    bigint    comment '对局id',
    round    int    comment '赛事最大战斗场次数',
    integral    int    comment '争霸赛总分',
    integral_1    int    comment '胜负得分',
    integral_2    int    comment '表现得分',
    integral_3    int    comment 'MVP得分',
    integral_4    int    comment '连胜得分'
)
    COMMENT '争霸赛积分'
    partitioned by (year string, month string, day string)
    stored as TEXTFILE ;

insert overwrite table shuguang.shuguang_ods_game_TournamentIntegral_d PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}') select
    get_json_object(DATA,'$.server'),
    get_json_object(DATA,'$.appid'),
    get_json_object(DATA,'$.account_id'),
    get_json_object(DATA,'$.app_channel'),
    get_json_object(DATA,'$.platform'),
    get_json_object(DATA,'$.role_id'),
    get_json_object(DATA,'$.role_name'),
    get_json_object(DATA,'$.role_level'),
    get_json_object(DATA,'$.role_stage'),
    get_json_object(DATA,'$.master_score'),
    get_json_object(DATA,'$.master_level'),
    get_json_object(DATA,'$.mac_addr'),
    get_json_object(DATA,'$.ip'),
    get_json_object(DATA,'$.device_id'),
    get_json_object(DATA,'$.recordid'),
    get_json_object(DATA,'$.Tournament_id'),
    get_json_object(DATA,'$.match_id'),
    get_json_object(DATA,'$.stage_id'),
    get_json_object(DATA,'$.battleid'),
    get_json_object(DATA,'$.round'),
    get_json_object(DATA,'$.integral'),
    get_json_object(DATA,'$.integral_1'),
    get_json_object(DATA,'$.integral_2'),
    get_json_object(DATA,'$.integral_3'),
    get_json_object(DATA,'$.integral_4')
from shuguang.shuguang_ods_game_TournamentIntegral_d_tmp
where year='${YEAR}' AND month='${MONTH}' AND day='${DAY}';

