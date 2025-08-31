package com.mgy.springboottutorial.highconcurrency;


import com.mgy.springboottutorial.highconcurrency.dto.MsgLikeDTO;
import com.mgy.springboottutorial.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class MsgLikeController {


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private RedisScript<Long> msgLikeLuaScript;

    // set 里面存用户ID
    String msgLikeSetKey = "like:msg:{msgId}:users";

    // String 存储消息点赞数量
    String msgLikeCountKey = "like:msg:{msgId}:count";

    // set 里面存用户喜欢的msgId
    String userLikeMsgSet = "like:user:{userId}";


    @PostMapping("/high/concurrency/like/reset")
    public String reset() {
        String key = "like_count";
        redisUtil.set(key, "0");
        return redisUtil.get(key);
    }

    @PostMapping("/high/concurrency/like")
    public Long like() {

        String key = "like_count";
        Long l = redisUtil.incrBy(key, 1);
        return l;
    }

    @PostMapping("/high/concurrency/dislike")
    public Long dislike() {
        String key = "like_count";
        Long l = redisUtil.incrBy(key, -1);
        return l;
    }


    @PostMapping("/high/concurrency/msg/like")
    public Object msgLike(@RequestBody MsgLikeDTO dto) {

        String key1 = msgLikeSetKey.replace("{msgId}", dto.getMsgId());
        String key2 = msgLikeCountKey.replace("{msgId}", dto.getMsgId());
        String key3 = userLikeMsgSet.replace("{userId}", dto.getUserId().toString());
        log.info("key1:{},key2:{},key3:{}", key1, key2, key3);

        List<String> keys = Arrays.asList(key1, key2, key3);
        String arg1 = dto.getUserId().toString();
        String arg2 = dto.getLike() == 1 ? "like" : "unlike";
        String arg3 = String.valueOf(System.currentTimeMillis() / 1000L);
        String arg4 = dto.getMsgId();

        Long r = stringRedisTemplate.execute(msgLikeLuaScript, keys, arg1, arg2, arg3, arg4);

        if (r == 1) {
            return "操作成功";
        } else if (r == -1) {
            return "异常操作";
        } else {
            return "重复操作";
        }

    }


    @PostMapping("/high/concurrency/msg/like/count")
    public List<MsgLikeDTO.MsgCountResp> getCount(@RequestBody MsgLikeDTO.MsgCountQryDTO dto) {

        List<String> msgIds = dto.getMsgIds();

        List<MsgLikeDTO.MsgCountResp> ret = new ArrayList<>();
        for (String msgId : msgIds) {

            String key2 = msgLikeCountKey.replace("{msgId}", msgId);
            String s = redisUtil.get(key2);

            long l = 0L;
            if(s != null){
                l = Long.parseLong(s);
            }

            MsgLikeDTO.MsgCountResp r = new MsgLikeDTO.MsgCountResp();
            r.setMsgId(msgId);
            r.setCount(l);

            ret.add(r);
        }

        return ret;
    }


}
