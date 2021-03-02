package cn.taqu.dns.facade;

import cn.taqu.dns.util.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 从系统 properties 中 获取 supportTypes 。 PropertiesDnsFacade 是个单例
 * supportTypes 判断该dns是否为可解析类型
 * @author LinZhengTao
 * @date 2021/01/13
 */
public class PropertiesDnsFacade implements DnsFacade {

    final static Logger logger = LoggerFactory.getLogger(PropertiesDnsFacade.class);

    /**
     * 支持的types
     *
     * @param dns dns
     * @return {@link String} null 表示 不支持
     */
    @Override
    public String supportTypes(String dns) {
        return PropertiesUtil.getDnsTypeMap().get(dns);
    }

}