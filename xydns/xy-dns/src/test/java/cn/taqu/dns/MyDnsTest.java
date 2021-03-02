package cn.taqu.dns;

import cn.taqu.dns.check.StableTypeTask;
import cn.taqu.dns.core.DnsCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author linzt
 * @Date 2021年1月8日 0008
 */
public class MyDnsTest {
    static {
        java.security.Security.setProperty("networkaddress.cache.ttl", "0");
        java.security.Security.setProperty("networkaddress.cache.negative.ttl", "0");
        //sun.net.spi.nameservice.provider.1
        System.setProperty("sun.net.spi.nameservice.provider.1", "dns,XYDnsNameService");
        System.setProperty("sun.net.spi.nameservice.provider.2", "default");
    }

    final static Logger logger = LoggerFactory.getLogger(MyDnsTest.class);

    public static void main(String[] args) throws UnknownHostException {
        testFeatures();
//        StableTypeTask stableTypeTask = new StableTypeTask();
//        stableTypeTask.doStableTypeTask();
//        LocalDateTime beginTime = LocalDateTime.now();
//        InetAddress inetAddress = InetAddress.getByName("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com");
//        Long opetime = Duration.between(beginTime,LocalDateTime.now()).toMillis();
//        logger.info("默认解析时间为： {}ms",opetime);
    }

    /**
     * dns facade 功能测试
     */
    private static void testFeatures() {
//        logger.info(" 从自定义 dns cache 中获取mysql InetAddress");
        String mysql = "pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com";
        testDns(mysql);
        DnsCacheManager.disableCache("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com","mysql");
        logger.info("------------------------------------");
        testDns(mysql);

//        logger.info("从自定义  dns cache 中获取www.baidu.com InetAddress");
//        String baidu = "www.baidu.com";
//        testDns(baidu);

    }

    private static void testDns(String dns) {
        for (int i = 0; i < 10; i++) {
            try {
                // mac 默认ipv6 支持
//                LocalDateTime beginTime = LocalDateTime.now();
                InetAddress inetAddress = InetAddress.getByName(dns);
                logger.debug("dns:{}    {}", inetAddress.getHostAddress(),dns);
//                Long opetime = Duration.between(beginTime,LocalDateTime.now()).toMillis();
//                System.out.println("默认dns解析时间："+opetime+"ms");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
}
