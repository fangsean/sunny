package com.shu.shuny.cluster.impl;

import com.shu.shuny.cluster.ClusterStrategy;
import com.shu.shuny.model.ProviderServiceMeta;

import java.util.List;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/10/9 15:21
 */
public class WeightPollingClusterStrategyImpl implements ClusterStrategy {

    @Override
    public ProviderServiceMeta doSelect(List<ProviderServiceMeta> providerServices) {
        //TODO 待实现
        return null;
    }

    @Override
    public String getName() {
        return "WeightPolling";
    }


}
