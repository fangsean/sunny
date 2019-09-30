package com.shu.shuny.serialization.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.shu.shuny.common.annotation.SerializeType;
import com.shu.shuny.common.enumeration.SerializeTypeEnum;
import com.shu.shuny.serialization.serializer.Serializer;
import com.shu.shuny.serialization.util.AssertUtils;


/**
 * 采用阿里巴巴 json 序列化
 *
 * @Author:shucq
 * @Description:
 * @Date 2019/9/27 11:50
 */
@SerializeType(type = SerializeTypeEnum.JSON_2_SERIALIZER)
public class JSON2Serializer implements Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        AssertUtils.checkNull(obj);
        return JSON.toJSONString(obj, SerializerFeature.WriteDateUseDateFormat).getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        AssertUtils.checkNull(data);
        return JSON.parseObject(new String(data), clazz);
    }

}
