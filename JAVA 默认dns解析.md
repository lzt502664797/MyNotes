使用InetAddress.getAllByName(“ ”) 获取域名解析结果

# 默认DNS解析流程：

### 1.在 `getAllByName0` 中，先通过 `SecurityManager`进行判断该域名是否可以连接

```java
if (check) {
            SecurityManager security = System.getSecurityManager();
            if (security != null) {
                security.checkConnect(host, -1);
            }
        }
```

### 2.获取该域名缓存，判断其是否已被解析并存储于缓存当中。

### 3.若不存在于缓存中，则通过`getAddressesFromNameService`进行解析。

1.先在`checkLookupTable(String host)`中判断是否域名正在被解析

#### `checkLookupTable`   ：

`lookupTable` 用于存放已经解析过的域名，数据类型为

```java
private static final HashMap<String, Void> lookupTable = new HashMap<String, Void>()
```

查找在`lookupTable`中是存在该域名，若不存在，则将其加入正在被解析列表`lookupTable` ，返回null；若存在，说明其他线程正在解析该域名，将该线程阻塞，等待其它线程唤醒后，再次查看缓存是否存在解析地址。若缓存存在则返回结果；若不存在则将其加入正在被解析列表`lookupTable` ，返回null进行解析。

2..在`getAddressesFromNameService`遍历所有`nameServices`的`lookupAllHostAddr`进行域名解析。

3.判断解析得到的结果是否为数组中的第 0 个，若不是则将其放到第0个。

```java
// More to do?
                if (reqAddr != null && addresses.length > 1 && !addresses[0].equals(reqAddr)) {
                    // Find it?
                    int i = 1;
                    for (; i < addresses.length; i++) {
                        if (addresses[i].equals(reqAddr)) {
                            break;
                        }
                    }
                    // Rotate
                    if (i < addresses.length) {
                        InetAddress tmp, tmp2 = reqAddr;
                        for (int j = 0; j < i; j++) {
                            tmp = addresses[j];
                            addresses[j] = tmp2;
                            tmp2 = tmp;
                        }
                        addresses[i] = tmp2;
                    }
                }
```

4.在`cacheAddresses`中将正向或负向dns加入缓存当中。

5.最后更新 `lookupTable` ，将域名从中删除，并唤醒其它线程。



### 使用自定义方法`NameService`解决由于域名映射变更而获取错误的域名解析缓存。

将默认正向负向缓存关闭，在`NameService`中使用自定义缓存，设置不同类型的缓存策略。并从etcd获取缓存是否开启配置。









