package cn.yiidii.ratelimit.aspect;

import cn.yiidii.base.annotation.RateLimiter;
import cn.yiidii.base.domain.enums.LimitType;
import cn.yiidii.base.exception.RateLimitException;
import cn.yiidii.base.exception.code.ExceptionCode;
import cn.yiidii.base.util.ServletUtil;
import cn.yiidii.redis.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RateType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 限流处理
 *
 * @author Lion Li
 */
@Slf4j
@Aspect
@Component
public class RateLimiterAspect {

    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint point, RateLimiter rateLimiter) {
        int time = rateLimiter.time();
        int count = rateLimiter.count();
        String combineKey = getCombineKey(rateLimiter, point);
        try {
            RateType rateType = RateType.OVERALL;
            if (rateLimiter.limitType() == LimitType.CLUSTER) {
                rateType = RateType.PER_CLIENT;
            }
            long number = RedisUtils.rateLimiter(combineKey, rateType, count, time);
            log.info("限制令牌 => {}, 剩余令牌 => {}, 缓存key => '{}', TTL => '{}'", count, number, combineKey, RedisUtils.getTimeToLive(combineKey));
            if (number < 0) {
                throw new RateLimitException(ExceptionCode.RATE_LIMIT);
            }

        } catch (RateLimitException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("服务器限流异常，请稍候再试");
        }
    }

    public String getCombineKey(RateLimiter rateLimiter, JoinPoint point) {
        StringBuilder stringBuffer = new StringBuilder(rateLimiter.key());
        if (rateLimiter.limitType() == LimitType.IP) {
            // 获取请求ip
            stringBuffer.append(ServletUtil.getClientIp()).append("-");
        } else if (rateLimiter.limitType() == LimitType.CLUSTER) {
            // 获取客户端实例id
            stringBuffer.append(RedisUtils.getClient().getId()).append("-");
        }
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        stringBuffer.append(targetClass.getName()).append("-")
                .append(method.getName()).append("-")
                .append(rateLimiter.time()).append("-").append(rateLimiter.count());
        return stringBuffer.toString();
    }
}
