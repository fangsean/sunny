package com.shu.shuny.common.util;

import com.shu.shuny.common.invoke.Invoker;
import com.shu.shuny.common.reflect.DefaultReflectorFactory;
import com.shu.shuny.common.reflect.MetaClass;
import com.shu.shuny.model.InvokerService;
import org.apache.commons.collections.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jsen.yin[jsen.yin@gmail.com]
 * @Description: <p>主流程设计：通过属性名获取、计算、设置值</p>
 */
public class Flector<T> {

    //反射实现计算
    private DefaultReflectorFactory defaultReflectorFactory;
    private MetaClass metaClass;

    public Flector(Class<T> clazz) {
        defaultReflectorFactory = new DefaultReflectorFactory();
        metaClass = MetaClass.forClass(clazz, defaultReflectorFactory);
    }


    public Class<?> getType(String name) {
        Invoker invoker = metaClass.getGetInvoker(name);
        Class<?> type = invoker.getType();
        return type;
    }

    public <U> U getter(T obj, String name) {
        Invoker invoker = metaClass.getGetInvoker(name);
        U invoke = null;
        try {
            invoke = (U) invoker.invoke(obj, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return invoke;
    }

    public void setter(T obj, String name, Object[] value) {
        Invoker invoker = metaClass.getSetInvoker(name);
        try {
            invoker.invoke(obj, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void setter(T obj, String name, Object value) {
        Invoker invoker = metaClass.getSetInvoker(name);
        try {
            Object[] objects = {value};
            invoker.invoke(obj, objects);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Flector<InvokerService> flector = new Flector(InvokerService.class);

        List<InvokerService> data = new ArrayList<>();
        data.add(InvokerService.builder()/*.timeout(12)*/.build());
        data.add(InvokerService.builder()/*.timeout(13)*/.build());
        data.add(InvokerService.builder().timeout(14).build());


        InvokerService invokerService = new InvokerService();
        if (CollectionUtils.isNotEmpty(data)) {
            long timeout = data.stream().mapToLong(T -> flector.getter(T, "timeout")).sum();
            System.out.printf(String.valueOf(timeout));
            flector.setter(invokerService,"timeout",timeout);
            System.out.printf(invokerService.toString());
        }

    }

}

