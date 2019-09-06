package cn.yiyang.common.RequestLimit;

import cn.yiyang.system.exception.WYYException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

//使用@Aspect注解将一个java类定义为切面类
@Aspect
@Component
@Slf4j
public class RequestLimitAop {
    @Autowired
    private RedisTemplate redisTemplate;
    //切面范围
    @Pointcut("execution(public * cn.yiyang.book.controller.*.*(..))")
    public void WebPointCut() {
    }

    @Before("WebPointCut() && @annotation(times)")
    /**
     * JoinPoint对象封装了SpringAop中切面方法的信息,在切面方法中添加JoinPoint参数,就可以获取到封装了该方法信息的JoinPoint对象.
     */
    public void ifovertimes(final JoinPoint joinPoint, RequestLimit times) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteAddr();
        String url = request.getRequestURL().toString();
        String key = "wangyiyang".concat(url).concat(ip);
        //访问次数加一
        long count = redisTemplate.opsForValue().increment(key, 1);
        //如果是第一次，则设置过期时间
        if (count == 1) {
            redisTemplate.expire(key, times.time(), TimeUnit.MILLISECONDS);
        }
        if (count > times.count()) {
            log.error("请求地址: {}, 请求ip： {}, 第{}次请求", url, ip, count);
            throw new WYYException("请求次数太频繁");
        }

    }
}
