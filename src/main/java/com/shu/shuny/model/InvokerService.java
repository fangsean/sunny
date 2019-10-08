package com.shu.shuny.model;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;


@Data
public class InvokerService implements Serializable {

    private Class<?> serviceInterface;
    private Object serviceImpl;
    private Method serviceMethod;
    private String invokerIp;
    private int invokerPort;
    private long timeout;
    //服务提供者唯一标识
    private String serviceKey;
    //服务分组组名
    private String version = "v1";

}
