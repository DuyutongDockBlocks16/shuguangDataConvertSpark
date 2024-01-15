-- 可以满足“小兵死亡位置观测”和“兵线推进观测”两个需求
select
    kc.battle_id,
    kc.match_elo,
    kc.mode_tid,
    kc.creep_round,
    kc.creep_line,
    kc.creep_tid,
    kc.creep_position_x,
    kc.creep_position_y,
    getArea(kc.creep_position_x, kc.creep_position_y) -- TODO udf
from
    shuguang_dwd_game_killcreep_d kc;