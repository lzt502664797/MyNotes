package cn.taqu.dns.namespace;

import sun.net.spi.nameservice.NameService;
import sun.net.spi.nameservice.NameServiceDescriptor;

/**
 * xydns 名称服务描述符
 *
 * @author pengchen
 * @date 2021/01/04
 */
public class XYDnsNameServiceDescriptor implements
    NameServiceDescriptor {

  @Override
  public NameService createNameService() throws Exception {
    return new XYDnsNameService();
  }

  @Override
  public String getProviderName() {
    return "XYDnsNameService";
  }

  @Override
  public String getType() {
    return "dns";
  }
}
