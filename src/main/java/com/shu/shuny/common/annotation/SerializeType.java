package com.shu.shuny.common.annotation;


import com.shu.shuny.common.enumeration.SerializeTypeEnum;

import java.lang.annotation.*;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/28 10:40
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SerializeType {
    SerializeTypeEnum type();
}
