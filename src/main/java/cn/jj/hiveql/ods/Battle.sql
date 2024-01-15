-- 命名规范：小驼峰，关键字后面+"_col"，数字用_
--- 对局信息
create external table if not exists shuguang.shuguang_ods_game_Battle_d_tmp (data string)
partitioned by (year string, month string, day string)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '|'  ESCAPED BY '\\'
STORED AS TEXTFILE location '/staging/shuguang/server/100055/Battle';

ALTER TABLE  shuguang.shuguang_ods_game_Battle_d_tmp add if not exists PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}')
    LOCATION '/staging/shuguang/server/100055/Battle/${YEAR}/${MONTH}/${DAY}';

create table if not exists shuguang.shuguang_ods_game_Battle_d (
     battleId    bigint    comment '战斗ID',
     gameType    int    comment '游戏类型',
     modeTid     int    comment '模式ID',
     battleTId    int    comment '战斗地图ID（字段注释修正：对局配置id）',
     beginTime    timestamp    comment '开始时间（字段修正：battle_start_time）',
     playTime    int    comment '游戏时长',
     playersNum    int    comment '对局人数',
     userNum    int    comment '真玩家人数',
     fakeuserNum    int    comment '填充电脑人数',
     robotNum    int    comment '电脑人数',
     gatherReportSec    float    comment '结算消耗时长（从收到第一个玩家结算数据开始计算到出结算数据发送前端截止）',
     illegalPlayer_1    int    comment '挂机人数（字段修正：afk_player_num）',
     illegalPlayer_3    int    comment '逃跑人数（逃跑的不算挂机）（字段修正：runaway_player_num）'
)
    COMMENT '对局信息'
    partitioned by (year string, month string, day string)
    stored as TEXTFILE ;

insert overwrite table shuguang.shuguang_ods_game_Battle_d PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}') select
    get_json_object(data,'$.battleid'),
    get_json_object(data,'$.gametype'),
    get_json_object(data,'$.modetid'),
    get_json_object(data,'$.battletid'),
    cast(get_json_object(data,'$.begintime') as int),
    get_json_object(data,'$.playtime'),
    get_json_object(data,'$.playersnum'),
    get_json_object(data,'$.usernum'),
    get_json_object(data,'$.fakeusernum'),
    get_json_object(data,'$.robotnum'),
    get_json_object(data,'$.gather_report_sec'),
    get_json_object(data,'$.illegal_player_1'),
    get_json_object(data,'$.illegal_player_3')
from shuguang.shuguang_ods_game_Battle_d_tmp
where year='${YEAR}' AND month='${MONTH}' AND day='${DAY}';

