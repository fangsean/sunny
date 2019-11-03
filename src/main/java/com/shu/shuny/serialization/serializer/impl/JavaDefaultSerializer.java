package com.shu.shuny.serialization.serializer.impl;


import com.shu.shuny.common.annotation.SerializeType;
import com.shu.shuny.common.enumeration.SerializeTypeEnum;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.serialization.serializer.Serializer;
import com.shu.shuny.common.util.AssertUtils;
import org.apache.commons.lang.SerializationException;

import java.io.*;

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
        byte[] bytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            bytes = bos.toByteArray();
        } catch (Exception e) {
            throw new SerializationException(e);
        }finally {
            try {
                bos.flush();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        AssertUtils.checkNull(data);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream input = new ObjectInputStream(bis)) {
            return (T) input.readObject();
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }
}
