-- 命名规范：小驼峰，关键字后面+"_col"，数字用_
--- 玩家对局信息
create external table if not exists shuguang.shuguang_ods_game_PlayerBattle_d_tmp (data string)
partitioned by (year string, month string, day string)
ROW FORMAT DELIMITED FIELDS TERMINATED BY '|'  ESCAPED BY '\\'
STORED AS TEXTFILE location '/staging/shuguang/server/100055/PlayerBattle';

ALTER TABLE  shuguang.shuguang_ods_game_PlayerBattle_d_tmp add if not exists PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}')
    LOCATION '/staging/shuguang/server/100055/PlayerBattle/${YEAR}/${MONTH}/${DAY}';

create table if not exists shuguang.shuguang_ods_game_PlayerBattle_d (
     battleid    bigint    comment '战斗ID',
     gametype    int    comment '游戏类型',
     modetid    int    comment '模式TID',
     modetid_battle    int    comment '模式TID(实际战斗打的)',
     battletid    int    comment '战斗TID',
     role_id    bigint    comment '玩家的UID',
     factionid    int    comment '阵营ID（字段歧义：camp_id）',
     herotid    int    comment '英雄ID',
     equipments    string    comment '装备',
     runes    string    comment '铭文',
     masteries    string    comment '铭文专精',
     skillid    int    comment '技能ID',
     kill    int    comment '杀人数',
     death    int    comment '死亡数',
     assist    int    comment '辅助数',
     level    int    comment '等级',
     gold    int    comment '金币',
     score    float    comment '评分',
     damagescore    float    comment '伤害评分',
     survivalscore    float    comment '生存评分',
     economyscore    float    comment '经济评分',
     teamscore    float    comment '团队评分',
     kdascore    float    comment 'KDA评分',
     carryscore    float    comment '推进评分',
     towerkill    int    comment '击杀塔数目',
     soldierkill    int    comment '击杀小怪数目（字段修正：creep_kill）',
     creepkill    int    comment '击杀野怪数目（字段修正：monster_kill）',
     damage    int    comment '总伤害',
     phydmg    int    comment '总物理伤害',
     magdmg    int    comment '总魔法伤害',
     truedmg    int    comment '总真实伤害',
     dmgtohero    int    comment '对英雄的总伤害',
     dmgtotower    int    comment '对防御塔的总伤害',
     phydmgtohero    int    comment '对英雄的总物理伤害',
     magdmgtohero    int    comment '对英雄的总魔法伤害',
     truedmgtohero    int    comment '对英雄的总真实伤害',
     healingtohero    int    comment '对英雄造成总治疗',
     dmgreceived    int    comment '承受总伤害',
     phydmgreceived    int    comment '承受总物理伤害',
     magdmgreceived    int    comment '承受总魔法伤害',
     truedmgreceived    int    comment '承受总真实伤害',
     dmgreceivedfromhero    int    comment '承受英雄总伤害',
     phydmgreceivedfromhero    int    comment '承受英雄总物理伤害',
     magdmgreceivedfromhero    int    comment '承受英雄总魔法伤害',
     truedmgreceivedfromhero    int    comment '承受英雄总真实伤害',
     dmgtosoldier    int    comment '对小兵总伤害（字段修正：dmg2creep）',
     dmgtocreep    int    comment '对野怪总伤害（字段修正：dmg2monster）',
     ip    string    comment 'ip地址',
     rank    int    comment '段位（旧）',
     rank_tid    int    comment '段位',
     battle_sum    int    comment '对局总数 现个人界面处对局场次（当前完成的对局数，开始匹配，对局，对局结束后，都为前一场；当开始下一场匹配时+1）',
     rank_star    int    comment '星级',
     victory    int    comment '胜利为1 失败为0',
     playersnum    int    comment '对局人数',
     usernum    int    comment '真玩家人数',
     fakeusernum    int    comment '填充电脑人数',
     robotnum    int    comment '电脑人数',
     disconnect_times    int    comment '挂机次数（字段修正：afk_times）',
     is_disconnect    int    comment '是否挂机惩罚（字段修正：is_afk_punish）',
     triblekill    int    comment '三杀次数',
     quadrakill    int    comment '四杀次数',
     pentakill    int    comment '五杀次数',
     legendary    int    comment '超神次数',
     playtime int       comment '对局时间（备注修正：对局时长）',
     keyversion string  comment '版本id',
     logtime string     comment '对局开始时间（字段修正：battle_start_time）'
)
    COMMENT '玩家对局信息'
    partitioned by (year string, month string, day string)
    stored as TEXTFILE ;

insert overwrite table shuguang.shuguang_ods_game_PlayerBattle_d PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}') select
    get_json_object(DATA,'$.battleid'),
    get_json_object(DATA,'$.gametype'),
    get_json_object(DATA,'$.modetid'),
    get_json_object(DATA,'$.modetid_battle'),
    get_json_object(DATA,'$.battletid'),
    get_json_object(DATA,'$.role_id'),
    get_json_object(DATA,'$.factionid'),
    get_json_object(DATA,'$.herotid'),
    get_json_object(DATA,'$.equipments'),
    get_json_object(DATA,'$.runes'),
    get_json_object(DATA,'$.masteries'),
    get_json_object(DATA,'$.skillid'),
    get_json_object(DATA,'$.kill'),
    get_json_object(DATA,'$.death'),
    get_json_object(DATA,'$.assist'),
    get_json_object(DATA,'$.level'),
    get_json_object(DATA,'$.gold'),
    get_json_object(DATA,'$.score'),
    get_json_object(DATA,'$.damagescore'),
    get_json_object(DATA,'$.survivalscore'),
    get_json_object(DATA,'$.economyscore'),
    get_json_object(DATA,'$.teamscore'),
    get_json_object(DATA,'$.kdascore'),
    get_json_object(DATA,'$.carryscore'),
    get_json_object(DATA,'$.towerkill'),
    get_json_object(DATA,'$.soldierkill'),
    get_json_object(DATA,'$.creepkill'),
    get_json_object(DATA,'$.damage'),
    get_json_object(DATA,'$.phydmg'),
    get_json_object(DATA,'$.magdmg'),
    get_json_object(DATA,'$.truedmg'),
    get_json_object(DATA,'$.dmgtohero'),
    get_json_object(DATA,'$.dmgtotower'),
    get_json_object(DATA,'$.phydmgtohero'),
    get_json_object(DATA,'$.magdmgtohero'),
    get_json_object(DATA,'$.truedmgtohero'),
    get_json_object(DATA,'$.healingtohero'),
    get_json_object(DATA,'$.dmgreceived'),
    get_json_object(DATA,'$.phydmgreceived'),
    get_json_object(DATA,'$.magdmgreceived'),
    get_json_object(DATA,'$.truedmgreceived'),
    get_json_object(DATA,'$.dmgreceivedfromhero'),
    get_json_object(DATA,'$.phydmgreceivedfromhero'),
    get_json_object(DATA,'$.magdmgreceivedfromhero'),
    get_json_object(DATA,'$.truedmgreceivedfromhero'),
    get_json_object(DATA,'$.dmgtosoldier'),
    get_json_object(DATA,'$.dmgtocreep'),
    get_json_object(DATA,'$.ip'),
    get_json_object(DATA,'$.rank'),
    get_json_object(DATA,'$.rank_tid'),
    get_json_object(DATA,'$.battle_sum'),
    get_json_object(DATA,'$.rank_star'),
    get_json_object(DATA,'$.victory'),
    get_json_object(DATA,'$.playersnum'),
    get_json_object(DATA,'$.usernum'),
    get_json_object(DATA,'$.fakeusernum'),
    get_json_object(DATA,'$.robotnum'),
    get_json_object(DATA,'$.disconnect_times'),
    get_json_object(DATA,'$.is_disconnect'),
    get_json_object(DATA,'$.triblekill'),
    get_json_object(DATA,'$.quadrakill'),
    get_json_object(DATA,'$.pentakill'),
    get_json_object(DATA,'$.legendary'),
    get_json_object(DATA,'$.playtime'),
    get_json_object(DATA,'$.keyversion'),
    get_json_object(DATA,'$.logtime')
from shuguang.shuguang_ods_game_PlayerBattle_d_tmp
where year='${YEAR}' AND month='${MONTH}' AND day='${DAY}';

