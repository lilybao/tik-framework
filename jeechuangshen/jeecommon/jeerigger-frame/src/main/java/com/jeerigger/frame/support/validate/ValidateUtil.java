package com.jeerigger.frame.support.validate;

import com.jeerigger.frame.exception.ValidateException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidateUtil {
    //初始化验证参数的框架
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 校验数据是否符合规则
     *
     * @param object 校验对象
     * @throws ValidateException 校验不通过，则抛出ValidateException异常
     */
    public static void validateObject(Object object) throws ValidateException {
        validateObject(object, null);
    }

    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws ValidateException 校验不通过，则报ValidateException异常
     */
    public static void validateObject(Object object, Class<?>... groups) throws ValidateException {
        Set<ConstraintViolation<Object>> constraintViolations;
        if (groups != null) {
            constraintViolations = validator.validate(object, groups);
        } else {
            constraintViolations = validator.validate(object);
        }
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for (ConstraintViolation<Object> constraint : constraintViolations) {
                msg.append(constraint.getMessage());
            }
            throw new ValidateException(msg.toString());
        }
    }
}
