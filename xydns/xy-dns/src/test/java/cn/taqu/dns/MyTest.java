package cn.taqu.dns;

import cn.taqu.dns.core.DnsCacheManager;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.requests.EtcdKeyGetRequest;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;
import mousio.etcd4j.responses.EtcdKeysResponse;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * @Description:
 * @Author linzt
 * @Date 2021年1月11日 0011
 */
public class MyTest {
    public static void main(String[] args) {
        String str = "stableType";
        if(str.contains("stabletype"))
            System.out.println("true");
        else
            System.out.println("false");
    }

    public void test(){
        InetAddress[] inetAddresses = new InetAddress[1];

    }

}
