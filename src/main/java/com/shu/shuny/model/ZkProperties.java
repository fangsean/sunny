package com.shu.shuny.model;

import com.shu.shuny.common.annotation.PropertyCaption;
import lombok.Data;
import lombok.ToString;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/30 18:04
 */

@Data
@ToString
public class ZkProperties {
    @PropertyCaption("shu.zk.service") private String service;
    @PropertyCaption("shu.zk.sessionTimeout") private Integer sessionTimeout;
    @PropertyCaption("shu.zk.connectionTimeout") private Integer connectionTimeout;

    public Integer getSessionTimeout() {
        if (sessionTimeout == null) {
            return 50000;
        }
        return sessionTimeout;
    }

    public Integer getConnectionTimeout() {
        if (connectionTimeout == null) {
            return 30000;
        }
        return connectionTimeout;

    }
}
