package cn.taqu.dns.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Properties;

/**
 * @Description:
 * @Author linzt
 * @Date 2021年1月14日 0014
 */
public class GetYmlPropertiesUtil {

    private final static Logger logger =  LoggerFactory.getLogger(GetYmlPropertiesUtil.class);
    private static Resource resource;

    /**
     * 获取yml文件某个key
     * @param key
     * @return
     */
    public static String getCommonYml(Object key,String fileName){
        resource = new ClassPathResource(fileName);
        Properties properties = null;
        try {
            YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
            yamlFactory.setResources(resource);
            properties =  yamlFactory.getObject();
        } catch (Exception e) {
            logger.warn("自定义读取yml文件时出错，文件名为{}。 {}",fileName,e);
            return null;
        }

        Object res = properties.get(key);
        // 若dns.env.key返回的值为null，则使用默认的facade
        if(res == null && key.toString().equals("dns.env")){
            return "default";
        }
        return res.toString();
    }

    /**
     * 默认从 application。yml中获取
     * @param key
     * @return
     */
    public static String getCommonYml(Object key){
        return getCommonYml(key,"application.yml");
    }
}
