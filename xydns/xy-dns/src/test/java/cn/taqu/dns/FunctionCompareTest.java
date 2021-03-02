package cn.taqu.dns;

import cn.taqu.dns.core.DnsCacheManager;
import cn.taqu.dns.facade.PropertiesDnsFacade;
import cn.taqu.dns.namespace.XYDnsNameService;
import cn.taqu.dns.remote.DnsRemoteParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author linzt
 * @Date 2021年1月14日 0014
 */
public class FunctionCompareTest {
    static {
        java.security.Security.setProperty("networkaddress.cache.ttl", "0");
        java.security.Security.setProperty("networkaddress.cache.negative.ttl", "0");
        //sun.net.spi.nameservice.provider.1
//        System.setProperty("sun.net.spi.nameservice.provider.1", "dns,XYDnsNameService");
//        System.setProperty("sun.net.spi.nameservice.provider.2", "default");
    }
    private static Long countTime = 0L;

    final static Logger logger = LoggerFactory.getLogger(FunctionCompareTest.class);
    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        // 自定义解析器的远程解析与java默认解析速度对比
        // 需要先关闭默认解析器缓存
//        testDnsRemoteParserAndJavaDefault();

        // 自定义解析器缓存与java默认解析缓存对比
//        testUseCacheCompare();

        // 有无缓存 无法对比 (电脑存在dns缓存，导致进行远程解析时得到电脑的缓存)
//        testUseCache();

        // java默认解析时长
        LocalDateTime beginTime = LocalDateTime.now();
        System.out.println(InetAddress.getByName("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com"));
        Long opetime = Duration.between(beginTime,LocalDateTime.now()).toMillis();
        logger.info("java默认解析时间为{}ms",opetime);

        // 先加载静态代码块
        new XYDnsNameService().lookupAllHostAddr("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com");
        // windows系统存在缓存dns 30s，等缓存过期
        Thread.sleep(32000);
        DnsCacheManager.disableCache("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com","mysql");
        new XYDnsNameService().lookupAllHostAddr("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com");
        // 由缓存获取的时间
        new XYDnsNameService().lookupAllHostAddr("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com");

    }

    public static void testUseCache() throws UnknownHostException {
        // 先获取解析缓存，再进行测试
        new PropertiesDnsFacade().lookupAllHostAddr("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com");

        // 从缓存获取
        for (int i = 0; i < 100; i++) {
            testProperties();
        }
        System.out.println(countTime);
        countTime = 0L;

        System.out.println("-----------------------------no cache");
//        // 关闭缓存
        for (int i = 0; i < 100; i++) {
            LocalDateTime beginTime = LocalDateTime.now();
            DnsRemoteParser.getDefaultCache("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com");
            Long opetime = Duration.between(beginTime,LocalDateTime.now()).toMillis();
            countTime += opetime;
        }
        System.out.println(countTime);
        countTime = 0L;
    }


    /**
     * 自定义解析器缓存与java默认解析缓存对比
     * @throws UnknownHostException
     */
    public static void testUseCacheCompare() throws UnknownHostException {
        // 先各自获取解析缓存，再进行测试
        new PropertiesDnsFacade().lookupAllHostAddr("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com");
        InetAddress.getByName("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com");

        for (int i = 0; i < 10; i++) {
            testProperties();
            testJavaDefault();
        }

    }

    public static void testProperties() throws UnknownHostException {
        LocalDateTime beginTime = LocalDateTime.now();

        PropertiesDnsFacade propertiesDnsFacade = new PropertiesDnsFacade();
        propertiesDnsFacade.lookupAllHostAddr("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com");
        Long opetime = Duration.between(beginTime,LocalDateTime.now()).toMillis();
        countTime += opetime;
        logger.debug("Properties 解析时长： {}毫秒",opetime);
    }

    /**
     * 自定义解析器的远程解析与java默认解析速度对比
     * @throws UnknownHostException
     */
    public static void testDnsRemoteParserAndJavaDefault() throws UnknownHostException {
        for (int i = 0; i < 10; i++) {
            testDnsRemoteParser();
        }
        System.out.println(countTime);
        countTime = 0L;
        for (int i = 0; i < 10; i++) {
            testJavaDefault();
        }
        System.out.println(countTime);
        countTime = 0L;
    }

    public static void testDnsRemoteParser() throws UnknownHostException {
        LocalDateTime beginTime = LocalDateTime.now();

//        PropertiesDnsFacade propertiesDnsFacade = new PropertiesDnsFacade();
//        propertiesDnsFacade.lookupAllHostAddr("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com");
        DnsRemoteParser.getDefaultCache("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com");

        Long opetime = Duration.between(beginTime,LocalDateTime.now()).toMillis();
        countTime += opetime;
        logger.debug("DnsRemoteParser 解析时长： {}毫秒",opetime);
    }

    public static void testJavaDefault() throws UnknownHostException {
        LocalDateTime beginTime = LocalDateTime.now();

        InetAddress.getByName("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com");
//        logger.debug("dns:{}    {}", inetAddress.getHostAddress(),"pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com");

        Long opetime = Duration.between(beginTime,LocalDateTime.now()).toMillis();
        countTime += opetime;
        logger.debug("java默认 解析时长： {}毫秒",opetime);
    }

}
