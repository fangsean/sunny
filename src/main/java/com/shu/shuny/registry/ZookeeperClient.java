package com.shu.shuny.registry;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/30 21:40
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZookeeperClient {

    private static ZookeeperClient zookeeperClient = new ZookeeperClient();
    // 客户端可选择为  Curator
    ZkClient framework = CuratorZookeeperTransporter.getZkClient();

    public boolean isExists(String path) {
        return framework.exists(path);
    }

    public static ZookeeperClient getInstance() {
        return zookeeperClient;
    }

    public void createPersistent(String path, boolean createParents) {
        framework.createPersistent(path, createParents);
    }

    public void createEphemeral(String path) {
        framework.createEphemeral(path);
    }


    public void subscribeChildChanges(String path, IZkChildListener listener) {
        framework.subscribeChildChanges(path, listener);
    }

    public List<String> getChildren(String providePath) {
        return framework.getChildren(providePath);
    }
}
