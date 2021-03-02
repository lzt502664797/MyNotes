package cn.taqu.dns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description:
 * @Author linzt
 * @Date 2021年1月12日 0012
 */
public class NoOpenCacheTest {
    static {
        java.security.Security.setProperty("networkaddress.cache.ttl", "0");
        java.security.Security.setProperty("networkaddress.cache.negative.ttl", "0");
        //sun.net.spi.nameservice.provider.1
        System.setProperty("sun.net.spi.nameservice.provider.1", "dns,XYDnsNameService");
        System.setProperty("sun.net.spi.nameservice.provider.2", "default");
    }

    final static Logger logger = LoggerFactory.getLogger(NoOpenCacheTest.class);
    public static void main(String[] args) throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getByName("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com");
        System.out.println(inetAddress.getHostAddress());
    }
}
