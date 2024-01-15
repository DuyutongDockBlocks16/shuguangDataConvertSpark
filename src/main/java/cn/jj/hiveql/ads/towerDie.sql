-- 满足“防御塔摧毁时间观测”需求
select
    kt.battle_id,
    kt.match_elo,
    kt.mode_tid,
    getLine(kt.tower_position_x, kt.tower_position_y), -- udf 通过坐标获得分路
    kt.camp_id,
    kt.tower_iid,
    kt.tower_tid,
    kt.tower_level,
    getArea(kt.tower_position_x, kt.tower_position_y),
    kt.battle_time
from
    shuguang_dwd_game_killtower_d kt