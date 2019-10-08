package com.shu.shuny.serialization.serializer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shu.shuny.common.annotation.SerializeType;
import com.shu.shuny.common.enumeration.SerializeTypeEnum;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.serialization.serializer.Serializer;
import com.shu.shuny.common.util.AssertUtils;


import java.text.SimpleDateFormat;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/27 11:27
 */
@SerializeType(type = SerializeTypeEnum.JSON_SERIALIZER)
public class JSONSerializer implements Serializer {
    private static final String DEFAULT_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";

    private static ObjectMapper mapper =
        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public <T> byte[] serialize(T obj) {
        AssertUtils.checkNull(obj);
        try {
            return mapper.writeValueAsString(obj).getBytes();
        } catch (JsonProcessingException e) {
            throw new BizException(e);
        }

    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        AssertUtils.checkNull(data);
        mapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
        try {
            return mapper.readValue(data, clazz);
        } catch (Exception e) {
            throw new BizException(e);
        }
    }
}
