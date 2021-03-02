package cn.taqu.dns.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;


/**
 * Properties 工具类
 * 提供获取 Properties 文件中域名类型的方法
 * @author LinZhengTao
 * @date 2021/01/13
 */
public class PropertiesUtil {
    private static Properties prop = new Properties();
    private final static Logger logger =  LoggerFactory.getLogger(PropertiesUtil.class);
    private static String propertiesLocate;
    private static HashMap<String,String> dnsTypeMap = new HashMap<>();

    static {
        // 从配yml文件获取properties文件地址
        propertiesLocate =  GetYmlPropertiesUtil.getCommonYml("dns.value");

        setDnsType("stableType");
        setDnsType("unStableType");

    }

    /**
     * 获取配置的域名类型
     * stableType和 unStableType
     * 通过分割 “, ” 获取域名
     * @param type
     */
    private static void setDnsType(String type){

        String unStableType = getPropType(type);
        String[] split = unStableType.split(", ");

        for (int i = 0; i < split.length; i++) {
            dnsTypeMap.put(split[i],type);
        }
    }

    /**
     * 获取Properties文件中某个配置
     * @param properties
     * @return
     */
    public static String getPropType(String properties){
        try {
            prop.load(new FileInputStream(propertiesLocate));
        } catch (IOException e) {
            logger.warn("properties 文件读取失败 {}",e);
        }
        String res = prop.getProperty(properties);
        return res;
    }

    public static HashMap<String, String> getDnsTypeMap() {
        return dnsTypeMap;
    }
}
