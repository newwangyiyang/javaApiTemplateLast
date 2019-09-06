package cn.yiyang.common.validate;

import cn.yiyang.common.utils.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

/**
 * 验证参数在某个集合范围内
 * @author ChenJianhui
 */
public class ValueSetValidator implements ConstraintValidator<ValueSet, Object>{

    private ValueSet valueSet;

    /**
     * 初始化验证参数
     */
    @Override
    public void initialize(ValueSet valueSet) {
        this.valueSet = valueSet;
    }

    /**
     * 验证参数是否有效
     * @param target 验证目标
     */
    @Override
    public boolean isValid(Object target, ConstraintValidatorContext context) {
        if(target == null || StringUtils.isEmpty((String) target)){
            return false;
        }
        if(Arrays.asList(valueSet.value()).contains(target.toString())){
            return true;
        }else{
            String messageTemplate = context.getDefaultConstraintMessageTemplate();
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
            return false;
        }

    }

}
