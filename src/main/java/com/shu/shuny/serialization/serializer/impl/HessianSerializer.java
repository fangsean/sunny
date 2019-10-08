package com.shu.shuny.serialization.serializer.impl;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.shu.shuny.common.annotation.SerializeType;
import com.shu.shuny.common.enumeration.SerializeTypeEnum;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.serialization.serializer.Serializer;
import com.shu.shuny.common.util.AssertUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            HessianOutput ho = new HessianOutput(bos);
            ho.writeObject(obj);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new BizException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        AssertUtils.checkNull(data);
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
            HessianInput input = new HessianInput(bis);
            return (T) input.readObject();
        } catch (Exception e) {
            throw new BizException(e);
        }
    }
}
