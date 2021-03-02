package cn.taqu.dns;

import cn.taqu.dns.facade.EtcdDnsFacade;
import cn.taqu.dns.util.EtcdListenerUtil;

/**
 * @Description:
 * @Author linzt
 * @Date 2021年2月18日 0018
 */
public class TestEtcdListenerUtil {
    public static void main(String[] args) throws Exception {
        EtcdListenerUtil etcdListenerUtil = new EtcdListenerUtil();
        etcdListenerUtil.startListener();
        Thread.sleep(2000);
        System.out.println("EtcdDnsFacade isOpen :" + EtcdDnsFacade.isOpen);
    }
}
