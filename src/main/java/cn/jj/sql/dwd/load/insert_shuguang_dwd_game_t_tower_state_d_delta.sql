insert overwrite table shuguang.shuguang_dwd_game_t_tower_state_d_delta PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}',part='${part}')
select
/*+ MAPJOIN(shuguang_dim_game_battle_time)*/
ogts.battleid,
ogts.complete,
dgbt.id,
ogts.id,
ogts.campid,
ogts.type,
ogts.posx,
ogts.posy,
dgma.id,
ogts.towerId,
ogts.hp,
ogts.hpMax,
ogts.atk,
ogts.def,
ogts.isVisiable,
ogts.targetId,
ogts.atkRange
from (select * from shuguang.shuguang_ods_game_tower_state_d_delta where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}' and part='${part}' and targetId != 0) ogts
         inner join shuguang.shuguang_dim_game_battle_time dgbt on ogts.frameno = dgbt.frame_no and dgbt.version = '${dim_version}'
         inner join shuguang.shuguang_dim_game_map_area dgma on (floor((ogts.posx/10000-40)/12) = dgma.x_section and floor((ogts.posy/10000-40)/12) = dgma.y_section and dgma.version = '${dim_version}')