

没得救。

花费的时间全部是在soa远程调用上，代码执行的时间可忽略不计。

![image-20210119152341005](SenstiveCheck.assets/image-20210119152341005.png)



mvn deploy:deploy-file -DgroupId=cn.taqu -DartifactId=xy-dns -Dversion=0.0.1 -Dpackaging=jar -Dfile=F:\xydns\xy-dns\out\artifacts\xy_dns_jar\xy-dns.jar -Durl=http://nexus.proxy.internal.taqu.cn/nexus/content/groups/public/ -DrepositoryId=releases



![image-20210119180800688](SenstiveCheck.assets/image-20210119180800688.png)