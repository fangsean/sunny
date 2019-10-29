package com.shu.shuny.common.invoke;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @author jsen.yin[jsen.yin@gmail.com]
 * 2019-10-29
 * @Description: <p></p>
 */
public class SetFieldInvoker implements Invoker {
    private final Field field;

    public SetFieldInvoker(Field field) {
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        field.set(target, args[0]);
        return null;
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
