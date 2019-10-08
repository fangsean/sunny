package com.shu.shuny.registry;


import com.shu.shuny.common.util.PropertyConfigeHelper;
import com.shu.shuny.model.ZkProperties;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;



/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/30 18:23
 */
public class CuratorZookeeperTransporter {
    private static ZkProperties zkProperties = PropertyConfigeHelper.getZkProperties();

    public static ZkClient getZkClient() {
        return CuratorZookeeperClientInner.client;
    }

    private static class CuratorZookeeperClientInner {
        private static ZkClient client =
              new ZkClient(zkProperties.getService(), zkProperties.getSessionTimeout(), zkProperties.getConnectionTimeout(), new SerializableSerializer());

    }
}
