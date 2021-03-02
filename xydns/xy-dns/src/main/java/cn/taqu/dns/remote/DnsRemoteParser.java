package cn.taqu.dns.remote;

import cn.taqu.dns.facade.DnsFacade;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.security.AccessController;
import sun.security.action.GetPropertyAction;

/**
 * DnsRemoteParser 提供远程解析dns 能力
 *
 * @author pengchen
 * @date 2021/01/07
 */
public class DnsRemoteParser {

  /**
   * 从默认缓存dns cache 中获取 InetAddress[]
   *
   * @param dns dns
   * @return {@link InetAddress[]}
   */
  public static InetAddress[] getDefaultCache(String dns) {

    String implName = "Inet6AddressImpl";
    Object impl = null;
    String prefix = AccessController.doPrivileged(
        new GetPropertyAction("impl.prefix", ""));
    Class clss = null;
    try {
      clss = Class.forName("java.net." + prefix + implName);
      Constructor<?> constructor = clss.getDeclaredConstructor();
      constructor.setAccessible(true);
      impl = constructor.newInstance();
    } catch (ClassNotFoundException e) {
      DnsFacade.logger.error("Class not found: java.net." + prefix +
          implName + ":\ncheck impl.prefix property " +
          "in your properties file.");
    } catch (InstantiationException e) {
      DnsFacade.logger.error("Could not instantiate: java.net." + prefix +
          implName + ":\ncheck impl.prefix property " +
          "in your properties file.");
    } catch (IllegalAccessException e) {
      DnsFacade.logger.error("Cannot access class: java.net." + prefix +
          implName + ":\ncheck impl.prefix property " +
          "in your properties file.");
    } catch (NoSuchMethodException e) {
      DnsFacade.logger.error("NoSuchMethodException ", e);
    } catch (InvocationTargetException e) {
      DnsFacade.logger.error("InvocationTargetException", e);
    }

    try {
      Method method = clss.getDeclaredMethod("lookupAllHostAddr", String.class);
      method.setAccessible(true);
      Object result = method.invoke(impl, dns);
      if (result != null) {
        return (InetAddress[]) result;
      } else {
        return null;
      }
    } catch (NoSuchMethodException e) {
      DnsFacade.logger.error("lookupAllHostAddr []", e);
    } catch (IllegalAccessException e) {
      DnsFacade.logger.error("lookupAllHostAddr []", e);
    } catch (InvocationTargetException e) {
      DnsFacade.logger.error("lookupAllHostAddr []", e);
    }
    return null;
  }
}
