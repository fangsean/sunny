package com.shu.shuny.cluster.impl;

import com.shu.shuny.cluster.ClusterStrategy;
import com.shu.shuny.model.ProviderServiceMeta;
import com.shu.shuny.spring.ClientFactoryBeanDefinitionParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author:shucq
 * @Description: 权重随机
 * @Date 2019/10/9 15:47
 */
@Slf4j
public class WeightRandomClusterStrategyImpl implements ClusterStrategy {
    @Override
    public ProviderServiceMeta doSelect(List<ProviderServiceMeta> providerServices) {
        int m = 0;
        int weight = 0;
        ProviderServiceMeta meta = null;
        //计算总权重
        int weightSum = providerServices.stream().mapToInt(serv -> serv.getWeight()).sum();
        //取值 n=[0,weightSum)
        int n = RandomUtils.nextInt(0, weightSum);
        for (ProviderServiceMeta serv : providerServices) {
            //先计算weight 权重值小的选重率越小，反之，权重值大的选重率越高
            weight += serv.getWeight();
            /*根据随机权重分区*/
            if (n >= m && n < m + weight) {
                meta = serv;
                break;
            }
        }
        return meta;
    }

    @Override
    public String getName() {
        return "WeightRandom";
    }
}
