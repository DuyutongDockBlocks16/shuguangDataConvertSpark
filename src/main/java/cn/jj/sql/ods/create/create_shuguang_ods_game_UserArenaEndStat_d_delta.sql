-- 命名规范：小驼峰，关键字后面+"_col"，数字用_
--- 玩家对局信息
drop table if exists shuguang.shuguang_ods_game_UserArenaEndStat_d_delta_tmp;
create external table if not exists shuguang.shuguang_ods_game_UserArenaEndStat_d_delta_tmp (data string)
partitioned by (year string, month string, day string)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '|'  ESCAPED BY '\\'
STORED AS TEXTFILE location '/staging/shuguang/server/100055/UserArenaEndStat';

drop table if exists shuguang.shuguang_ods_game_UserArenaEndStat_d_delta;
create table if not exists shuguang.shuguang_ods_game_UserArenaEndStat_d_delta (
    battleid    bigint    comment '战斗ID',
    modetid    int    comment '模式id',
    role_id    bigint    comment '玩家id',
    factionid    int    comment '阵营ID',
    disc_secs    string    comment '断线时间集合',
    idle_secs    string    comment '挂机时间集合',
    illegal    string    comment '判定非法行为集合',
    doubt_illegal    string    comment '判定疑似非法行为集合',
    conn_tcp_times    int    comment '连接tcp次数',
    disc_tcp_times    int    comment '断开tcp次数',
    conn_udp_times    int    comment '连接udp次数',
    disc_udp_times    int    comment '断开udp次数',
    duration    int    comment '对局时长',
    disc_tcp_sec    int    comment '断开tcp总时长',
    disc_udp_sec    int    comment '断开udp总时长',
    first_tcp    bigint    comment '第一次连接tcp时间戳',
    first_udp    bigint    comment '第一次连接udp时间戳',
    last_disc_tcp    bigint    comment '最后一次断开tcp时间戳',
    last_disc_udp    bigint   comment '最后一次断开udp时间戳',
    idle_first    int    comment '首次挂机时间',
    idle_cut    int    comment '挂机次数',
    idle_run    string    comment '挂机记录'
    ,invalid_play_secs	string	comment '玩家被动挂机数据'
    ,deepai_host_secs	string	comment '玩家被超参数ai托管的时间集合（数据示例：{"370.03":100.15,"615.36":47.51}，表示第一次托管从370.03秒开始，持续100.15s）'
    ,strategyai_host_secs	string	comment '玩家被行为树ai托管的时间集合（数据格式与deepai_host_secs一致）'
    ,negative_secs	string	comment '玩家消极数据参数1.首次购买装备时间，参数2.长时间未购买装备次数，参数3.恶意卖空装备次数'
    ,logtime	string	comment ''
)
    COMMENT '玩家对局-重连信息'
    partitioned by (year string, month string, day string)
    stored as TEXTFILE ;
