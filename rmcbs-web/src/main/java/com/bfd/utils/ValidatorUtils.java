package com.bfd.utils;

import com.bfd.common.exception.RmcbsException;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * hibernate-validator校验工具类 Bean Validation 中内置的 constraint
 *
 * @author bfd
 * @Null 被注释的元素必须为 null
 * @NotNull 被注释的元素必须不为 null
 * @AssertTrue 被注释的元素必须为 true
 * @AssertFalse 被注释的元素必须为 false
 * @Min(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
 * @Max(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值
 * @DecimalMin(value) 被注释的元素必须是一个数字，其值必须大于等于指定的最小值
 * @DecimalMax(value) 被注释的元素必须是一个数字，其值必须小于等于指定的最大值 @Size(max=, min=) 被注释的元素的大小必须在指定的范围内 集合或数组 集合或数组的大小是否在指定范围内
 * @Digits (integer, fraction) 验证字符串是否是符合指定格式的数字，interger指定整数精度，fraction指定小数精度。
 * @Past 被注释的元素必须是一个过去的日期
 * @Future 被注释的元素必须是一个将来的日期 @Pattern(regex=,flag=) 被注释的元素必须符合指定的正则表达式
 *
 *         Hibernate Validator 附加的 constraint
 * @NotBlank(message =) 验证字符串非null，且长度必须大于0
 * @Email 被注释的元素必须是电子邮箱地址 @Length(min=,max=) 被注释的字符串的大小必须在指定的范围内
 * @NotEmpty 被注释的字符串的必须非空 @Range(min=,max=,message=) 被注释的元素必须在合适的范围内
 *
 *           以上每个注解都可能性有一个message属性，用于在验证失败后向用户返回的消息，还可以三个属性上使用多个注解
 *
 *           参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 * @date 2018-07-26
 */
public class ValidatorUtils {
    
    private static Validator validator;
    
    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
    
    /**
     * 校验对象
     *
     * @param object 待校验对象
     * @param groups 待校验的组
     * @throws RmcbsException 校验不通过，则报RRException异常
     */
    public static void validateEntity(Object object, Class<?>... groups) throws RmcbsException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            ConstraintViolation<Object> constraint = (ConstraintViolation<Object>)constraintViolations.iterator().next();
            throw new RmcbsException(constraint.getPropertyPath() + constraint.getMessage());
        }
    }
    
    public static void isBlank(String str, String message) {
        if (StringUtils.isBlank(str)) {
            throw new RmcbsException(message);
        }
    }
    
    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new RmcbsException(message);
        }
    }
}
