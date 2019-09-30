package com.shu.shuny.serialization.engine;

import com.google.common.collect.Maps;

import com.shu.shuny.common.annotation.SerializeType;
import com.shu.shuny.common.enumeration.SerializeTypeEnum;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.serialization.serializer.Serializer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/28 10:50
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SerializerEngine {
    public static final Map<SerializeTypeEnum, Serializer> serializerMap = Maps.newConcurrentMap();

    static {
        ServiceLoader<Serializer> serializers = ServiceLoader.load(Serializer.class);
        Iterator<Serializer> iterator = serializers.iterator();
        while (iterator.hasNext()) {
            Serializer serializer = iterator.next();
            SerializeType serializeType = serializer.getClass().getAnnotation(SerializeType.class);
            serializerMap.put(serializeType.type(), serializer);
        }
    }

    public static <T> byte[] serialize(T obj, String serializeType) {
        Serializer serializer = getSerializerType(serializeType);

        try {
            return serializer.serialize(obj);
        } catch (Exception e) {
            throw new BizException(e);
        }
    }



    /**
     * 反序列化
     *
     * @param data
     * @param clazz
     * @param serializeType
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz, String serializeType) {
        Serializer serializer = getSerializerType(serializeType);
        try {
            return serializer.deserialize(data, clazz);
        } catch (Exception e) {
            throw new BizException(e);
        }
    }

    private static Serializer getSerializerType(String serializeType) {
        SerializeTypeEnum serialize = SerializeTypeEnum.queryByType(serializeType);
        if (serialize == null) {
            throw new BizException("serialize is null");
        }

        Serializer serializer = serializerMap.get(serialize);
        if (serializer == null) {
            throw new BizException("serialize error");
        }
        return serializer;
    }

}
