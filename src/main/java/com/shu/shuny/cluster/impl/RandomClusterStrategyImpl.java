package com.shu.shuny.cluster.impl;

import com.shu.shuny.cluster.ClusterStrategy;
import com.shu.shuny.model.ProviderServiceMeta;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

/**
 * @Author:shucq
 * @Description: 随机选取
 * @Date 2019/10/9 15:18
 */
public class RandomClusterStrategyImpl implements ClusterStrategy {
    @Override
    public ProviderServiceMeta doSelect(List<ProviderServiceMeta> providerServices) {
        int length = providerServices.size();
        int index = RandomUtils.nextInt(0, length - 1);
        return providerServices.get(index);
    }


}
