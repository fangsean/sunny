package com.shu.shuny.cluster;

import com.shu.shuny.model.ProviderServiceMeta;

import java.util.List;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/10/9 15:16
 * TODO :待优化
 */
public interface ClusterStrategy {

    ProviderServiceMeta doSelect(List<ProviderServiceMeta> providerServices);

    String getName();
}
