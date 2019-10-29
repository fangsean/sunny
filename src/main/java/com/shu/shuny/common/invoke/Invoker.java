package com.shu.shuny.common.invoke;

import java.lang.reflect.InvocationTargetException;

/**
 * @author jsen.yin[jsen.yin@gmail.com]
 * 2019-10-29
 * @Description: <p></p>
 */
public interface Invoker {

    Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException;

    Class<?> getType();

}
