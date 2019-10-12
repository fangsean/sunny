package com.shu.shuny.cluster.impl;

import com.shu.shuny.cluster.ClusterStrategy;
import com.shu.shuny.common.util.IPHelper;
import com.shu.shuny.model.ProviderServiceMeta;

import java.util.List;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/10/9 15:35
 */
public class HashClusterStrategyImpl implements ClusterStrategy {
    @Override
    public ProviderServiceMeta doSelect(List<ProviderServiceMeta> providerServices) {
        String localIP = IPHelper.localIp();
        int hashCode = localIP.hashCode();
        int size = providerServices.size();
        return providerServices.get(hashCode % size);
    }

    @Override
    public String getName() {
        return "HashCluster";
    }
}
