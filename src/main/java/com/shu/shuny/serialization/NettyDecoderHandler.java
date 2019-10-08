package com.shu.shuny.serialization;

import com.shu.shuny.common.enumeration.SerializeTypeEnum;
import com.shu.shuny.serialization.engine.SerializerEngine;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;


/**
 * @Author:shucq
 * @Description:
 * @Date 2019/10/2 18:59
 */
public class NettyDecoderHandler extends ByteToMessageDecoder {

    //解码对象class
    private Class<?> genericClass;
    //解码对象编码所使用序列化类型
    private SerializeTypeEnum serializeType;

    public NettyDecoderHandler(Class<?> genericClass, SerializeTypeEnum serializeType) {
        this.genericClass = genericClass;
        this.serializeType = serializeType;
    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int dataLength = in.readableBytes();
        //读取完整的消息体字节数组
        byte[] data = new byte[dataLength];
        in.readBytes(data);
        //将字节数组反序列化为java对象(SerializerEngine参考序列化与反序列化章节)
        Object obj = SerializerEngine.deserialize(data, genericClass, serializeType.getSerializeType());
        out.add(obj);
    }

}
