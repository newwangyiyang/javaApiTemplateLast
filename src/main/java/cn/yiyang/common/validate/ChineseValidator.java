package cn.yiyang.common.validate;


import cn.yiyang.common.utils.StringUtils;
import cn.yiyang.common.utils.ValidateUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号验证器
 * @author ChenJianhui
 */
public class ChineseValidator implements ConstraintValidator<Chinese, String> {

    @Override
    public void initialize(Chinese chinese) {

    }

    @Override
    public boolean isValid(String str, ConstraintValidatorContext context) {
        if(!ValidateUtils.isValid(str)){
            return true;
        }
        return StringUtils.isChinese(str);
    }

}
