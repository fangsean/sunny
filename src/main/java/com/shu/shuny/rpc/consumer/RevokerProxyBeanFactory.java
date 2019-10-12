package com.shu.shuny.rpc.consumer;



import com.shu.shuny.cluster.engine.ClusterStrategyEngine;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.model.ProviderServiceMeta;
import com.shu.shuny.model.SunnyRequest;
import com.shu.shuny.model.SunnyResponse;
import com.shu.shuny.registry.ConsumerIRegisterCenter;
import com.shu.shuny.registry.impl.ZkRegisterCenter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class RevokerProxyBeanFactory implements InvocationHandler {

    private ExecutorService fixedThreadPool = null;
    /**
     * 服务接口
     */
    private Class<?> targetInterface;

    /**
     * 超时时间
     */
    private int consumeTimeout;

    /**
     * 调用者线程数
     */
    private static int threadWorkerNumber = 10;

    /**
     * 负载均衡策略
     */
    private String clusterStrategy;


    public RevokerProxyBeanFactory(Class<?> targetInterface, int consumeTimeout, String clusterStrategy) {
        this.targetInterface = targetInterface;
        this.consumeTimeout = consumeTimeout;
        this.clusterStrategy = clusterStrategy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 服务接口名称
        String serviceKey = targetInterface.getName();
        // 获取某个接口的服务提供者列表
        ConsumerIRegisterCenter zkRegisterCenter = ZkRegisterCenter.singleton();
        List<ProviderServiceMeta> providerServices =
            ((ZkRegisterCenter) zkRegisterCenter).getServiceMetaDataMapWithConsume().get(serviceKey);
        // 根据软负载策略,从服务提供者列表选取本次调用的服务提供者
        ProviderServiceMeta providerService =
            ClusterStrategyEngine.doSelectByStrategyName(clusterStrategy, providerServices);
        // 复制一份服务提供者信息
        ProviderServiceMeta newProvider = providerService.copy();
        // 设置本次调用服务的方法以及接口
        newProvider.setServiceMethod(method);
        newProvider.setServiceInterface(targetInterface);

        // 声明调用SunnyRequest对象,SunnyRequest表示发起一次调用所包含的信息
        final SunnyRequest request = new SunnyRequest();
        // 设置本次调用的唯一标识
        request.setUniqueKey(UUID.randomUUID().toString() + "-" + Thread.currentThread().getId());
        // 设置本次调用的服务提供者信息
        request.setProviderService(newProvider);
        // 设置本次调用的超时时间
        request.setInvokeTimeout(consumeTimeout);
        // 设置本次调用的方法名称
        request.setInvokedMethodName(method.getName());
        // 设置本次调用的方法参数信息
        request.setArgs(args);

        try {
            //构建用来发起调用的线程池
            if (fixedThreadPool == null) {
                synchronized (RevokerProxyBeanFactory.class) {
                    if (null == fixedThreadPool) {
                        fixedThreadPool = Executors.newFixedThreadPool(threadWorkerNumber);
                    }
                }
            }
            // 根据服务提供者的ip,port,构建InetSocketAddress对象,标识服务提供者地址
            String serverIp = request.getProviderService().getServerIp();
            int serverPort = request.getProviderService().getServerPort();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(serverIp, serverPort);
            // 提交本次调用信息到线程池fixedThreadPool,发起调用
            Future<SunnyResponse> responseFuture =
                fixedThreadPool.submit(RevokerServiceCallable.of(inetSocketAddress, request));
            // 获取调用的返回结果
            SunnyResponse response = responseFuture.get(request.getInvokeTimeout(), TimeUnit.MILLISECONDS);
            if (response != null) {
                return response.getResult();
            }
        } catch (Exception e) {
            throw new BizException(e);
        }
        return null;
    }


    public Object getProxy() {
        return Proxy
            .newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] {targetInterface}, this);
    }


    private static volatile RevokerProxyBeanFactory singleton;

    public static RevokerProxyBeanFactory singleton(Class<?> targetInterface, int consumeTimeout,
        String clusterStrategy) {
        if (null == singleton) {
            synchronized (RevokerProxyBeanFactory.class) {
                if (null == singleton) {
                    singleton = new RevokerProxyBeanFactory(targetInterface, consumeTimeout, clusterStrategy);
                }
            }
        }
        return singleton;
    }


}
