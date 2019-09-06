package cn.yiyang.demo.controller;

import cn.yiyang.common.utils.ResultBean;
import cn.yiyang.system.shiro.util.AesCipherUtil;
import cn.yiyang.system.shiro.util.JedisUtil;
import cn.yiyang.system.shiro.util.JwtUtil;
import cn.yiyang.system.shiro.util.common.Constant;
import cn.yiyang.system.shiro.util.common.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName DemoController
 * @Description Token认证示例代码
 * @Author Administrator
 * @Date 2019/9/6 9:26
 * @Version 1.0
 **/
@RestController
@RequestMapping("demo")
@Slf4j
public class DemoController {

    /**
     * @Author Administrator
     * @Description 获取Token
     * 由于redis缓存的refreshToken每次都是唯一的，当用户在另外一个设备上进行登录，当前设备登录的token就会失效，即单点登录
     * @Date 12:25 2019/9/6
     * @Param
     * @return
     **/
    @GetMapping("login")
    public ResultBean login(HttpServletResponse httpServletResponse) {
        String userId = "1";
        String[] userRoles = {"admin"};
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        String token = JwtUtil.sign(userId, userRoles, currentTimeMillis);

        PropertiesUtil.readProperties("config.properties");
        String refreshTokenExpireTime = PropertiesUtil.getProperty("refreshTokenExpireTime");

        // 先判断用户当前是否已登录
        if (JedisUtil.exists(Constant.PREFIX_SHIRO_CACHE + userId)) {
            JedisUtil.delKey(Constant.PREFIX_SHIRO_CACHE + userId);
        }

        JedisUtil.setObject(Constant.PREFIX_SHIRO_REFRESH_TOKEN + userId, currentTimeMillis, Integer.parseInt(refreshTokenExpireTime));

        // 将token在header里面传递给前端， 前端再response拦截器里面对header中token进行获取
        //    var accessToken = response.headers['Authorization']
        //    if (accessToken) {
        //      console.log('RefreshToken:' + accessToken)
        //      removeToken()
        //      setToken(accessToken)
        //    }
        httpServletResponse.setHeader("Authorization", token);
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
        return ResultBean.success();
    }

    /**
     * @Author Administrator
     * @Description token校验
     * @Date 12:25 2019/9/6
     * @Param
     * @return
     **/
    @PostMapping("getDemo")
    @RequiresRoles(logical = Logical.OR, value = {"admin"})
    public ResultBean getDemo() {
        return ResultBean.success("demo is show");
    }

    /**
     * @Author Administrator
     * @Description 用户密码的加密解密demo
     * @Date 12:28 2019/9/6
     * @Param
     * @return
     **/
    @GetMapping("password")
    public ResultBean password() {

        // 将用户的账号密码一起加密存储在数据库中password字段
        //  String key = AesCipherUtil.enCrypto(userId + password); 加密

        // String key = AesCipherUtil.deCrypto(password) 解密 获取正常的 userId + password（未进行加密的）

        // 解密数据跟正常数据进行比较，校验用户登录
        // key.equal(userId + passwrod)


        String enCrypto = AesCipherUtil.enCrypto("admin");
        log.info("加密后的数据是: {}", enCrypto);
        log.info("解密出来的数据是: {}", AesCipherUtil.deCrypto(enCrypto));
        return ResultBean.success(enCrypto);
    }
}
