package com.shu.shuny.model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class SunnyResponseWrapper {

    //存储返回结果的阻塞队列
    private BlockingQueue<SunnyResponse> responseQueue = new ArrayBlockingQueue<SunnyResponse>(1);
    //结果返回时间
    private long responseTime;

    /**
     * 计算该返回结果是否已经过期
     *
     * @return
     */
    public boolean isExpire() {
        SunnyResponse response = responseQueue.peek();
        if (response == null) {
            return false;
        }

        long timeout = response.getInvokeTimeout();
        if ((System.currentTimeMillis() - responseTime) > timeout) {
            return true;
        }
        return false;
    }

    public static SunnyResponseWrapper of() {
        return new SunnyResponseWrapper();
    }

    public BlockingQueue<SunnyResponse> getResponseQueue() {
        return responseQueue;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }
}
