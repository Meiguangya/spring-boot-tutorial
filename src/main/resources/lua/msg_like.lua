-- 合并点赞/取消点赞脚本
-- KEYS[1] = like:msg:{msgId}            → 消息的点赞用户集合 (Set)
-- KEYS[2] = like:msg:{msgId}:count      → 点赞计数器 (String)
-- KEYS[3] = like:user:{userId}          → 用户点赞记录 (ZSET)

-- ARGV[1] = userId
-- ARGV[2] = action: 'like' 或 'unlike'
-- ARGV[3] = timestamp (毫秒), 仅在 action='like' 时使用


-- 点赞 EVAL "脚本" 3 like:msg:123 like:msg:123:count like:user:1001  user:1001  like  1725123456789
-- 取消点赞 EVAL "脚本" 3 like:msg:123 like:msg:123:count like:user:1001  user:1001  unlike  0

local user = ARGV[1]
local action = ARGV[2]
local msgId = ARGV[4]

if action == 'like' then
    local added = redis.call('SADD', KEYS[1], user)
    if added == 1 then
        redis.call('INCR', KEYS[2])
        -- 使用 ARGV[3] 作为时间戳
        redis.call('ZADD', KEYS[3], ARGV[3], msgId)
        return 1
    else
        return 0  -- 已点赞
    end

elseif action == 'unlike' then
    local removed = redis.call('SREM', KEYS[1], user)
    if removed == 1 then
        redis.call('DECR', KEYS[2])
        redis.call('ZREM', KEYS[3], user)
        return 1
    else
        return 0  -- 未点赞，无需取消
    end

else
    return -1  -- 无效操作
end