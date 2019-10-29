package com.shu.shuny.common.util;

import com.shu.shuny.common.exception.BizException;

import java.util.Locale;

/**
 * @author jsen.yin[jsen.yin@gmail.com]
 * 2019-10-29
 * @Description: <p></p>
 */
public final class PropertyName {

    /**
     * 工具类禁止公开构造
     */
    private PropertyName() {
    }

    public static String methodToProperty(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else if (name.startsWith("get") || name.startsWith("set")) {
            name = name.substring(3);
        } else {
            throw new BizException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
        }

        if (name.length() == 1 || (name.length() > 1 && !Character.isUpperCase(name.charAt(1)))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }
        return name;
    }

}
