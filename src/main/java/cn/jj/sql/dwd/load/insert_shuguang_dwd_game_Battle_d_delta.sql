insert overwrite table shuguang.shuguang_dwd_game_Battle_d_delta PARTITION(year='${YEAR}',month='${MONTH}',day='${DAY}')
select distinct
    b.battleid,
    b.gametype,
    b.modetid,
    b.battletid,
    me.avg_team_elo,
    me.group_pool_id,
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
    me.group_server,
    1
from
    (select * from shuguang.shuguang_ods_game_battle_d_delta where year='${YEAR}'
          and month='${MONTH}'
          and day='${DAY}') b inner join (
        select
            mee.matching_id,
            group_concat(mee.pool_id) as group_pool_id,
            group_concat(mee.server) as group_server,
--             mee.protect_pool_id,
--             mee.origin_pool_id,
            cast(avg(mee.team_elo) as decimal(10,0)) as avg_team_elo
        from
            shuguang.shuguang_ods_game_matching_ensure_d_delta mee
        where
                mee.success=1
          and mee.ensure!=-1
          and mee.year='${YEAR}'
          and mee.month='${MONTH}'
          and mee.day='${DAY}'
        group by mee.matching_id
    ) me on b.battleid = me.matching_id;
