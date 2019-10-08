package com.shu.shuny.serialization;

import com.shu.shuny.common.enumeration.SerializeTypeEnum;
import com.shu.shuny.serialization.engine.SerializerEngine;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * @Author:shucq
 * @Description:
 * @Date 2019/10/2 18:59
 */
public class NettyEncoderHandler extends MessageToByteEncoder {
    //序列化类型
    private SerializeTypeEnum serializeType;

    public NettyEncoderHandler(SerializeTypeEnum serializeType) {
        this.serializeType = serializeType;
    }

    @Override
    public void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
        //将对象序列化为字节数组
        byte[] data = SerializerEngine.serialize(in, serializeType.getSerializeType());
        //写入序列化后得到的字节数组
        out.writeBytes(data);
    }
}
