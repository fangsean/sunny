package com.shu.shuny.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/10/2 18:47
 */
@Data
public class SunnyRequest  implements Serializable {

    //UUID,唯一标识一次返回值
    private String uniqueKey;
    //服务提供者信息
    private ProviderServiceMeta providerService;
    //调用的方法名称
    private String invokedMethodName;
    //传递参数
    private Object[] args;
    //消费端应用名
    private String appName;
    //消费请求超时时长
    private long invokeTimeout;
}
