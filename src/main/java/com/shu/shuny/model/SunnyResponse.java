package com.shu.shuny.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/10/2 18:49
 */
@Data
public class SunnyResponse implements Serializable {
    //UUID,唯一标识一次返回值
    private String uniqueKey;
    //客户端指定的服务超时时间
    private long invokeTimeout;
    //接口调用返回的结果对象
    private Object result;
}
