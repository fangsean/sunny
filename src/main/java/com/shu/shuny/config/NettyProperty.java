package com.shu.shuny.config;

import com.shu.shuny.common.annotation.PropertyCaption;
import lombok.Data;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/30 18:09
 */
@Data
public class NettyProperty {
    @PropertyCaption("shu.channel.connect.size")
    private Integer channelConnectSize;
    @PropertyCaption("shu.channel.serialize.type")
    private String serializeType;

}
