package com.huang.lightweight.liteconnect;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ConnectClient {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    String beanName() default "";

    boolean primary() default true;
}
