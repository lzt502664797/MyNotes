

![image-20201130135440467](j24的sql性能优化.assets/image-20201130135440467.png)





文化沙漠  知识盲区  ETCD





接口1：查询白名单用户数据





```
sensitiveWordMap  分为 key 和 SensitiveWord
SensitiveWord分为type  content  createtime
```

使用缓存的接口：setSensitiveWordMap  setSensitiveWordListMap(在sensitiveWordListMap中添加新增的sensitiveWordMap)

```tip
setSensitiveWordMap ()
setSensitiveWordListMap(在sensitiveWordListMap中添加新增的sensitiveWordMap)
updateSensitiveWordMap
```









```
Map<ContentTypeEnum, Map<String, SyncCheckRule>> syncCheckRuleMap
```









1.调用数美或者ali有异常时的处理

2.外部api调用文档详情及优化





