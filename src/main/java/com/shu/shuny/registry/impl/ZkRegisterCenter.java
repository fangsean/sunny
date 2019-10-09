package com.shu.shuny.registry.impl;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.shu.shuny.common.Constant;
import com.shu.shuny.common.exception.BizException;
import com.shu.shuny.common.util.IPHelper;
import com.shu.shuny.model.InvokerService;
import com.shu.shuny.model.ProviderServiceMeta;
import com.shu.shuny.registry.ConsumerIRegisterCenter;
import com.shu.shuny.registry.ProviderIRegisterCenter;
import com.shu.shuny.registry.ZookeeperClient;
import org.I0Itec.zkclient.IZkChildListener;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * @Author:shucq
 * @Description:
 * @Date 2019/9/30 18:12
 */
public class ZkRegisterCenter extends ConsumerRegisterCenter
    implements ProviderIRegisterCenter, ConsumerIRegisterCenter {
    ZookeeperClient zkClient = ZookeeperClient.getInstance();
    //服务提供者列表,Key:服务提供者接口  value:服务提供者服务方法列表
    private static final Map<String, List<ProviderServiceMeta>> providerServiceMap = Maps.newConcurrentMap();
    // 服务提供者标致
    public static final String PROVIDER_TYPE = "provider";
    private static final String ROOT_PATH = "/config_register";
    private static ZkRegisterCenter registerCenter = new ZkRegisterCenter();

    public static ZkRegisterCenter singleton() {
        return registerCenter;
    }

    @Override
    public void registerProvider(List<ProviderServiceMeta> serviceMetaData) {
        if (CollectionUtils.isEmpty(serviceMetaData)) {
            return;
        }
        // 按照接口分组
        groupingByServiceMetaData(serviceMetaData);
        String serviceKey = serviceMetaData.get(0).getServiceKey();
        String zkPath = ROOT_PATH + Constant.SLASH + serviceKey;
        boolean exists = zkClient.isExists(zkPath);
        // 创建持久节点 -- 父节点不存在则创建父节点
        if (!exists) {
            zkClient.createPersistent(zkPath, true);
        }
        // 一个 一个的注册
        providerServiceMap.forEach(this::doRegister);
    }



    private void doRegister(String serviceInterfaceName, List<ProviderServiceMeta> providerServiceMetas) {
        String zkPath = ROOT_PATH + Constant.SLASH + providerServiceMetas.get(0).getServiceKey();
        //服务分组
        String version = providerServiceMetas.get(0).getVersion();
        //创建服务提供者
        String serviceNode = serviceInterfaceName;
        String servicePath =
            zkPath + Constant.SLASH + version + Constant.SLASH + serviceNode + Constant.SLASH + PROVIDER_TYPE;
        boolean exist = zkClient.isExists(servicePath);
        if (!exist) {
            zkClient.createPersistent(servicePath, true);
        }
        //创建当前服务器节点
        int serverPort = providerServiceMetas.get(0).getServerPort();//服务端口
        int weight = providerServiceMetas.get(0).getWeight();//服务权重
        int workerThreads = providerServiceMetas.get(0).getWorkerThreads();//服务工作线程
        String localIp = IPHelper.localIp();
        String currentServiceIpNode =
            servicePath + Constant.SLASH + localIp + "|" + serverPort + "|" + weight + "|" + workerThreads + "|"
                + version;
        exist = zkClient.isExists(currentServiceIpNode);
        if (!exist) {
            //注意,这里创建的是临时节点
            zkClient.createEphemeral(currentServiceIpNode);
        }

        zkClient.subscribeChildChanges(servicePath, this::processListener);



    }

    @Override
    public Map<String, List<ProviderServiceMeta>> getProviderServiceMap() {
        return providerServiceMap;
    }



    private void groupingByServiceMetaData(List<ProviderServiceMeta> serviceMetaData) {
        synchronized (ZkRegisterCenter.class) {
            for (ProviderServiceMeta provider : serviceMetaData) {
                String serviceItfKey = provider.getServiceInterface().getName();
                List<ProviderServiceMeta> providers = providerServiceMap.get(serviceItfKey);
                if (providers == null) {
                    providers = Lists.newArrayList();
                }
                providers.add(provider);
                providerServiceMap.put(serviceItfKey, providers);
            }
        }


    }

    private void processListener(String parentPath, List<String> currentChilds) {
        if (currentChilds == null) {
            currentChilds = Lists.newArrayList();
        }
        //存活的服务IP列表
        List<String> activityServiceIpList =
            Lists.newArrayList(Lists.transform(currentChilds, input -> StringUtils.split(input, "|")[0]));
        refreshActivityService(activityServiceIpList);
    }

    //  更新缓存
    private void refreshActivityService(List<String> activityServiceIpList) {
        providerServiceMap.clear();
        if (CollectionUtils.isEmpty(activityServiceIpList)) {
            return;
        }
        ConcurrentHashMap<String, List<ProviderServiceMeta>> ctivity =
            providerServiceMap.values().stream().flatMap(Collection::stream)
                .filter(e -> activityServiceIpList.contains(e.getServerIp())).collect(
                Collectors.groupingBy(e -> e.getServiceInterface().getName(), ConcurrentHashMap::new, toList()));
        providerServiceMap.putAll(ctivity);
    }


    @Override
    protected Map<String, List<ProviderServiceMeta>> fetchOrUpdateServiceMetaData(String serviceKey, String version) {
        final Map<String, List<ProviderServiceMeta>> providerServiceMapResult = Maps.newConcurrentMap();
        //从ZK获取服务提供者列表
        String providePath = ROOT_PATH + Constant.SLASH + serviceKey + Constant.SLASH + version;
        List<String> providerServices = zkClient.getChildren(providePath);
        for (String serviceName : providerServices) {
            String servicePath = providePath + Constant.SLASH + serviceName + Constant.SLASH + PROVIDER_TYPE;
            List<String> ipPathList = zkClient.getChildren(servicePath);
            for (String ipPath : ipPathList) {
                String serverIp = StringUtils.split(ipPath, "|")[0];
                String serverPort = StringUtils.split(ipPath, "|")[1];
                int weight = Integer.parseInt(StringUtils.split(ipPath, "|")[2]);
                int workerThreads = Integer.parseInt(StringUtils.split(ipPath, "|")[3]);
                String group = StringUtils.split(ipPath, "|")[4];
                List<ProviderServiceMeta> providerServiceList = providerServiceMap.get(serviceName);
                if (providerServiceList == null) {
                    providerServiceList = Lists.newArrayList();
                }
                ProviderServiceMeta providerService = new ProviderServiceMeta();
                try {
                    providerService.setServiceInterface(ClassUtils.getClass(serviceName));
                } catch (ClassNotFoundException e) {
                    throw new BizException(e);
                }
                providerService.setServerIp(serverIp);
                providerService.setServerPort(Integer.parseInt(serverPort));
                providerService.setWeight(weight);
                providerService.setWorkerThreads(workerThreads);
                providerService.setVersion(group);
                providerServiceList.add(providerService);
                providerServiceMapResult.put(serviceName, providerServiceList);
            }

            //监听注册服务的变化,同时更新数据到本地缓存
            zkClient.subscribeChildChanges(servicePath, new IZkChildListener() {
                @Override
                public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                    if (currentChilds == null) {
                        currentChilds = Lists.newArrayList();
                    }
                    currentChilds =
                        Lists.newArrayList(Lists.transform(currentChilds, input -> StringUtils.split(input, "|")[0]));
                    refreshServiceMetaDataMap(currentChilds);
                }
            });
        }
        return providerServiceMapResult;
    }


    @Override
    public void registerInvoker(InvokerService invoker) {
        if (invoker == null) {
            return;
        }
        //创建 ZK命名空间/当前部署应用APP命名空间/
        boolean exist = zkClient.isExists(ROOT_PATH);
        if (!exist) {
            zkClient.createPersistent(ROOT_PATH, true);
        }


        //创建服务消费者节点
        String remoteAppKey = invoker.getServiceKey();
        String groupName = invoker.getVersion();
        String serviceNode = invoker.getServiceInterface().getName();
        String servicePath =
            ROOT_PATH + Constant.SLASH + remoteAppKey + Constant.SLASH + groupName + Constant.SLASH + serviceNode
                + Constant.SLASH + INVOKER_TYPE;
        exist = zkClient.isExists(servicePath);
        if (!exist) {
            zkClient.createPersistent(servicePath, true);
        }

        //创建当前服务器节点
        String localIp = IPHelper.localIp();
        String currentServiceIpNode = servicePath + Constant.SLASH + localIp;
        exist = zkClient.isExists(currentServiceIpNode);
        if (!exist) {
            //注意,这里创建的是临时节点
            zkClient.createEphemeral(currentServiceIpNode);
        }
    }

}


