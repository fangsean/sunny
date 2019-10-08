package com.shu.shuny.registry;

import com.shu.shuny.model.ProviderServiceMeta;

import java.util.List;
import java.util.Map;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/30 17:59
 */
public interface ProviderIRegisterCenter {

    /**
     * @param serviceMetaData
     */

    void registerProvider(final List<ProviderServiceMeta> serviceMetaData);


    /**
     * 服务端获取服务提供者信息
     *
     * @return 返回对象, Key:服务提供者接口  value:服务提供者服务方法列表
     */
    public Map<String, List<ProviderServiceMeta>> getProviderServiceMap();



}
