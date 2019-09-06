package cn.yiyang.system.shiro;

import cn.hutool.core.lang.Console;
import cn.yiyang.system.shiro.util.JedisUtil;
import cn.yiyang.system.shiro.util.JwtUtil;
import cn.yiyang.system.shiro.util.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA
 *
 * @Author yuanhaoyue swithaoy@gmail.com
 * @Description 自定义 Realm
 * @Date 2018-04-09
 * @Time 16:58
 */
@Component
@Slf4j
public class CustomRealm extends AuthorizingRealm {
//    private final UserMapper userMapper;
//
//    @Autowired
//    public CustomRealm(UserMapper userMapper) {
//        this.userMapper = userMapper;
//    }

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 默认使用此方法进行用户id正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException { //认证
        log.info("————身份认证方法————");
        String token = (String) authenticationToken.getCredentials();
        log.info("获取到请求头header里面的token: {}", token);
        // 解密获得username，用于和数据库进行对比
        String userId = JwtUtil.getClaim(token, Constant.USERID);
        String[] userRoles = JwtUtil.getClaim(token, Constant.USERROLES, "role");
        log.info("获取到的userId是: {}, 获取到的userRoles是: {}", userId, userRoles);
        JedisUtil.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + userId);
        if(JwtUtil.verify(token) && JedisUtil.exists(Constant.PREFIX_SHIRO_REFRESH_TOKEN + userId)) {
            // 获取RefreshToken的时间戳
            String currentTimeMillisRedis = JedisUtil.getObject(Constant.PREFIX_SHIRO_REFRESH_TOKEN + userId).toString();
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            if (JwtUtil.getClaim(token, Constant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                return new SimpleAuthenticationInfo(token, token, "CustomRealm");
            }
        }
        throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {  //授权
        log.info("————权限认证————");
        //访问权限认证失败会抛出AuthorizationException异常
        String userId = JwtUtil.getClaim(principals.toString(), Constant.USERID);
        String[] userRoles = JwtUtil.getClaim(principals.toString(), Constant.USERROLES, "role");
        //用于校验角色，权限工具类
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(Arrays.asList(userRoles)); //向校验类里面填充用户的角色信息
        //获得该用户角色
//        String role = userMapper.getRole(username);
        //每个角色拥有默认的权限
//        String rolePermission = userMapper.getRolePermission(username);
        //每个用户可以设置新的权限
//        String permission = userMapper.getPermission(username);
//        Set<String> roleSet = new HashSet<>();
        //需要将 role, permission 封装到 Set 作为 info.setRoles(), info.setStringPermissions() 的参数
//        roleSet.add(role);
        //设置该用户拥有的角色和权限
//        info.setRoles(roleSet); //添加角色
//        Set<String> permissionSet = new HashSet<>();
//        permissionSet.add(rolePermission);
//        permissionSet.add(permission);
//        info.setStringPermissions(permissionSet); //添加权限
        return info;
    }
}
