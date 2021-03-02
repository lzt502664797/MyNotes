package cn.taqu.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * dns 性能测试 基于jmh 框架
 *
 * @author pengchen
 * @date 2021/01/07
 */
public class DnsPerformanceTest {

    final static Logger logger = LoggerFactory.getLogger(DnsPerformanceTest.class);

    static {
      java.security.Security.setProperty("networkaddress.cache.ttl", "0");
      java.security.Security.setProperty("networkaddress.cache.negative.ttl", "0");
      //sun.net.spi.nameservice.provider.1
      System.setProperty("sun.net.spi.nameservice.provider.1", "dns,XYDnsNameService");
      System.setProperty("sun.net.spi.nameservice.provider.2", "default");
    }

    public static void main(String[] args) {
        testFeatures();


    }

    /**
     * dns facade 功能测试
     */
    private static void testFeatures() {
        logger.info("测试1 从dns cache 中获取mysql InetAddress");
        String mysql = "pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com";
        testDns(mysql);

        logger.info("测试2 从dns cache 中获取redis InetAddress");
    }






  private static void testDns(String dns) {
    for (int i = 0; i < 10; i++) {
      try {
        // mac 默认ipv6 支持
        InetAddress inetAddress = InetAddress.getByName(dns);
        logger.debug("dns:{} ", inetAddress.getHostAddress());
      } catch (UnknownHostException e) {
        e.printStackTrace();
      }
    }
  }
}
