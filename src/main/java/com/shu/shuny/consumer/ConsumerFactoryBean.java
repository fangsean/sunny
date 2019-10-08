package com.shu.shuny.consumer;


import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.model.InvokerService;
import com.shu.shuny.model.ProviderServiceMeta;
import com.shu.shuny.registry.impl.ConsumerIRegisterCenter;
import com.shu.shuny.registry.impl.ZkRegisterCenter;
import lombok.Data;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;



/**
 * @Author:shucq
 * @Description:
 * @Date 2019/10/2 19:09
 */
@Data
public class ConsumerFactoryBean implements FactoryBean, InitializingBean {
    /**
     * 服务接口
     */
    private Class<?> targetInterface;

    /**
     * 超时时间
     */
    private int timeout;

    /**
     * 服务实现
     */
    private Object serviceImpl;

    /**
     * 负载均衡策略
     */
    private String clusterStrategy;

    /**
     * 服务提供者唯一标识
     */
    private String serviceKey;

    //服务分组组名
    private String version = "v1";

    @Override
    public Object getObject() throws Exception {
        return serviceImpl;
    }

    @Override
    public Class<?> getObjectType() {
        return targetInterface;
    }

    @Override
    public boolean isSingleton() {
        return Boolean.TRUE;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ConsumerIRegisterCenter zkRegisterCenter = ZkRegisterCenter.singleton();
        zkRegisterCenter.initProviderMap(serviceKey, version);
        Map<String, List<ProviderServiceMeta>> providerMap = zkRegisterCenter.getServiceMetaDataMapWithConsume();
        if (MapUtils.isEmpty(providerMap)) {
            throw new BizException("service provider list is empty.");
        }
        NettyChannelPoolFactory.channelPoolFactoryInstance().initChannelPoolFactory(providerMap);

        //获取服务提供者代理对象  重点  方法拦截代理
        RevokerProxyBeanFactory proxyFactory =
            RevokerProxyBeanFactory.singleton(targetInterface, timeout, clusterStrategy);
        this.serviceImpl = proxyFactory.getProxy();
        //将消费者信息注册到注册中心
        InvokerService invoker = new InvokerService();
        invoker.setServiceInterface(targetInterface);
        invoker.setServiceKey(serviceKey);
        invoker.setVersion(version);
        zkRegisterCenter.registerInvoker(invoker);
    }
}
