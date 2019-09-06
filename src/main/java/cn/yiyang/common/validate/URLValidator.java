package cn.yiyang.common.validate;

import cn.yiyang.common.utils.ValidateUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 手机号验证器
 * @author ChenJianhui
 */
public class URLValidator implements ConstraintValidator<URL, String>{

    @Override
    public void initialize(URL url) {

    }

    @Override
    public boolean isValid(String url, ConstraintValidatorContext context) {
        if(!ValidateUtils.isValid(url)){
            return true;
        }else{
            String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(url);
            return m.find();
        }
    }
}
