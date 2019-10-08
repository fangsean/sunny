package com.shu.shuny.provider;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/10/2 18:12
 */
import com.google.common.collect.Lists;
import com.shu.shuny.common.util.IPHelper;
import com.shu.shuny.model.ProviderServiceMeta;
import com.shu.shuny.registry.impl.ZkRegisterCenter;
import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.Method;
import java.util.List;

@Data
public class ProviderFactoryBean implements FactoryBean, InitializingBean {

    /**
     * 服务接口
     */
    private Class<?> serviceInterface;

    /**
     * 服务实现
     */
    private Object serviceImpl;
    /**
     * 服务端口
     */
    private String serverPort;

    /**
     * 服务超时时间
     */
    private long timeout;
    /**
     * 服务代理对象,暂时没有用到
     */
    private Object serviceProxyObject;

    /**
     * 服务提供者唯一标识
     */
    private String serviceKey;

    /**
     * 服务分组组名(版本号)
     */
    private String version = "v1";
    /**
     * 服务提供者权重,默认为1 ,范围为1-100
     */

    private int weight = 1;

    /**
     * 服务端线程数,默认10个线程
     */
    private int workerThreads = 10;

    @Override
    public Object getObject() throws Exception {
        return serviceProxyObject;
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //  step 1 启动 netty
        NettyServer.singleton().start(Integer.parseInt(serverPort));
        List<ProviderServiceMeta> providerServiceList = buildProviderServiceInfos();
        // 注册 服务
        ZkRegisterCenter zkRegisterCenter = ZkRegisterCenter.singleton();
        zkRegisterCenter.registerProvider(providerServiceList);
    }

    private List<ProviderServiceMeta> buildProviderServiceInfos() {
        List<ProviderServiceMeta> providerList = Lists.newArrayList();
        Method[] methods = serviceImpl.getClass().getDeclaredMethods();
        for (Method method : methods) {
            ProviderServiceMeta providerService = new ProviderServiceMeta();
            providerService.setServiceInterface(serviceInterface);
            providerService.setServiceImpl(serviceImpl);
            providerService.setServerIp(IPHelper.localIp());
            providerService.setServerPort(Integer.parseInt(serverPort));
            providerService.setTimeout(timeout);
            providerService.setServiceMethod(method);
            providerService.setWeight(weight);
            providerService.setWorkerThreads(workerThreads);
            providerService.setServiceKey(serviceKey);
            providerService.setVersion(version);
            providerList.add(providerService);
        }
        return providerList;
    }

}
