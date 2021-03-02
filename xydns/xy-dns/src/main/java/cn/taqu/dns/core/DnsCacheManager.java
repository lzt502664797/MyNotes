package cn.taqu.dns.core;

import com.google.common.collect.Lists;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * xydns 缓存管理器
 * 不同类型由相应的缓存，如mysql、redis
 * 缓存数据类型为 ConcurrentHashMap<String, DnsCacheEntity>
 * 所有类别的缓存存放在 typeCaches 中
 * @author pengchen
 * @date 2021/01/04
 */
public class DnsCacheManager {

  final static Logger logger = LoggerFactory.getLogger(DnsCacheManager.class);
  /**
   * stable dns caches
   */
  private final static ConcurrentHashMap<String, DnsCacheEntity> stableDnsCache = new ConcurrentHashMap<>();
  /**
   * unstable dns caches
   */
  private final static ConcurrentHashMap<String, DnsCacheEntity> unStableDnsCache = new ConcurrentHashMap<>();
  /**
   * 类型 - stable cache or unstable cache
   */
  public static ConcurrentHashMap<String, ConcurrentHashMap<String, DnsCacheEntity>> typeCaches = new ConcurrentHashMap<>();

  static {
    typeCaches.put("stableType", stableDnsCache);
    typeCaches.put("unStableType", unStableDnsCache);
  }

  /**
   * service code
   */
  private String code = "antispam_v2";
  /**
   * service index
   */
  private String index = "j241";


  /**
   * 添加缓存
   *
   * @param dns dns
   * @param result 结果
   * @param type 类型
   */
  public static void putCache(String dns, InetAddress[] result, String type) {
    DnsCacheEntity entity = createXYDnsEntity(dns, result, type);
    typeCaches.get(type).put(dns, entity);
  }


  /**
   * 禁用缓存
   *
   * @param dns dns
   * @param type 类型
   */
  public static void disableCache(String dns, String type) {
    if (null != typeCaches.get(type).get(dns)) {
      typeCaches.get(type).get(dns).setDisable(true);
    }
  }


  /**
   * 启用缓存
   *
   * @param dns dns
   * @param type 类型
   */
  public static void enableCache(String dns, String type) {
    if (null != typeCaches.get(type).get(dns)) {
      typeCaches.get(type).get(dns).setDisable(false);
    }
  }


  /**
   * 获取对应dns type 下的缓存
   *
   * @param dns dns
   * @param type 类型
   * @return {@link InetAddress[]}*
   */
  public static InetAddress[] getCache(String dns, String type) {
    DnsCacheEntity entity = null;
    entity = typeCaches.get(type).get(dns);

    // 判断缓存是否为 null 或 被禁用
    if (entity == null) {
      return null;
    }
    return entity.getInetAddresses();
  }


  public static boolean removeCache(String dns) {
    return dns.contains("mysql") ? removeCache(dns, "mysql") : removeCache(dns, "redis");
  }

  /**
   * 删除对应dns ，type 下缓存
   *
   * @param dns dns
   * @param type 类型
   */
  public static boolean removeCache(String dns, String type) {
    if (dns.contains(type)) {
      return typeCaches.get(type).remove(dns) != null;
    }
    logger.warn("未成功移除cache dns [] , type [] ", dns, type);
    return false;
  }

  /**
   * 删除该type所有cache
   *
   * @param type 类型
   */
  public static void removeAll(String type) {
    typeCaches.get(type).clear();
  }

  /**
   * create XYDnsCacheEntity
   *
   * @param dns dns
   * @param result InetAddress
   * @param type mysql redis
   * @return {@link DnsCacheEntity}
   */
  private static DnsCacheEntity createXYDnsEntity(String dns, InetAddress[] result, String type) {
    DnsCacheEntity xyDnsCacheEntity = new DnsCacheEntity();
    xyDnsCacheEntity.setDns(dns);
    xyDnsCacheEntity.setInetAddresses(result);
    xyDnsCacheEntity.setType(type);

    String strInet = result[0].getClass().toString();
    boolean isIpv4 = strInet.contains("Inet4");
    boolean isIpv6 = strInet.contains("Inet6");
    xyDnsCacheEntity.setIpV4(isIpv4);
    xyDnsCacheEntity.setIpV6(isIpv6);
    // 开启缓存
//    xyDnsCacheEntity.setDisable(false);
    List<String> lst = Lists.newArrayList();
    for (int i = 0; i < result.length; i++) {
      lst.add(result[i].getHostAddress());
    }
    xyDnsCacheEntity.setValues(lst);


    return xyDnsCacheEntity;
  }

  public static ConcurrentHashMap<String, DnsCacheEntity> getStableDnsCache() {
    return stableDnsCache;
  }

  public static ConcurrentHashMap<String, DnsCacheEntity> getUnStableDnsCache() {
    return unStableDnsCache;
  }

  public static ConcurrentHashMap<String, ConcurrentHashMap<String, DnsCacheEntity>> getTypeCaches() {
    return typeCaches;
  }

  public static void setTypeCaches(
      ConcurrentHashMap<String, ConcurrentHashMap<String, DnsCacheEntity>> typeCaches) {
    DnsCacheManager.typeCaches = typeCaches;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }
}
