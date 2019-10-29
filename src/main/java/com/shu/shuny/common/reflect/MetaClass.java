package com.shu.shuny.common.reflect;

import com.shu.shuny.common.invoke.Invoker;

/**
 * @author jsen.yin[jsen.yin@gmail.com]
 * 2019-10-29
 * @Description: <p></p>
 */
public class MetaClass {


    private final ReflectorFactory reflectorFactory;
    private final Reflector reflector;

    private MetaClass(Class<?> type, ReflectorFactory reflectorFactory) {
        this.reflectorFactory = reflectorFactory;
        this.reflector = reflectorFactory.findForClass(type);
    }

    public static MetaClass forClass(Class<?> type, ReflectorFactory reflectorFactory) {
        return new MetaClass(type, reflectorFactory);
    }

    public Invoker getGetInvoker(String name) {
        return reflector.getGetInvoker(name);
    }

    public Invoker getSetInvoker(String name) {
        return reflector.getSetInvoker(name);
    }

}
