package cn.taqu.dns.util;

import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.requests.EtcdKeyGetRequest;
import mousio.etcd4j.responses.EtcdKeysResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;


/**
 * etcd工具类
 * 提供获取Etcd类型dns解析配置的方法
 * @author LinZhengTao
 * @date 2021/01/13
 */
public class EtcdUtil {

    private static String etcdLocate;
    private final static Logger logger =  LoggerFactory.getLogger(EtcdUtil.class);
    private static EtcdClient client;

    static {
        LocalDateTime beginTime = LocalDateTime.now();

        // 从application.yml文件获取项目运行环境
        String env = GetYmlPropertiesUtil.getCommonYml("spring.profiles.active");
        // 根据项目环境拼接处application文件名
        String filename = "application-" + env + ".yml";
        // 从相应application文件中获取etcd的地址
        etcdLocate = GetYmlPropertiesUtil.getCommonYml("etcd.url",filename);
        client = new EtcdClient(URI.create(etcdLocate));

        Long opetime = Duration.between(beginTime,LocalDateTime.now()).toMillis();
        logger.info("静态加载获取etcd工具类配置用时为： {}ms",opetime);
    }

    /**
     * 获取Etcd中是否有dns配置，以判断使用哪种环境获取缓存配置
     * @param key
     * @return
     */
    public static String getKey(String key){
        LocalDateTime beginTime = LocalDateTime.now();

        try {
            EtcdKeysResponse etcdKeysResponse = client.get(key).send().get();
            Long opetime = Duration.between(beginTime,LocalDateTime.now()).toMillis();
            logger.info("getKey用时为： {}ms",opetime);
            return etcdKeysResponse.node.value;
        } catch (Exception e) {
            logger.warn("{} Etcd配置获取失败",key);
        }


        return "";
    }

    /**
     * 查看该key是否存在
     * @param key
     * @return
     */
    public static EtcdKeyGetRequest getRequest(String key){
        return client.get(key);
    }

    /**
     * 将dns添加至etcd
     * @param key
     * @param value
     */
    public static void putDnsToEtcd(String key,String value){
        LocalDateTime beginTime = LocalDateTime.now();
        try {
            client.put(key,value).send().get();
            logger.warn("将新的DNS:{} 添加至ETCD成功 ", key);
            Long opetime = Duration.between(beginTime,LocalDateTime.now()).toMillis();
            logger.info("添加至etcd用时为： {}ms",opetime);
        } catch (Exception e) {
            logger.warn("添加新的DNS:{} 至 ETCD 失败，{}", key, e);
        }
    }

    public static boolean getIsOpen(){
        return true;
    }
}
