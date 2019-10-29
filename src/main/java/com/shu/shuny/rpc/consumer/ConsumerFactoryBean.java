package com.shu.shuny.rpc.consumer;


import com.shu.shuny.model.InvokerService;
import com.shu.shuny.model.ProviderServiceMeta;
import com.shu.shuny.registry.ConsumerIRegisterCenter;
import com.shu.shuny.registry.impl.ZkRegisterCenter;
import com.shu.shuny.rpc.netty.client.NettyChannelPoolFactory;
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
    /**用集合对象封存*/
    private InvokerService invoker;

    @Override
    public Object getObject() {
        return invoker.getServiceImpl();
    }

    @Override
    public Class<?> getObjectType() {
        return invoker.getServiceInterface();
    }

    @Override
    public boolean isSingleton() {
        return Boolean.TRUE;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ConsumerIRegisterCenter zkRegisterCenter = ZkRegisterCenter.singleton();
        zkRegisterCenter.initProviderMap(invoker.getServiceKey(), invoker.getVersion());
        Map<String, List<ProviderServiceMeta>> providerMap = zkRegisterCenter.getServiceMetaDataMapWithConsume();
        if (MapUtils.isEmpty(providerMap)) {
            throw new IllegalArgumentException("service provider list is empty.");
        }
        NettyChannelPoolFactory.channelPoolFactoryInstance().initChannelPoolFactory(providerMap);

        //获取服务提供者代理对象  重点  方法拦截代理
        RevokerProxyBeanFactory proxyFactory = RevokerProxyBeanFactory.singleton(
                invoker.getServiceInterface(), invoker.getTimeout(), invoker.getClusterStrategy());
        this.invoker.setServiceImpl(proxyFactory.getProxy());
        //将消费者信息注册到注册中心
        zkRegisterCenter.registerInvoker(this.invoker);
    }
}
