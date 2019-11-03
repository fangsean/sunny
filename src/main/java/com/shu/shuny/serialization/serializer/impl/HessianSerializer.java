package com.shu.shuny.serialization.serializer.impl;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.shu.shuny.common.annotation.SerializeType;
import com.shu.shuny.common.enumeration.SerializeTypeEnum;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.serialization.serializer.Serializer;
import com.shu.shuny.common.util.AssertUtils;
import org.apache.commons.lang.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/27 11:10
 */
@SerializeType(type = SerializeTypeEnum.HESSIAN_SERIALIZER)
public class HessianSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        AssertUtils.checkNull(obj);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            HessianOutput ho = new HessianOutput(bos);
            ho.writeObject(obj);
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
        return bos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        AssertUtils.checkNull(data);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
            HessianInput input = new HessianInput(bis);
            return (T) input.readObject();
        } catch (Exception e) {
            throw new SerializationException(e);
        }
    }
}
