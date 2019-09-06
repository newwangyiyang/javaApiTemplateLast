package cn.yiyang.system.shiro.cache;

import cn.hutool.core.lang.Console;
import cn.yiyang.system.shiro.util.JedisUtil;
import cn.yiyang.system.shiro.util.JwtUtil;
import cn.yiyang.system.shiro.util.common.Constant;
import cn.yiyang.system.shiro.util.common.PropertiesUtil;
import cn.yiyang.system.shiro.util.common.SerializableUtil;
import lombok.Data;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

/**
 * 重写Shiro的Cache保存读取
 * @author dolyw.com
 * @date 2018/9/4 17:31
 */
public class CustomCache<K,V> implements Cache<K,V> {

    /**
     * 缓存的key名称获取为shiro:cache:USERID
     * @param key
     * @return java.lang.String
     * @author dolyw.com
     * @date 2018/9/4 18:33
     */
    private String getKey(Object key) {
        return Constant.PREFIX_SHIRO_CACHE + JwtUtil.getClaim(key.toString(), Constant.USERID);
    }

    /**
     * 获取缓存
     */
    @Override
    public Object get(Object key) throws CacheException {
        if(!JedisUtil.exists(this.getKey(key))){
            return null;
        }
        return JedisUtil.getObject(this.getKey(key));
    }

    /**
     * 保存缓存
     */
    @Override
    public Object put(Object key, Object value) throws CacheException {
        // 设置Redis的Shiro缓存
        PropertiesUtil.readProperties("config.properties");
        String shiroCacheExpireTime = PropertiesUtil.getProperty("shiroCacheExpireTime");
        return JedisUtil.setObject(this.getKey(key), value, Integer.parseInt(shiroCacheExpireTime));
    }

    /**
     * 移除缓存
     */
    @Override
    public Object remove(Object key) throws CacheException {
        if(!JedisUtil.exists(this.getKey(key))){
            return null;
        }
        JedisUtil.delKey(this.getKey(key));
        return null;
    }

    /**
     * 清空所有缓存
     */
    @Override
    public void clear() throws CacheException {
        Objects.requireNonNull(JedisUtil.getJedis()).flushDB();
    }

    /**
     * 缓存的个数
     */
    @Override
    public int size() {
        Long size = Objects.requireNonNull(JedisUtil.getJedis()).dbSize();
        return size.intValue();
    }

    /**
     * 获取所有的key
     */
    @Override
    public Set keys() {
        Set<byte[]> keys = Objects.requireNonNull(JedisUtil.getJedis()).keys("*".getBytes());
        Set<Object> set = new HashSet<Object>();
        for (byte[] bs : keys) {
            set.add(SerializableUtil.unserializable(bs));
        }
        return set;
    }

    /**
     * 获取所有的value
     */
    @Override
    public Collection values() {
        Set keys = this.keys();
        List<Object> values = new ArrayList<Object>();
        for (Object key : keys) {
            values.add(JedisUtil.getObject(this.getKey(key)));
        }
        return values;
    }
}
