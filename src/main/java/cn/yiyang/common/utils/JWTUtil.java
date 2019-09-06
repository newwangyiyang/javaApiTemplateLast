package cn.yiyang.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created with IntelliJ IDEA
 *
 * @Author yuanhaoyue swithaoy@gmail.com
 * @Description JWT 工具类
 * @Date 2018-04-07
 * @Time 22:48
 */
public class JWTUtil {
    // 过期时间 24 小时
    private static final long EXPIRE_TIME = 60 * 24 * 60 * 1000;
    // 密钥
    private static final String SECRET = "wangyiyang";

    /**
     * 生成 token, 5min后过期
     *
     * @param userId 用户id,userRoles  用户角色, userPermissions 用户权限
     * @return 加密的token
     */
//    public static String createToken(String userId, String[] userRoles, String[] userPermissions) {
    public static String createToken(String userId, String[] userRoles) {
        try {
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            return JWT.create()
                    .withClaim("userId", userId)  //存储用户的userId
                    .withArrayClaim("userRoles", userRoles)  //存储用的角色
                    //到期时间
                    .withExpiresAt(date)
                    //创建一个新的JWT，并使用给定的算法进行标记
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 校验 token 是否正确
     * 该方法是在CustomRealm中进行校验
     * @param token    密钥
     * @param userId 用户名
     * @return 是否正确
     */
    public static boolean verify(String token, String userId, String[] userRoles) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            //在token中附带了userId和userRoles信息
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("userId", userId)
                    .withArrayClaim("userRoles", userRoles)
                    .build();
            //验证 token
            verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息，无需secret解密也能获得
     *
     * @return token中包含的用户id
     */
    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 获得token中的信息，无需secret解密也能获得
     *
     * @return token中包含的用户角色
     */
    public static String[] getUserRoles(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userRoles").asArray(String.class);
        } catch (JWTDecodeException e) {
            return null;
        }
    }
}
