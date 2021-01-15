

默认dns解析成功结果保存缓存30s，失败缓存时间为10s

初始化加载时，会先对本机 127.0.0.1 进行解析。

dns解析失败：

```tip
如果域名在DNS服务器上不存在，那么客户端在进行一段时间的尝试后（平均为5秒），就会抛出一个UnknownHostException异常。为了让下一次访问这个域名时不再等待，DNS缓存将这个错误信息也保存了起来。也就是说，只有第一次访问错误域名时才进行5称左右的尝试，以后再访问这个域名时将直接抛出UnknownHostException异常，而无需再等待5秒钟
```

若直接设置所有的缓存时间为1s或更短，则会影响系统性能。稳定的dns 可设置较长的缓存时间，不稳定的dns则需要设置短的缓存时长。所以需要自定不同的策略根据不同类型进行处理。

```java
public interface CheckStrategy {

  /**
   * 检查间隔时间
   *
   * @return long
   */
  default long checkTime() {
    return 1000L;
  }

  /**
   * 不同的域名 处理
   *  当域名发生变化时的处理策略
   * @return boolean true 处理成功
   */
  default boolean differentDns(){
    return true ;
  }

  /**
   * dns服务无法响应的处理
   *
   * @return boolean true 处理成功
   */
  default boolean remoteDnsNoResponse(String dns){
    return true ;
  }

}

```

mysql类型dns缓存检查策略：

```tip
checkTime 为 10s
differentDns ：
* 更新dns解析缓存:
     * 从 DnsCacheManager 中获取mysql缓存中获取所有域名
     * 将所有域名重新解析并更新缓存数据
     * 若出现dns服务无法响应的处理情况
     * 则调用 remoteDnsNoResponse 方法进行处理
```



### 自定义DNS解析

先通过java.security.Security.setProperty设置自定义的DnsNameService

```
System.setProperty("sun.net.spi.nameservice.provider.1", "dns,XYDnsNameService");
```

在InetAddress.getByName(dns) 走到 getAddressesFromNameService 时，会调用到自定义的 NameService 的 lookupAllHostAddr

```java
for (NameService nameService : nameServices) {
                    try {
                        /*
                         * Do not put the call to lookup() inside the
                         * constructor.  if you do you will still be
                         * allocating space when the lookup fails.
                         */

                        addresses = nameService.lookupAllHostAddr(host);
                        success = true;
                        break;
                    } catch (UnknownHostException uhe) {
                        if (host.equalsIgnoreCase("localhost")) {
                            InetAddress[] local = new InetAddress[] { impl.loopbackAddress() };
                            addresses = local;
                            success = true;
                            break;
                        }
                        else {
                            addresses = unknown_array;
                            success = false;
                            ex = uhe;
                        }
                    }
                }
```

在 lookupAllHostAddr 里自定义dns解析过程

```java
default InetAddress[] lookupAllHostAddr(String dns) throws UnknownHostException {
    if(!isOpen(dns)){
      logger.warn("Dns{} 缓存不可用，需重新解析",dns);
      // todo 缓存不可用时,是否需要抛出异常？
      throw new UnknownHostException(" DnsFacade is closed ");
    }
    InetAddress[] result;
    String type = supportTypes(dns);
    if (type == null) {
      logger.debug("DnsFacade can not support {} ,default namespace will parse it ", dns);
      // todo 没有该类型缓存时，是否需要抛出异常？
      throw new UnknownHostException(" can not support this type " + dns);
    }

    result = getInetAddresses(dns, type);
    if (result == null) {
      throw new UnknownHostException(" can not find {}" + dns);
    }
    logger.debug("DnsFacade get dns {}  successfully", dns);
    return result;
  }

/**
   * 获取dns 对应的 InetAddress[]
   * 1. 从 DnsCacheManager 中获取
   * 2. 无 DnsRemoteParser 中获取 ，并put DnsCacheManager
   *
   * @param dns dns
   * @param type 类型
   * @return {@link InetAddress[]}* @throws UnknownHostException 未知主机异常
   */
  default InetAddress[] getInetAddresses(String dns, String type) {

    InetAddress[] result;
    result = DnsCacheManager.getCache(dns, type);
    if (result == null) {
      result = DnsRemoteParser.getDefaultCache(dns);
      if (result != null) {
        DnsCacheManager.putCache(dns, result, type);
      }
    }
    return result;
  }
```







当域名映射的ip发生变化时，在业务层调用dns解析缓存出错，需调用自定义的策略接口，先关闭缓存，然后重新解析，当解析成功时再开启缓存。







将不同类型域名是否可解析数据存放至ETCD，当一台机子解析失败后，将数据保存至ETCD，其它机子读取后可减少使用无效缓存出错。



域名发生变化时的策略：   重新解析域名，若解析成功则加到缓存当中；若解析失败，抛出异常记录日志