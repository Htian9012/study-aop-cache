package com.anno;

import java.lang.annotation.*;

/**
 * 缓存切面
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Cache {

    boolean open() default false;

    String key() default "";

    long expire() default 0;

}