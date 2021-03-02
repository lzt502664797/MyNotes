package cn.taqu.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;

import cn.taqu.dns.util.EtcdListenerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * dns 功能测试
 *
 * @author pengchen
 * @date 2021/01/07
 */
public class DnsFeatureTest {

    static {
        java.security.Security.setProperty("networkaddress.cache.ttl", "0");
        java.security.Security.setProperty("networkaddress.cache.negative.ttl", "0");
        //sun.net.spi.nameservice.provider.1
        System.setProperty("sun.net.spi.nameservice.provider.1", "dns,XYDnsNameService");
        System.setProperty("sun.net.spi.nameservice.provider.2", "default");

    }

    final static Logger logger = LoggerFactory.getLogger(DnsFeatureTest.class);



    public static void main(String[] args) throws Exception {
        EtcdListenerUtil etcdListenerUtil = new EtcdListenerUtil();
        try {
            etcdListenerUtil.startListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
        testFeatures();
    }

    /**
     * dns facade 功能测试
     */
    private static void testFeatures() {
    logger.info("测试1 从dns cache 中获取mysql InetAddress");
    String mysql = "pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com";
    testDns(mysql);


    logger.info("测试3 ");
    String tq = "j2.test.k8s.taqu.cn";
    testDns(tq);

    logger.info("测试4");
    String baidu = "www.baidu.com";
    testDns(baidu);

  }

  /**
   *  TODO 测试默认dns 解析
   */
  private static void testDefaultDns() {
    // 关闭开关
    // networkaddress.cache.ttl  测试
    // networkaddress.cache.negative.ttl 测试

    // networkaddress.cache.ttl 可以覆盖？
  }


  /**
   *  TODO 测试DnsCacheEntity 所有属性
   *  如果 有不合理属性请修改
   */
  private static void testDnsCacheEntity() {

  }



  private static void testDns(String dns) {
    for (int i = 0; i < 10; i++) {
      try {
          if(i==0){
              logger.info("第一次解析：");
          }
        // mac 默认ipv6 支持
        InetAddress inetAddress = InetAddress.getByName(dns);
        logger.debug("dns:{} ", inetAddress.getHostAddress());
      } catch (UnknownHostException e) {
        e.printStackTrace();
      }
    }
  }
}
