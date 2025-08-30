-- 原子性地设置键值对和过期时间
-- KEYS[1] 为 key
-- ARGV[1] 为 value
-- ARGV[2] 为过期时间（秒）

-- 检查key是否已存在（可选，根据业务决定）
-- if redis.call('exists', KEYS[1]) == 1 then
--     return 0
-- end

-- 设置键值对和过期时间
redis.call('set', KEYS[1], ARGV[1])
redis.call('expire', KEYS[1], ARGV[2])
return 1