package cn.taqu.dns.check;

import cn.taqu.dns.check.strategy.CheckStrategy;
import cn.taqu.dns.core.DnsCacheEntity;
import cn.taqu.dns.core.DnsCacheManager;
import cn.taqu.dns.remote.DnsRemoteParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 稳定类型检查策略
 * 检查时长为 20s
 * differentDns 用于检查dns解析缓存并更新
 * remoteDnsNoResponse 用于检查dns服务器是否工作
 * @author LinZhengTao
 * @date 2021/01/08
 */
public class StableTypeCheckStrategy implements CheckStrategy {
    private static final Logger logger = LoggerFactory.getLogger(StableTypeCheckStrategy.class);

    @Override
    public long checkTime() {
        return 20000L;
    }

    /**
     * 检查dns解析缓存并更新:
     * 从 DnsCacheManager 中获取mysql缓存中获取所有域名
     * 将所有域名重新解析并检查解析结果是否相同
     * 若不同，则更新缓存并返回 false
     * 出现dns解析出错，记录错误日志，抛出 UnknownHostException 异常
     * 返回为 true 表示检查dns解析结果与缓存中相同
     * 返回为 false 表示检查dns解析结果与缓存中不同
     * @return
     */
    @Override
    public boolean differentDns() throws UnknownHostException {

        // 获取type类型缓存
        ConcurrentHashMap<String, DnsCacheEntity> mysql = DnsCacheManager.typeCaches.get("stableType");

        // 将所有域名重新解析并更新缓存数据
        for (String dns : mysql.keySet()) {

            InetAddress[] newAddress = DnsRemoteParser.getDefaultCache(dns);
            if(newAddress == null){
                logger.error("域名{} 解析失败，请查看是否配置正常",dns);
                throw new UnknownHostException();
            }

            // 解析结果不为null 且 解析结果发生了变化时 加入缓存
            if (!Arrays.equals(newAddress, mysql.get(dns).getInetAddresses())) {
                DnsCacheManager.putCache(dns, newAddress, "mysql");
                logger.warn("dns：{} 域名发生了变更,重新获取解析并更新缓存 ", dns);
                return true;
            }
        }

        return false;
    }

    /**
     * 检查dns服务器是否工作:
     * 当调用 differentDns 解析dns出错时，使用本方法检查dns服务器
     * 若有dns解析成功，说明dns服务器没有问题，返回true
     * 若都失败则说明dns服务器出现问题 返回 false
     * @return
     */
    @Override
    public boolean checkRemoteDnsServer() {

        // 获取mysql类型缓存
        ConcurrentHashMap<String, DnsCacheEntity> mysql = DnsCacheManager.typeCaches.get("stableType");

        for (String dns : mysql.keySet()) {

            // 当只有一个dns时，对其做三次检查
            if(mysql.size() == 1){
                for (int i = 0; i < 3; i++) {
                    if(DnsRemoteParser.getDefaultCache(dns) != null){
                        return true;
                    }
                }
            }

            InetAddress[] newAddress = DnsRemoteParser.getDefaultCache(dns);

            // 若有dns解析成功，说明dns服务器没有问题，返回true
            if (newAddress != null){
                return true;
            }
        }
        return false;
    }
}
