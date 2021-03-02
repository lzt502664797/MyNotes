package cn.taqu.dns.facade;

import cn.taqu.dns.remote.DnsRemoteParser;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 默认DNSFacade
 * @Description: properties和Etcd都没有开启，则使用默认facade
 * @Author linzt
 * @Date 2021年1月15日 0015
 */
public class DefaultFacade implements DnsFacade{
    @Override
    public InetAddress[] lookupAllHostAddr(String dns) throws UnknownHostException {
        // 解析dns
        InetAddress[] result;
        result = getInetAddresses(dns, "");
        if (result == null) {
            logger.warn(" can not find {}" + dns);
            throw new UnknownHostException(" can not find {}" + dns);
        }
        logger.debug("DnsFacade get dns {}  successfully", dns);
        return result;
    }

    /**
     * 获取dns 对应的 InetAddress[]
     * 1. 从 DnsCacheManager 中获取
     * 2. 无 DnsRemoteParser 中获取 ，并put DnsCacheManager
     *
     * @param dns  dns
     * @param type 类型
     * @return {@link InetAddress[]}* @throws UnknownHostException 未知主机异常
     */
    @Override
    public InetAddress[] getInetAddresses(String dns, String type) {
        return DnsRemoteParser.getDefaultCache(dns);
    }
}
