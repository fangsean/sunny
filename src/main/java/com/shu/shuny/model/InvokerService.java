package com.shu.shuny.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.reflect.Method;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvokerService implements Serializable {

    /**服务接口*/
    private Class<?> serviceInterface;
    private Method serviceMethod;
    private String invokerIp;
    private int invokerPort;

    /**
     * 超时时间
     */
    private long timeout;

    /**
     * 服务实现
     */
    private Object serviceImpl;

    /**
     * 负载均衡策略
     */
    private String clusterStrategy;

    /**
     * 服务提供者唯一标识
     */
    private String serviceKey;

    /**服务分组组名*/
    private String version = "v1";

}
