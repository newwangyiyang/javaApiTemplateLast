package cn.yiyang.system.shiro.util;

import cn.hutool.core.lang.Console;
import cn.yiyang.system.exception.WYYException;
import cn.yiyang.system.shiro.util.common.Base64ConvertUtil;
import cn.yiyang.system.shiro.util.common.Constant;
import cn.yiyang.system.shiro.util.common.PropertiesUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * JAVA-JWT工具类
 * @author Wang926454
 * @date 2018/8/30 11:45
 */
@Component
public class JwtUtil {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * 校验token是否正确
     * @param token Token
     * @return boolean 是否正确
     * @author Wang926454
     * @date 2018/8/31 9:05
     */
    public static boolean verify(String token) {
        try {
            // 帐号加JWT私钥解密
            PropertiesUtil.readProperties("config.properties");
            String encryptJWTKey = PropertiesUtil.getProperty("encryptJWTKey");
            String secret = getClaim(token, Constant.USERID) + Base64ConvertUtil.decode(encryptJWTKey);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            logger.error("JWTToken认证解密出现UnsupportedEncodingException异常:" + e.getMessage());
            throw new WYYException("JWTToken认证解密出现UnsupportedEncodingException异常:" + e.getMessage());
        }
    }

    /**
     * 获得Token中的信息无需secret解密也能获得
     * @param token
     * @param claim
     * @return java.lang.String
     * @author Wang926454
     * @date 2018/9/7 16:54
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            logger.error("解密Token中的公共信息出现JWTDecodeException异常:" + e.getMessage());
            throw new WYYException("解密Token中的公共信息出现JWTDecodeException异常:" + e.getMessage());
        }
    }

    public static String[] getClaim(String token, String claim, String role) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getClaim(claim).asArray(String.class);
        } catch (JWTDecodeException e) {
            logger.error("解密Token中的公共信息出现JWTDecodeException异常:" + e.getMessage());
            throw new WYYException("解密Token中的公共信息出现JWTDecodeException异常:" + e.getMessage());
        }
    }

    /**
     * 生成签名
     * @param userId 帐号
     * @return java.lang.String 返回加密的Token
     * @author Wang926454
     * @date 2018/8/31 9:07
     */
    public static String sign(String userId, String[] userRoles, String currentTimeMillis) {
        try {
            // 帐号加JWT私钥加密
            PropertiesUtil.readProperties("config.properties");
            String encryptJWTKey = PropertiesUtil.getProperty("encryptJWTKey");
            String accessTokenExpireTime = PropertiesUtil.getProperty("accessTokenExpireTime");
            String secret = userId + Base64ConvertUtil.decode(encryptJWTKey);
            // 此处过期时间是以毫秒为单位，所以乘以1000
            Date date = new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpireTime) * 1000);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带account帐号信息
            return JWT.create()
                    .withClaim("userId", userId)
                    .withArrayClaim("userRoles", userRoles)
                    .withClaim("currentTimeMillis", currentTimeMillis)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            logger.error("JWTToken加密出现UnsupportedEncodingException异常:" + e.getMessage());
            throw new WYYException("JWTToken加密出现UnsupportedEncodingException异常:" + e.getMessage());
        }
    }
}
