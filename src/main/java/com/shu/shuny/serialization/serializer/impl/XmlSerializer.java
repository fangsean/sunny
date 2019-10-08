package com.shu.shuny.serialization.serializer.impl;


import com.shu.shuny.common.annotation.SerializeType;
import com.shu.shuny.common.enumeration.SerializeTypeEnum;
import com.shu.shuny.serialization.serializer.Serializer;
import com.shu.shuny.common.util.AssertUtils;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.springframework.stereotype.Service;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/27 11:43
 */
@SerializeType(type = SerializeTypeEnum.XML_SERIALIZER)
@Service
public class XmlSerializer implements Serializer {

    private static final XStream xStream = new XStream(new DomDriver());

    @Override
    public <T> byte[] serialize(T obj) {
        AssertUtils.checkNull(obj);
        return xStream.toXML(obj).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        AssertUtils.checkNull(data);
        return (T) xStream.fromXML(new String(data));
    }
}
