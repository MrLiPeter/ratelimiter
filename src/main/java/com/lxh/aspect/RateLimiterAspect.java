package com.lxh.aspect;

import com.lxh.annotation.RateLimiter;
import com.lxh.enums.LimitType;
import com.lxh.exception.RateLimitException;
import com.lxh.utils.IpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

import java.util.Collections;

@Aspect
@Component
public class RateLimiterAspect {
    private static final Logger logger  = LoggerFactory.getLogger(RateLimiterAspect.class);

    @Autowired
    RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    RedisScript<Long> redisScript;



    @Before("@annotation(rateLimiter)")
    public void before(JoinPoint jp, RateLimiter rateLimiter) throws RateLimitException {
        int time = rateLimiter.time();
        int count = rateLimiter.count();
        String combineKey = getCombineKey(jp,rateLimiter);
        try {
            //超过限流阈值
            Long number = redisTemplate.execute(redisScript, Collections.singletonList(combineKey), time, count);
            if(number == null || number.intValue() > count) {
                logger.info("当前接口已经达到最大请求次数");
                throw new RateLimitException("访问过于频繁,请稍候访问.....");
            }
            logger.info("一个时间窗内请求次数:{},当前请求次数:{},缓存的key为{}",count,number,combineKey);
        }catch (Exception e){
            e.getMessage();
            throw e;
        }
    }

    /**
     * 这个 key 其实就是接口调用次数缓存在 redis 的 key
     * eg:
     * rate_limit:111.11.11-com.lxh.controller.Hello-controller-hello
     * rate_limit:com.lxh.controller.Hello-controller-hello
     * @param jp
     * @param rateLimiter
     * @return
     */
    private String getCombineKey(JoinPoint jp, RateLimiter rateLimiter) {
        StringBuffer key = new StringBuffer(rateLimiter.key()+":");
        if(rateLimiter.limitType() == LimitType.IP){
            key.append(IpUtils.getIpAddr(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest()))
                    .append("-");
        }
        //用反射机制获取Controller类,和接口方法
        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        key.append(method.getDeclaringClass().getName()).append("-").append(method.getName());
        return key.toString();
    }


}
