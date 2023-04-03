package com.wanghui.blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description:
 *  自定义注解，用于结合aop打印系统日志
 * @Author 王辉
 * @Create 2023/3/30 21:04
 * @Version 1.0
 */
@Target({ ElementType.METHOD })//只能加在方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface SelfDefinedSystemLog {
    String BusinessName();//业务名称
}
