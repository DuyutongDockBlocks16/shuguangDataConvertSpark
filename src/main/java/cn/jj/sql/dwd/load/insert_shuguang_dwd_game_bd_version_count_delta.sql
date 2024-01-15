insert overwrite table shuguang.shuguang_dwd_game_bd_version_count_d_delta PARTITION(year='${YEAR}',month='${MONTH}',day='${DAY}',part='${part}')
select
split(version, '_')[0],
split(version, '_')[1],
split(version, '_')[2],
version,
count_col
from shuguang.shuguang_ods_game_bd_version_count_delta
where year='${YEAR}' and month='${MONTH}' and day='${DAY}' and part='${part}'
