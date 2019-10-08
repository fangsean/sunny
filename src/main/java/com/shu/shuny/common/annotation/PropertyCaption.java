package com.shu.shuny.common.annotation;

import java.lang.annotation.*;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/30 19:05
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PropertyCaption {
    String value();
}
