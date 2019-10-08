package com.shu.shuny.rpc.consumer;

import com.google.common.collect.Maps;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.model.SunnyResponse;
import com.shu.shuny.model.SunnyResponseWrapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class RevokerResponseHolder {

    //服务返回结果Map
    private static final Map<String, SunnyResponseWrapper> responseMap = Maps.newConcurrentMap();
    //清除过期的返回结果
    private static final ExecutorService removeExpireKeyExecutor = Executors.newSingleThreadExecutor();

    static {
        //删除超时未获取到结果的key,防止内存泄露
        removeExpireKeyExecutor.execute(() -> {
            while (true) {
                try {
                    for (Map.Entry<String, SunnyResponseWrapper> entry : responseMap.entrySet()) {
                        boolean isExpire = entry.getValue().isExpire();
                        if (isExpire) {
                            responseMap.remove(entry.getKey());
                        }
                        Thread.sleep(10);
                    }
                } catch (Exception e) {
                    log.warn("{}", e);
                }

            }
        });
    }

    /**
     * 初始化返回结果容器,requestUniqueKey唯一标识本次调用
     *
     * @param requestUniqueKey
     */
    public static void initResponseData(String requestUniqueKey) {
        responseMap.put(requestUniqueKey, SunnyResponseWrapper.of());
    }


    /**
     * 将Netty调用异步返回结果放入阻塞队列
     *
     * @param response
     */
    public static void putResultValue(SunnyResponse response) {
        long currentTime = System.currentTimeMillis();
        SunnyResponseWrapper responseWrapper = responseMap.get(response.getUniqueKey());
        responseWrapper.setResponseTime(currentTime);
        responseWrapper.getResponseQueue().add(response);
        responseMap.put(response.getUniqueKey(), responseWrapper);
    }


    /**
     * 从阻塞队列中获取Netty异步返回的结果值
     *
     * @param requestUniqueKey
     * @param timeout
     * @return
     */
    public static SunnyResponse getValue(String requestUniqueKey, long timeout) {
        SunnyResponseWrapper responseWrapper = responseMap.get(requestUniqueKey);
        try {
            return responseWrapper.getResponseQueue().poll(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new BizException(e);
        } finally {
            responseMap.remove(requestUniqueKey);
        }
    }


}
