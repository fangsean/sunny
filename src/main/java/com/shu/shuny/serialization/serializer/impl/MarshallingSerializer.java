package com.shu.shuny.serialization.serializer.impl;


import com.shu.shuny.common.annotation.SerializeType;
import com.shu.shuny.common.enumeration.SerializeTypeEnum;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.serialization.serializer.Serializer;
import com.shu.shuny.common.util.MarshallingCodecFactory;
import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.Unmarshaller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/27 17:46
 */
@SerializeType(type = SerializeTypeEnum.MARSHALLING_SERIALIZER)
public class MarshallingSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Marshaller marshaller = MarshallingCodecFactory.buildMarshalling();
            marshaller.start(Marshalling.createByteOutput(byteArrayOutputStream));
            marshaller.writeObject(obj);
            marshaller.finish();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new BizException(e);
        }

    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        try {
            final Unmarshaller unmarshaller = MarshallingCodecFactory.buildUnMarshalling();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            unmarshaller.start(Marshalling.createByteInput(byteArrayInputStream));
            Object object = unmarshaller.readObject();
            unmarshaller.finish();
            return (T) object;
        } catch (Exception e) {
            throw new BizException(e);
        }

    }
}
