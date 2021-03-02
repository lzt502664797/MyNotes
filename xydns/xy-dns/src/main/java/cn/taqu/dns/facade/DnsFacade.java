package cn.taqu.dns.facade;

import cn.taqu.dns.core.DnsCacheManager;
import cn.taqu.dns.remote.DnsRemoteParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * dns 门面  将dns操作 转化到指定type的dns 缓存操作 ，
 * 将dns 返回值 DnsCacheEntity转化为 InetAddress[] 等核心操作
 * lookupAllHostAddr 为自定义解析过程：
 * 先判断自定义解析器是否开启 若未开启则抛出异常使用默认解析器
 * 然后判定dns是否为可解析的类型，再判自定义解析该dns缓存是否开启，开启则调用 getInetAddresses 进行解析操作。
 * 缓存未开启则直接使用远程解析
 * getInetAddresses 是自定义解析方法：
 * 若存在本地缓存，则直接使用；若没有，则进行域名解析，解析成功则将其加入本地缓存当中。
 * @author pengchen
 * @date 2021/01/06
 */
public interface DnsFacade {
    Logger logger = LoggerFactory.getLogger(DnsFacade.class);

    /**
     * 自定义dns解析过程
     *
     * @param dns dns
     * @return {@link InetAddress[]}* @throws UnknownHostException 未知主机异常
     */
    default InetAddress[] lookupAllHostAddr(String dns) throws UnknownHostException {


        // 判断解析器是否开启
        if (!isOpen()) {
            logger.warn("自定义解析器未开启，不可用，请先在 Etcd 中配置");
            throw new UnknownHostException(" DnsFacade is closed ");
        }

        // 判断自定义解析器是否支持该dns类型 若不支持则抛出异常使用默认解析器进行解析
        String type = supportTypes(dns);
        if (type == null) {
            logger.debug("DnsFacade can not support {} ,default namespace will parse it ", dns);
            throw new UnknownHostException(" can not support this type " + dns);
        }

        // 判断该dns的缓存是否开启 若未开启则进行远程解析
        if (!isDnsUseCache(dns)) {
            logger.warn("Dns {} 缓存未开启，不可用", dns);
            return DnsRemoteParser.getDefaultCache(dns);
        }

        // 解析dns
        InetAddress[] result;
        result = getInetAddresses(dns, type);
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
    default InetAddress[] getInetAddresses(String dns, String type) {

        InetAddress[] result;
        result = DnsCacheManager.getCache(dns, type);
        if (result == null) {
            result = DnsRemoteParser.getDefaultCache(dns);
            if (result != null) {
                DnsCacheManager.putCache(dns, result, type);
            }
        }
        return result;
    }

    /**
     * 支持的types
     *
     * @param dns dns
     * @return {@link String} null 表示 不支持
     */
    default String supportTypes(String dns) {
        return null;
    }


    /**
     * 开关
     *
     * @return boolean
     */
    default boolean isOpen() {
        return true;
    }

    /**
     * 判断 dns 是否开启缓存
     * @param dns
     * @return
     */
    default boolean isDnsUseCache(String dns){
        return true;
    }


}