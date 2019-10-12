package com.shu.shuny.rpc.netty.server;


import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.shu.shuny.model.ProviderServiceMeta;
import com.shu.shuny.model.SunnyRequest;
import com.shu.shuny.model.SunnyResponse;
import com.shu.shuny.registry.impl.ZkRegisterCenter;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/10/2 18:45
 */
@ChannelHandler.Sharable
@Slf4j
public class NettyServerInvokeHandler extends SimpleChannelInboundHandler<SunnyRequest> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerInvokeHandler.class);

    //服务端限流
    private static final Map<String, Semaphore> serviceKeySemaphoreMap = Maps.newConcurrentMap();

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("Netty Server Invoke Handler error(msg={})",cause.getMessage());

        //发生异常,关闭链路
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SunnyRequest request) throws Exception {

        if (ctx.channel().isWritable()) {
            //从服务调用对象里获取服务提供者信息
            ProviderServiceMeta metaDataModel = request.getProviderService();
            long consumeTimeOut = request.getInvokeTimeout();
            final String methodName = request.getInvokedMethodName();

            //根据方法名称定位到具体某一个服务提供者
            String serviceKey = metaDataModel.getServiceInterface().getName();
            //获取限流工具类
            int workerThread = metaDataModel.getWorkerThreads();
            Semaphore semaphore = serviceKeySemaphoreMap.get(serviceKey);
            if (semaphore == null) {
                synchronized (serviceKeySemaphoreMap) {
                    semaphore = serviceKeySemaphoreMap.get(serviceKey);
                    if (semaphore == null) {
                        semaphore = new Semaphore(workerThread);
                        serviceKeySemaphoreMap.put(serviceKey, semaphore);
                    }
                }
            }
            //获取注册中心服务
            ZkRegisterCenter registerCenter = ZkRegisterCenter.singleton();
            List<ProviderServiceMeta> localProviderCaches = registerCenter.getProviderServiceMap().get(serviceKey);
            Object result = null;
            boolean acquire = false;
            try {
                ProviderServiceMeta localProviderCache = Collections2.filter(localProviderCaches,
                    input -> StringUtils.equals(input.getServiceMethod().getName(), methodName)).iterator().next();
                Object serviceObject = localProviderCache.getServiceImpl();
                //利用反射发起服务调用
                Method method = localProviderCache.getServiceMethod();
                //利用semaphore实现限流
                acquire = semaphore.tryAcquire(consumeTimeOut, TimeUnit.MILLISECONDS);
                if (acquire) {
                    result = method.invoke(serviceObject, request.getArgs());
                }
            } catch (Exception e) {
                result = e;
            } finally {
                if (acquire) {
                    semaphore.release();
                }
            }

            //根据服务调用结果组装调用返回对象
            SunnyResponse response = new SunnyResponse();
            response.setInvokeTimeout(consumeTimeOut);
            response.setUniqueKey(request.getUniqueKey());
            response.setResult(result);

            //将服务调用返回对象回写到消费端
            ctx.writeAndFlush(response);

        } else {
            logger.error("------------channel closed!---------------");
        }


    }
}
