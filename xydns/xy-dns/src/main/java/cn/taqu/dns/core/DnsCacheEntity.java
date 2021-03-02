package cn.taqu.dns.core;

import com.google.common.base.Objects;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

/**
 * xydns缓存实体
 *
 * @author pengchen
 * @date 2021/01/04
 */
public class DnsCacheEntity {

  // mysql or redis
  private String type;
  // dns
  private String dns;
  // ipv4 or ipv6
  private List<String> values;
  // is ipv4 ？
  private volatile Boolean isIpV4;
  // is ipv6 ？
  private volatile Boolean isIpV6;

  // is disable ？
  private volatile Boolean isDisable;
  // inetAddresses
  private InetAddress[] inetAddresses;


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDns() {
    return dns;
  }

  public void setDns(String dns) {
    this.dns = dns;
  }

  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

  public Boolean getIpV4() {
    return isIpV4;
  }

  public void setIpV4(Boolean ipV4) {
    isIpV4 = ipV4;
  }

  public Boolean getIpV6() {
    return isIpV6;
  }

  public void setIpV6(Boolean ipV6) {
    isIpV6 = ipV6;
  }

  public Boolean getDisable() {
    return isDisable;
  }

  public void setDisable(Boolean disable) {
    isDisable = disable;
  }

  public InetAddress[] getInetAddresses() {
    return inetAddresses;
  }

  public void setInetAddresses(InetAddress[] inetAddresses) {
    this.inetAddresses = inetAddresses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DnsCacheEntity that = (DnsCacheEntity) o;
    return Objects.equal(dns, that.dns);

  }

  @Override
  public int hashCode() {
    return Objects.hashCode(dns);
  }

  @Override
  public String toString() {
    return "DnsCacheEntity{" +
        "type='" + type + '\'' +
        ", dns='" + dns + '\'' +
        ", values=" + values +
        ", isIpV4=" + isIpV4 +
        ", isIpV6=" + isIpV6 +
        ", isDisable=" + isDisable +
        ", inetAddresses=" + Arrays.toString(inetAddresses) +
        '}';
  }
}
