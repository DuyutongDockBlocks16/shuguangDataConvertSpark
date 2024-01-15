-- 可以满足“装备使用率”的需求
select
    be.battle_id,
    be.match_elo,
    be.mode_tid,
    be.battle_time,
    be.hero_id,
    be.hero_type,
    be.buy_equipment
from
    shuguang_dwd_game_buyequipment_d be;