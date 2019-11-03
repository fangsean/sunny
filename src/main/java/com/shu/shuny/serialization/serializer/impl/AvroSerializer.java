package com.shu.shuny.serialization.serializer.impl;

import com.shu.shuny.common.annotation.SerializeType;
import com.shu.shuny.common.enumeration.SerializeTypeEnum;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.serialization.serializer.Serializer;
import org.apache.avro.io.*;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.commons.lang.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/28 9:16
 */
@SerializeType(type = SerializeTypeEnum.AVRO_SERIALIZER)
public class AvroSerializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        byte[] bytes;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            if (!(obj instanceof SpecificRecordBase)) {
                throw new BizException("不支持这种类型");
            }
            DatumWriter userDatumWriter = new SpecificDatumWriter(obj.getClass());
            BinaryEncoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);
            userDatumWriter.write(obj, binaryEncoder);
            bytes = outputStream.toByteArray();
            outputStream.flush();
        } catch (Exception e) {
            throw new SerializationException(e);
        }finally {
            try {
                //关闭
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        T obj;
        try {
            if (!SpecificRecordBase.class.isAssignableFrom(clazz)) {
                throw new BizException("不支持这种类型");
            }
            DatumReader userDatumReader = new SpecificDatumReader(clazz);
            BinaryDecoder binaryDecoder =
                DecoderFactory.get().directBinaryDecoder(new ByteArrayInputStream(data), null);
            obj = (T) userDatumReader.read(clazz.newInstance(), binaryDecoder);
        } catch (Exception e) {
            throw new SerializationException(e);
        }
        return obj;
    }
}
