package com.shu.shuny.serialization.serializer.impl;


import com.shu.shuny.common.annotation.SerializeType;
import com.shu.shuny.common.enumeration.SerializeTypeEnum;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.serialization.serializer.Serializer;
import org.apache.thrift.TBase;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TCompactProtocol;
import org.springframework.stereotype.Service;


/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/27 18:00
 */
@SerializeType(type = SerializeTypeEnum.THRIFT_SERIALIZER)
@Service
public class ThriftSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T obj) {
        try {
            if (!(obj instanceof TBase)) {
                throw new BizException("不支持这种类型");
            }
            TSerializer serializer = new TSerializer(new TCompactProtocol.Factory());
            return serializer.serialize((TBase) obj);
        } catch (TException e) {
            throw new BizException(e);
        }
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            if (!TBase.class.isAssignableFrom(cls)) {
                throw new BizException("不支持这种类型");
            }
            TBase result = (TBase) cls.newInstance();
            TDeserializer tDeserializer = new TDeserializer(new TCompactProtocol.Factory());
            tDeserializer.deserialize(result, data);
            return (T) result;
        } catch (Exception e) {
            throw new BizException(e);
        }

    }


}
