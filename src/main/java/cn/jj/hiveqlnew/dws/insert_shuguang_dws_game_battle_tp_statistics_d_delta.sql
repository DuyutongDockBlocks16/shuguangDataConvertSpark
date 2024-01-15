insert overwrite table shuguang.shuguang_dws_game_battle_tp_statistics_d_delta PARTITION(game_server_version='${game_server_version}',game_big_version_short='${game_big_version_short}',game_key_version='${game_key_version}',year='${YEAR}',month='${MONTH}',day='${DAY}')
select
       a.battle_id,
       a.player_type,
       a.tp_action_sum,
       b.tp_success_sum,
       cast(b.tp_success_sum/a.tp_action_sum as decimal(10,2)) as tp_success_rate
from
    (select battle_id,player_type,sum(tp_action_sum) as tp_action_sum from shuguang.shuguang_dws_game_battle_tp_d_delta where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}' group by battle_id,player_type) a
        inner join
    (select battle_id,player_type,sum(tp_success) as tp_success_sum from shuguang.shuguang_dwd_game_t_tp_success_d_delta where game_server_version='${game_server_version}'and game_big_version_short='${game_big_version_short}'and game_key_version='${game_key_version}' and year='${YEAR}' and month='${MONTH}' and day='${DAY}' group by battle_id,player_type) b
    on a.battle_id = b.battle_id and a.player_type = b.player_type
