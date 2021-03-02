package cn.taqu.dns.namespace;

import cn.taqu.dns.facade.DefaultFacade;
import cn.taqu.dns.facade.DnsFacade;
import cn.taqu.dns.facade.EtcdDnsFacade;
import cn.taqu.dns.facade.PropertiesDnsFacade;
import cn.taqu.dns.util.GetYmlPropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.spi.nameservice.NameService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * XYDnsNameService 本身作为自定义dns 处理逻辑的核心，对于指定dns，做指定处理
 * getDnsFacde 判断dns解析配置是从什么环境获取的，并返回相应类型的Facde
 * lookupAllHostAddr 使用自定义的解析策略进行解析
 * @author LinZhengTao
 * @date 2021/01/14
 */
public class XYDnsNameService implements NameService {

  private final static Logger logger =  LoggerFactory.getLogger(XYDnsNameService.class);
  private static boolean isProperties = false;
  private static boolean isEtcd = false;

  static {
    LocalDateTime beginTime = LocalDateTime.now();

    // 使用读取yml文件工具类 判断为哪种方式获取dns配置
    String commonYml = GetYmlPropertiesUtil.getCommonYml("dns.env");
    if (commonYml.equals("properties")) {
      isProperties = true;
    } else if(commonYml.equals("etcd")){
      isEtcd = true;
    }

    Long opetime = Duration.between(beginTime,LocalDateTime.now()).toMillis();
    System.out.println(opetime);
    logger.info("静态加载获取yml文件中的配置用时为： {}ms",opetime);
  }

  /**
   * 自定义dns解析过程
   *
   * @param dns hostname
   * @return {@link InetAddress[]}* @throws UnknownHostException 未知主机异常
   */
  @Override
  public InetAddress[] lookupAllHostAddr(String dns) throws UnknownHostException {
    LocalDateTime beginTime = LocalDateTime.now();
    InetAddress[] inetAddresses = getDnsFacde().lookupAllHostAddr(dns);
    Long opetime = Duration.between(beginTime,LocalDateTime.now()).toMillis();
    logger.info("自定义解析时间为： {}ms",opetime);
    System.out.println("自定义解析时间为： "+opetime+"ms");

    return inetAddresses;
  }


  /**
   * 交给default provider 去做这个事情
   *
   * @param bytes ip bytes
   * @return {@link String}* @throws UnknownHostException 未知主机异常
   */
  @Override
  public String getHostByAddr(byte[] bytes) throws UnknownHostException {
    throw new UnknownHostException(" ip-host is not support ");
  }

  /**
   * 判断配置信息存在于Properties或ETCD
   * 从Properties文件或ETCD中获取是否开启配置
   * 若都未配置，则日志警告并返回 DefaultFacade
   * @return
   */
  DnsFacade getDnsFacde() {

    if (isProperties) {
      return new PropertiesDnsFacade();
    }
    if (isEtcd) {
      return new EtcdDnsFacade();
    }
    logger.warn("尚未配置自定义DNS缓存信息，请在application.yml进行配置。");
    // 若都未开启，则默认返回 PropertiesDnsFacade
    return new DefaultFacade();
  }

}
