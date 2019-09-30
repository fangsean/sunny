package com.shu.shuny.common.enumeration;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/30 16:55
 */
@Getter
public enum SerializeTypeEnum {
    JAVA_DEFAULT_SERIALIZER("JavaDefaultSerializer"),
    HESSIAN_SERIALIZER("HessianSerializer"),
    JSON_SERIALIZER("JSONSerializer"),
    JSON_2_SERIALIZER("JSON2Serializer"),

    PROTO_STUFF_SERIALIZER("ProtoStuffSerializer"),
    XML_SERIALIZER("XmlSerializer"),
    MARSHALLING_SERIALIZER("MarshallingSerializer"),
    AVRO_SERIALIZER("AvroSerializer"),
    PROTOCOL_BUFFER_SERIALIZER("ProtocolBufferSerializer"),
    THRIFT_SERIALIZER("ThriftSerializer");

    private String serializeType;

    private SerializeTypeEnum(String serializeType) {
        this.serializeType = serializeType;
    }


    public static SerializeTypeEnum queryByType(String serializeType) {
        if (StringUtils.isBlank(serializeType)) {
            return null;
        }
        for (SerializeTypeEnum serialize : SerializeTypeEnum.values()) {
            if (StringUtils.equals(serializeType, serialize.getSerializeType())) {
                return serialize;
            }
        }
        return null;
    }}
