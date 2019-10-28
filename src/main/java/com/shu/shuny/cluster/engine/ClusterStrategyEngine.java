package com.shu.shuny.cluster.engine;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shu.shuny.cluster.ClusterStrategy;
import com.shu.shuny.cluster.impl.RandomClusterStrategyImpl;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.model.ProviderServiceMeta;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/10/12 16:24
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClusterStrategyEngine {
    private static final Map<String, ClusterStrategy> clusterStrategyMap = Maps.newConcurrentMap();
    private static final List<String> clusterStrategyNameList = Lists.newArrayList();
    private static final ClusterStrategy defaultStrategy = new RandomClusterStrategyImpl();

    static {
        ServiceLoader<ClusterStrategy> serializers = ServiceLoader.load(ClusterStrategy.class);
        Iterator<ClusterStrategy> iterator = serializers.iterator();
        while (iterator.hasNext()) {
            ClusterStrategy clusterStrategy = iterator.next();
            clusterStrategyMap.put(clusterStrategy.getName(), clusterStrategy);
            clusterStrategyNameList.addAll(clusterStrategyMap.keySet());
        }
    }

    public static ProviderServiceMeta doSelectByStrategyName(String name, List<ProviderServiceMeta> providerServices) {
        Assert.notEmpty(providerServices,"services is empty.");
        ProviderServiceMeta result = findClusterStrategyByName(name).doSelect(providerServices);
        if (result != null) {
            return result;
        }
        // 补偿
        return providerServices.get(0);
    }

    private static ClusterStrategy findClusterStrategyByName(String name) {
        //  取默认
        if (StringUtils.isBlank(name)) {
            return defaultStrategy;
        }
        if (!clusterStrategyNameList.contains(name)) {
            throw new BizException("负载均衡策略名配置错误");
        }
        return clusterStrategyMap.get(name);
    }
}
