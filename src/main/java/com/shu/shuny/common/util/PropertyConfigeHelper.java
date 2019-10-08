package com.shu.shuny.common.util;

import com.alibaba.fastjson.JSON;
import com.shu.shuny.common.annotation.PropertyCaption;
import com.shu.shuny.common.enumeration.SerializeTypeEnum;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.model.NettyProperty;
import com.shu.shuny.model.ZkProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/30 18:59
 */
public class PropertyConfigeHelper {
    private static final Logger logger = LoggerFactory.getLogger(PropertyConfigeHelper.class);
    private static ZkProperties zkProperties = null;
    private static NettyProperty nettyProperty = null;
    private static final String PROPERTY_CLASSPATH = "/sunny.properties";

    static {
        Properties properties = new Properties();
        try (InputStream is = PropertyConfigeHelper.class.getResourceAsStream(PROPERTY_CLASSPATH)) {
            if (is == null) {
                throw new BizException("sunny.properties.properties can not found in the classpath");
            }
            properties.load(is);
            zkProperties = new ZkProperties();
            nettyProperty = new NettyProperty();
            readProperty2Object(properties, zkProperties);
            readProperty2Object(properties, nettyProperty);
        } catch (Exception e) {
            throw new BizException(e);
        }
    }

    public static ZkProperties getZkProperties() {
        return zkProperties;
    }

    public static NettyProperty getNettyProperty() {
        return nettyProperty;
    }

    public static SerializeTypeEnum getSerializeType() {
        return SerializeTypeEnum.queryByType(nettyProperty.getSerializeType());
    }

    private static void readProperty2Object(Properties properties, Object targetDo) {
        Class classParameter = targetDo.getClass();
        Field[] fields = classParameter.getDeclaredFields();
        for (Field field : fields) {
            PropertyCaption propertyCaption = field.getAnnotation(PropertyCaption.class);
            if (propertyCaption != null) {
                String key = propertyCaption.value();
                Object value = properties.get(key);
                if (value != null) {
                    field.setAccessible(true);
                    try {
                        Class<?> type = field.getType();
                        if (!StringUtils.equals(type.getSimpleName(), String.class.getSimpleName())) {
                            value = JSON.parseObject(value.toString(), type);
                        }
                        System.out.println(value);
                        field.set(targetDo, value);
                        field.setAccessible(false);
                    } catch (Exception e) {
                        logger.error("trans properties(key={}) error", key);
                    }
                }
            }
        }
    }

    public static int getChannelConnectSize() {
        return nettyProperty.getChannelConnectSize();
    }
}
