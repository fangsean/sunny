package com.shu.shuny.serialization.serializer;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/27 10:39
 */
public interface Serializer {

    /**
     * 序列化
     *
     * @param obj
     * @param <T>
     * @return
     */
    <T> byte[] serialize(T obj);

    /**
     * 反序列化
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] data, Class<T> clazz);

}
