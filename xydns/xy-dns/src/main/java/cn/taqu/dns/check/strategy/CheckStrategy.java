package cn.taqu.dns.check.strategy;

import java.net.UnknownHostException;

/**
 * 检查策略是检查域名解析缓存是否过期、域名配置是否正确、服务器是否正常
 * 每个类型都有相应的检查策略
 * 当检查出dns服务器解析出问题时，需及时查看dns映射是否有问题。
 * checkTime 为该类型定时任务的时间间隔
 * differentDns 为该类型定时任务检测策略
 * remoteDnsNoResponse dns服务无法响应的处理
 * @author pengchen
 * @date 2021/01/07
 */
public interface CheckStrategy {

  /**
   * 检查间隔时间
   *
   * @return long
   */
  default long checkTime() {
    return 2000L;
  }

  /**
   * 不同的域名 处理
   *  当域名发生变化时的处理策略
   * @return boolean true 处理成功
   */
  default boolean differentDns() throws UnknownHostException {
    return true ;
  }



  /**
   * dns服务无法响应的处理
   *
   * @return boolean true 处理成功
   */
  default boolean checkRemoteDnsServer(){
    return true ;
  }

}
