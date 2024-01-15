-- 可以满足”英雄死亡位置观测“和”玩家参团意愿观测“这两个需求
select
    kh.match_elo,
    kh.mode_tid,
    kh.battle_time,
    kh.die_role_id,
    kh.die_hero_id,
    kh.assist_num,
    kh.die_position_x,
    kh.die_position_y,
    getArea(kh.die_position_x, kh.die_position_y) -- TODO udf 获取坐标所在的区块
from
    shuguang_dwd_game_killhero_d kh