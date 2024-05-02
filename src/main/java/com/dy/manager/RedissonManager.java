package com.dy.manager;

import com.dy.common.ErrorCode;
import com.dy.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: dy
 * @Date: 2024/4/23 19:21
 * @Description:
 */
@Component
@Slf4j
public class RedissonManager {

    @Resource
    private RedissonClient redissonClient;

    public void doRateLimit(String key) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(RateType.OVERALL, 1, 1, RateIntervalUnit.SECONDS);

        //  当一个操作来了之后, 请求一个令牌
        boolean flag = rateLimiter.tryAcquire(1);


        if (!flag) {
            log.info("我们进行限流!!!");
            //  获取令牌失败, 我们抛出异常
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST);
        }
    }

}
