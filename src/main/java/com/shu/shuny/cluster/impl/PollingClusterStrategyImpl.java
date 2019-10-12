package com.shu.shuny.cluster.impl;

import com.shu.shuny.cluster.ClusterStrategy;
import com.shu.shuny.model.ProviderServiceMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/10/9 15:37
 */
@Slf4j
public class PollingClusterStrategyImpl implements ClusterStrategy {
    private int index = 0;
    private Lock lock = new ReentrantLock();

    @Override
    public ProviderServiceMeta doSelect(List<ProviderServiceMeta> providerServices) {

        ProviderServiceMeta service = null;
        try {
            lock.tryLock(10, TimeUnit.MILLISECONDS);
            //若计数大于服务提供者个数,将计数器归0
            if (index >= providerServices.size()) {
                index = 0;
            }
            service = providerServices.get(index);
            index++;
        } catch (Exception e) {
            log.warn("{}", e);
        } finally {
            lock.unlock();
        }

        //兜底,保证程序健壮性,若未取到服务,则直接取第一个
        if (service == null) {
            service = providerServices.get(0);
        }
        return service;
    }

    @Override
    public String getName() {
        return "PollingCluster";
    }
}
