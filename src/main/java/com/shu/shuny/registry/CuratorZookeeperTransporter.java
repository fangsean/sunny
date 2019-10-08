package com.shu.shuny.registry;


import com.shu.shuny.common.util.PropertyConfigerHelper;
import com.shu.shuny.config.ZkProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;



/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/30 18:23
 */
@NoArgsConstructor(access= AccessLevel.PRIVATE)
public class CuratorZookeeperTransporter {
    private static ZkProperties zkProperties = PropertyConfigerHelper.getZkProperties();

    public static ZkClient getZkClient() {
        return CuratorZookeeperClientInner.client;
    }

    private static class CuratorZookeeperClientInner {
        private static ZkClient client =
              new ZkClient(zkProperties.getService(), zkProperties.getSessionTimeout(), zkProperties.getConnectionTimeout(), new SerializableSerializer());

    }
}
