package cn.taqu.dns.util;

import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.promises.EtcdResponsePromise;
import mousio.etcd4j.responses.EtcdKeysResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author linzt
 * @Date 2021年2月18日 0018
 */
public class EtcdListenerUtil {

    private static String etcdLocate;
    private final static Logger logger =  LoggerFactory.getLogger(EtcdListenerUtil.class);
    private static EtcdClient client;
    private static String etcdKey;
    private final static String STABLE = "stableType";
    private final static String UNSTABLE = "unStableType";

    /**
     * dns 解析开关
     */
    public static HashMap<String,Boolean> dnsPropertiesMap = new HashMap<>();

    /**
     * dns 类型
     */
    public static HashMap<String,String> dnsTypeMap = new HashMap<>();

    static {
        LocalDateTime beginTime = LocalDateTime.now();

        // 从application.yml文件获取项目运行环境
        String env = GetYmlPropertiesUtil.getCommonYml("spring.profiles.active");

        // 根据项目环境拼接处application文件名
        String filename = "application-" + env + ".yml";

        // 从相应application文件中获取etcd的地址
        etcdLocate = GetYmlPropertiesUtil.getCommonYml("etcd.url",filename);

        client = new EtcdClient(URI.create(etcdLocate));

        etcdKey = GetYmlPropertiesUtil.getCommonYml("dns.etcd");

        Long opetime = Duration.between(beginTime,LocalDateTime.now()).toMillis();
        logger.info("静态加载获取etcd工具类配置用时为： {}ms",opetime);
    }

    /**
     * 将从etcd上获取的配置进行赋值
     * @param nodes
     */
    private void setConfig(List<EtcdKeysResponse.EtcdNode> nodes) throws Exception {

        for (int i = 0; i < nodes.size(); i++) {
            // 非目录时 添加至本地
            if (!nodes.get(i).dir){
                String key = getKey(nodes.get(i).key);
                // 放入本地存储
                dnsPropertiesMap.put(key, nodes.get(i).getValue().equals("true"));
            } else {
                // 获取子节点配置
                List<EtcdKeysResponse.EtcdNode> dirNodes = client.getDir(nodes.get(i).key).consistent().send().get().node.nodes;
                setConfig(dirNodes);
            }
        }
    }

    /**
     * 切割字符串获取 key
     * 若 key 为某类型dns 如
     * /antispam_v2/dns/unStableType/j2.test.k8s.taqu.cn
     * 则将 j2.test.k8s.taqu.cn 放入 dnsTypeMap，vaule为 unStableType
     * 若类型为 stableType  则 vaule 为 stableType
     * @param etcdKey
     * @return
     */
    public String getKey(String etcdKey){
        String[] split = etcdKey.split("/");
        // 获取dns类型
        String isType = split[split.length - 2];
        if(isType.equals(STABLE)){
            dnsTypeMap.put(split[split.length - 1],STABLE);
        }else if (isType.equals(UNSTABLE)){
            dnsTypeMap.put(split[split.length - 1],UNSTABLE);
        }
        return split[split.length - 1];
    }

    public void startListener() throws Exception{
        this.startListenerThread(client , etcdKey);
    }

    /**
     * 获取指定目录的 EtcdKeysResponse
     * 使用 getAllKeyStartThread 方法进行遍历所有key
     * @param client
     * @param dir
     * @throws Exception
     */
    public void startListenerThread(EtcdClient client, String dir) throws Exception {
        // dns目录下配置项
        EtcdKeysResponse dataTree3 = client.getDir(dir).consistent().send().get();
        setConfig(dataTree3.node.nodes);

        // 线程工具类创建线程
        ThreadPoolUtil.getExecutorThreadService().execute(() -> {
            startListener(client, dir, dataTree3.etcdIndex + 1);
        });

    }


    /**
     * 加载配置项，然后监听等待 etcd
     * 当 etcd 发生变化时，更换新的监听器
     * @param client
     * @param dir
     * @param waitIndex
     */
    public void startListener(final EtcdClient client , final String dir , long waitIndex)  {
        EtcdResponsePromise<EtcdKeysResponse> promise = null;
        try {
            // 如果监听的waitIndex 不存在与key对应的EventHistroy 中（currentIndex >= waitIndex） ，
            // 并且key对应的modifyIndex > waitIndex , 则会查找到第一个大于waitIndex 的数据进行展示
            promise = client.getDir(dir).recursive().waitForChange(waitIndex).consistent().send();

            promise.addListener(promise1 -> {
                try {
                    // 更新配置项
                    updateConfig(promise1.get().node);
                    System.out.println("node change");
                    new Thread(() -> {startListener(client , dir , waitIndex + 1);}).start();
                } catch (Exception e) {
                    logger.error("获取etcd配置出现错误: {}", e.getMessage());
                }
            });

        } catch (IOException e) {
            logger.error("监听etcd配置发生异常: {}", e.getMessage());
        }

    }

    /**
     * 更新 etcd 中发生变化的配置
     * @param node
     */
    private void updateConfig(EtcdKeysResponse.EtcdNode node){
        String[] split = node.key.split("/");
        String dns = split[split.length - 1];
        dnsPropertiesMap.put(dns,node.value.equals("true"));
    }


}
