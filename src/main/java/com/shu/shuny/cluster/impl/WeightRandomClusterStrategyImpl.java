package com.shu.shuny.cluster.impl;

import com.shu.shuny.cluster.ClusterStrategy;
import com.shu.shuny.model.ProviderServiceMeta;

import java.util.List;

/**
 * @Author:shucq
 * @Description: 权重随机
 * @Date 2019/10/9 15:47
 */
public class WeightRandomClusterStrategyImpl implements ClusterStrategy {
    @Override
    public ProviderServiceMeta doSelect(List<ProviderServiceMeta> providerServices) {
        //TODO 待实现
        return null;
    }

    @Override
    public String getName() {
        return "WeightRandom";
    }
}
