package com.shu.shuny.serialization.serializer.impl;


import com.shu.shuny.common.annotation.SerializeType;
import com.shu.shuny.common.enumeration.SerializeTypeEnum;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.serialization.serializer.Serializer;
import com.shu.shuny.common.util.AssertUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * java 自带序列化方式序列化
 *
 * @Author:shucq
 * @Description:
 * @Date 2019/9/27 10:42
 */
@SerializeType(type = SerializeTypeEnum.JAVA_DEFAULT_SERIALIZER)
public class JavaDefaultSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        AssertUtils.checkNull(obj);
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new BizException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        AssertUtils.checkNull(data);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream input = new ObjectInputStream(bis)) {
            return (T) input.readObject();
        } catch (Exception e) {
            throw new BizException(e);
        }
    }
}
