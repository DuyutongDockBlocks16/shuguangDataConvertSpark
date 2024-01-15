-- 满足“野怪击杀效率”、“大小龙击杀效率”和“大小龙击杀频次”三个需求
select
    km.battle_id,
    km.match_elo,
    km.mode_tid,
    km.battle_time,
    km.monster_iid,
    km.monster_tid,
    km.monster_type,
    km.monster_level,
    km.monster_position_x,
    km.monster_position_y,
    getArea(km.monster_position_x, km.monster_position_y),
    km.kill_take_time
from
    shuguang_dwd_game_killmonster_d km;