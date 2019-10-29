package com.shu.shuny.common.reflect;

/**
 * @author jsen.yin[jsen.yin@gmail.com]
 * 2019-10-29
 * @Description: <p></p>
 */
public interface ReflectorFactory {

    /**
     * @return
     */
    boolean isClassCacheEnabled();

    /**
     * @param classCacheEnabled
     */
    void setClassCacheEnabled(boolean classCacheEnabled);

    /**
     * @param type
     * @return
     */
    Reflector findForClass(Class<?> type);

}
