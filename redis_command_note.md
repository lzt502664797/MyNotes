

# Redis基本数据类型及使用方法



## 五大数据类型:

#### 1.     String:         key:String

```redis
set key
get key
STRLEN key
GETRANGE key index index
SETRANGE key index XXXX
append key
incr key(i++)
decr key(i--)
setex key 30(设置key属性存在时间30s)
setnx key value(若key不存在，设置key值为value，若key存在则设置不生效)
mset k1 v1 k2 v2 k3 v3
mget k1 k2 k3
msetnx k1 v1 k2 v2 k3 v3(同上)
mset user:1:name zhangsan user:1:age 20
mget user:1:name user:1:age
getset key value (先get后再执行set)

```



#### 2.    List:	    	key:LIST

```redis
LPUSH list1 value1
RPUSH list1 value2
LLEN list1
LRANGE list1 index index
LINDEX list1 index
LPOP list1
RPOP list1
LREM list1 count value(移除count个value)
RPOPLPUSH list1 list2
LSET list1 index value1(list1:index 必须存在)
LINSERT list1 value1 before|after value2(在list1的value1的before或者after位置插入一个value2)
```



#### 3.    Set:			   key:SET

```redis
SADD set value
SISMEMBER set value
SCARD set (获取集合中的个数)
SMEMBERS set
SREM set value
SRANDMEMBER set count
SPOP set
SMOVE set1 set2 value
SDIFF set1 set2     差集
SINTER set1 set2   交集
SUNION set1 set2 并集
```



#### 4.     Hash:		key:HASH

```redis
HSET  hash1 key1 value1
HGET hash1 key1
HMSET hash1 k1 v1 k2 v2
HMGET hash1 k1 k2
HGETALL hash1
HDEL hash1 k1
HLEN hash1
HEXISTS hash1 k1
HVALS hash1(只获取value)
HINCRBY hash1 k1 1
HSETNX hash1 k1 v1

```



#### 5.  	ZSET:  (有序set)  		key:SET

```redis
ZSET:  (有序set)  		key:SET
ZADD myset score1 value1 score2 value2
ZRANGE	myset index index
ZRANGEBYSCORE myset -inf +inf
ZREVRANGEBYSCORE myset +inf -inf
ZREM myset value
ZCOUNT myset score1 score2
```





## 三种特殊数据类型:

#### 1. 	GEOSPATIAL: (地理位置的数据类型)

```redis
GEOSPATIAL: (地理位置的数据类型)
GEOADD china:city 116.0 39.90 beijin 121.47 31.23 shanghai
GEOPOS china:city beijin
GEODIST china:city beijin shanghai km
GEORADIUS china:city 110 30 1000 km withcoord withdist count 1
(经度110 纬度30 半径距离1000 单位km 显示经纬度withcoord 显示距离withdist 搜索结果的个数1)
GEORADIUSBYMEMBER china:city beijin 1000 km

```

#### 2. 	HyperLogLog: 计数统计

```redis
PFADD mykey1 a b c d
PFMERGE mykey3 mykey1 mykey2
PFCOUNT mykey3
优点：占用的内存是固定的，2^64不同的元素只需12KB
网页的uv（unique visit）
错误率为0.81%

```

#### 3.	Bitmaps： 位存储  (用于统计只有两种状态的用户信息，如   登陆|未登录   签到|未签到)

```redis
SETBIT sign:peter 0 1
GETBIT sign:peter 0
BITCOUNT sign:peter

```



## 消息订阅

```tip
订阅端：
SUBSCRIBE channel

发送端：
PUBLISH channel message
```

