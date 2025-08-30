-- 如果值 > 0，则自减 1，并插入 set
-- 返回值：
--   1: 成功领取
--   0: 库存不足
--   2: 用户已领取过

local current = redis.call('GET', KEYS[1])
local isMember = redis.call('SISMEMBER', KEYS[2], ARGV[1])

if isMember == 1 then
    return 2
end

-- ✅ 利用 Lua 短路求值：只有 current 存在且 tonumber(current) > 0 才返回 true
if current and tonumber(current) and tonumber(current) > 0 then
    redis.call('DECR', KEYS[1])
    redis.call('SADD', KEYS[2], ARGV[1])
    return 1
else
    return 0
end