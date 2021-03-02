package cn.taqu.dns.facade;

import cn.taqu.dns.util.EtcdListenerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Etcd dns facade , 监听etcd ，获取 open 属性 ，获取 supportTypes EtcdDnsFacade 是个单例
 * isOpen 通过自定义ETCD工具类进行获取该dns是否开启自定义解析
 * changeCacheStatue 修改缓存的状态
 * supportTypes 判断该dns是否为可解析类型
 * isSupport 从ETCD中获取某类型dns所需要包含的字段，以此确定域名是否属于该类型。
 * @author LinZhengTao
 * @date 2021/01/13
 */
public class EtcdDnsFacade implements DnsFacade {

    final static Logger logger = LoggerFactory.getLogger(EtcdDnsFacade.class);

    public static boolean isOpen = false;
    private final static String OPEN_KEY = "isOpenFacade";

    /**
     * 判断自定义解析器是否开启
     * @return boolean
     */
    @Override
    public boolean isOpen() {

        // 判断该Facade是否打开
        Boolean isFacadeOpen = EtcdListenerUtil.dnsPropertiesMap.get(OPEN_KEY);
        if(isFacadeOpen == null || !isFacadeOpen){
            return false;
        }

        return true;

    }

    @Override
    public String supportTypes(String dns) {
        return EtcdListenerUtil.dnsTypeMap.get(dns);
    }

    @Override
    public boolean isDnsUseCache(String dns) {
        Boolean isDnsOpen = EtcdListenerUtil.dnsPropertiesMap.get(dns);
        if(isDnsOpen == null || !isDnsOpen){
            return false;
        }
        return true;
    }
}