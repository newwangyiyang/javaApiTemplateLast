package cn.yiyang.system.config;

import cn.hutool.core.lang.Console;
import lombok.Data;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Jedis配置，项目启动注入JedisPool
 * http://www.cnblogs.com/GodHeng/p/9301330.html
 * @author dolyw.com
 * @date 2018/9/5 10:35
 */
@Data
@Configuration
@EnableAutoConfiguration
public class JedisConfig {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(JedisConfig.class);

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout-num}")
    private int timeout;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.max-wait-num}")
    private int maxWait;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Bean
    public JedisPool redisPoolFactory() {
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxIdle(maxIdle);
            jedisPoolConfig.setMaxWaitMillis(maxWait);
            jedisPoolConfig.setMaxTotal(maxActive);
            jedisPoolConfig.setMinIdle(minIdle);
            // JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
            String pwd = StringUtil.isBlank(password) ? null : password;
            JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, pwd);
            logger.info("初始化Redis连接池JedisPool成功!地址: " + host + ":" + port);
            return jedisPool;
        } catch (Exception e) {
            logger.error("初始化Redis连接池JedisPool异常:" + e.getMessage());
        }
        return null;
    }
}

