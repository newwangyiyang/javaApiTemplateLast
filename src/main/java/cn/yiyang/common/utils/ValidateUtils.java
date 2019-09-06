package cn.yiyang.common.utils;

import java.util.Collection;

/**
 * 校验工具类
 * @author chenjianhui
 * @date 2017/03/15
 */
public class ValidateUtils {

    /**
     * 判断字符串有效性
     */
    public static boolean isValid(String src){
        if(src == null || "".equals(src)){
            return false ;
        }
        return true ;
    }

    /**
     * 判断Boolean值
     * @param bool
     * @return
     */
    public static boolean isValid(Boolean bool){
        return bool != null && bool;
    }

    /**
     * 判断集合的有效性
     */
    public static boolean isValid(Collection<?> col){
        if(col == null || col.isEmpty()){
            return false ;
        }
        return true ;
    }

    /**
     * 判断数组是否有效
     */
    public static boolean isValid(Object[] arr){
        if(arr == null || arr.length == 0){
            return false ;
        }
        return true ;
    }

    /**
     * 验证手机是否合法，如果合法返回true,否则返回false
     * @param phone
     * @return
     */
    public static boolean isValidPhone(String phone){
        return isValid(phone) && phone.matches("^1\\d{10}$");
    }

    /**
     * 验证手机是否邮箱，如果合法返回true,否则返回false
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email){
        return isValid(email) && email.matches("(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})");
    }


}
