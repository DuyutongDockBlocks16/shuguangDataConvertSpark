ALTER TABLE  shuguang.shuguang_ods_game_PlayerBattle_d_delta_tmp add if not exists PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}')
    LOCATION '/staging/shuguang/server/100055/PlayerBattle/${YEAR}/${MONTH}/${DAY}';

insert overwrite table shuguang.shuguang_ods_game_PlayerBattle_d_delta PARTITION(year='${YEAR}', month='${MONTH}', day='${DAY}') select
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
    get_json_object(DATA,'$.logtime'),
    get_json_object(DATA,'$.ai_type'),
    get_json_object(DATA,'$.ai_base'),
    get_json_object(DATA,'$.deepainum')
from shuguang.shuguang_ods_game_PlayerBattle_d_delta_tmp
where year='${YEAR}' AND month='${MONTH}' AND day='${DAY}';
