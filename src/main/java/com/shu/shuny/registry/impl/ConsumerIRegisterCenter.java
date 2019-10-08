package com.shu.shuny.registry.impl;

import com.shu.shuny.model.InvokerService;
import com.shu.shuny.model.ProviderServiceMeta;

import java.util.List;
import java.util.Map;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/10/2 21:03
 */
public interface ConsumerIRegisterCenter {


    void initProviderMap(String serviceKey, String version);


    /**
     * 消费端获取服务提供者信息
     *
     * @return
     */
    Map<String, List<ProviderServiceMeta>> getServiceMetaDataMapWithConsume();

    void registerInvoker(InvokerService invoker);

}
