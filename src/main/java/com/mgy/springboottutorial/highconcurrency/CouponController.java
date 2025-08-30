package com.mgy.springboottutorial.highconcurrency;

import com.mgy.springboottutorial.highconcurrency.dto.CouponDTO;
import com.mgy.springboottutorial.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class CouponController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisScript<Long> couponDecr;


    private final String couponUserSet = "coupon:phone:iphone16_user";

    private final String couponKey = "coupon:phone:iphone16_count";

    @PostMapping("/coupon/start")
    public void start() {

        redisUtil.delete(couponKey);
        redisUtil.delete(couponUserSet);

        int count = 50000;

        stringRedisTemplate.opsForValue().set(couponKey, String.valueOf(count), 10, TimeUnit.MINUTES);
        log.info("活动开始");

    }

    @PostMapping("/coupon/get")
    public String getCoupon(@RequestBody CouponDTO dto) {

        // String couponName = dto.getCouponName();

        if (!stringRedisTemplate.hasKey(couponKey)) {
            return "活动结束";
        }

        String count = redisUtil.get(couponKey);

        int num = Integer.parseInt(count);

        if (num <= 0) {
            return "券已抢完";
        }

        if (redisUtil.sIsMember(couponUserSet, String.valueOf(dto.getUserId()))) {
            return "已领取";
        }

        // 开始领取
        long ret = tryDecrementAndAdd(couponKey, couponUserSet, String.valueOf(dto.getUserId()));

        if (ret == 0) {
            return "券已抢完";
        }

        if (ret == 1) {
            return "领取成功";
        }

        if (ret == 2) {
            return "已领取";
        }

        return "领取失败";
    }

    /**
     *
     * @param couponKey
     * @param userSetKey
     * @param userId
     * @return 1-成功 0-失败 2-已存在
     */
    public Long tryDecrementAndAdd(String couponKey, String userSetKey, String userId) {

        log.info("couponKey:[{}],userSetKey:[{}],userId:[{}]", couponKey, userSetKey, userId);

        List<String> keys = new ArrayList<>();
        keys.add(couponKey);
        keys.add(userSetKey);

        return stringRedisTemplate.execute(couponDecr, keys, userId);
    }


}
