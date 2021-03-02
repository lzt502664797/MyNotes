package cn.taqu.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author linzt
 * @Date 2021年2月20日 0020
 */
public class TestJavaDefault {
    static {
        java.security.Security.setProperty("networkaddress.cache.ttl", "0");
        java.security.Security.setProperty("networkaddress.cache.negative.ttl", "0");

    }
    public static void main(String[] args) throws UnknownHostException {
        LocalDateTime beginTime = LocalDateTime.now();
        InetAddress.getByName("www.4399.com");
        Long opetime = Duration.between(beginTime,LocalDateTime.now()).toMillis();
        System.out.println("默认解析时间1为： "+opetime+"ms");

        LocalDateTime beginTime1 = LocalDateTime.now();
        InetAddress.getByName("www.4399.com");
        Long opetime1 = Duration.between(beginTime1,LocalDateTime.now()).toMillis();
        System.out.println("默认解析时间2为： "+opetime1+"ms");
    }
}
