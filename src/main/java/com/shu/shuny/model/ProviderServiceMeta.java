package com.shu.shuny.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/30 17:48
 */
@Data
public class ProviderServiceMeta implements Serializable {
    private Class<?> serviceInterface;
    private transient Object serviceImpl;
    @JsonIgnore private transient Method serviceMethod;
    private String serverIp;
    private int serverPort;
    private long timeout;
    //该服务提供者权重
    private int weight;
    //服务端线程数
    private int workerThreads;
    //服务提供者唯一标识/
    private String serviceKey;
    //服务版本号
    private String version;


    public ProviderServiceMeta copy() {
        ProviderServiceMeta providerService = new ProviderServiceMeta();
        providerService.setServiceInterface(serviceInterface);
        providerService.setServiceImpl(serviceImpl);
        providerService.setServiceMethod(serviceMethod);
        providerService.setServerIp(serverIp);
        providerService.setServerPort(serverPort);
        providerService.setTimeout(timeout);
        providerService.setWeight(weight);
        providerService.setWorkerThreads(workerThreads);
        providerService.setServiceKey(serviceKey);
        providerService.setVersion(version);
        return providerService;
    }
}
