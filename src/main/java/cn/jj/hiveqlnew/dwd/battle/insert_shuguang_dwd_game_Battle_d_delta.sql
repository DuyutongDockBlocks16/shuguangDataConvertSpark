insert overwrite table shuguang.shuguang_dwd_game_Battle_d_delta PARTITION(year='${YEAR}',month='${MONTH}',day='${DAY}')
select distinct
    b.battleid,
    b.gametype,
    b.modetid,
    b.battletid,
    me.avg_match_elo,
--     me.pool_id,
--     me.protect_pool_id,
--     me.origin_pool_id,
    b.begintime,
    b.playtime,
    b.playersnum,
    b.usernum,
    b.fakeusernum,
    b.robotnum,
    b.gatherreportsec,
    b.illegalplayer_1,
    b.illegalplayer_3,
    1
from
    (select * from shuguang.shuguang_ods_game_battle_d_delta where year='${YEAR}'
          and month='${MONTH}'
          and day='${DAY}') b inner join (
        select
            mee.matching_id,
--             mee.pool_id,
--             mee.protect_pool_id,
--             mee.origin_pool_id,
            cast(avg(DISTINCT mee.team_elo) as decimal(10,0)) as avg_match_elo
        from
            shuguang.shuguang_ods_game_matching_ensure_d_delta mee
        where
                mee.success=1
          and mee.ensure!=-1
          and mee.year='${YEAR}'
          and mee.month='${MONTH}'
          and mee.day='${DAY}'
        group by mee.matching_id,mee.pool_id
    ) me on b.battleid = me.matching_id;
