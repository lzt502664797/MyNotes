# Mybatis_plus

### 一. Mybatis-plus最基本项目结构

1.导入依赖

```xml
<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.4.0</version>
        <relativePath/> <!-- lookup parent from repository -->
</parent>
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.4.1</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <scope>test</scope>
        </dependency>
</dependencies>
```



2.配置yml文件

```yml
spring:
  datasource:
    driver: com.mysql.cj.jdbc.Driver
    username: root
    password: 123456
    url: jdbc:mysql://localhost:3306/mybatis_plus?useSSL=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
 
mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```



3.生成pojo类

```java
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
```



4.生成mapper接口类继承BaseMapper

```java
@Repository
public interface UserMapper extends BaseMapper<User> {
}
```



5.在主程序添加mapperscan注解

```java
@SpringBootApplication
@MapperScan("com.my.mybatis_plus.mapper")
public class MybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusApplication.class, args);
    }

}
```



6.在测试类测试结果

```java
@RunWith(SpringRunner.class)
@SpringBootTest
class MybatisPlusApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

}
```

![image-20201123170609945](mybatis_plus.assets/image-20201123170609945.png)



### 二： 插入数据的主键生成策略

```java
@Test
    public void testInsert() {
        User user = new User();
        user.setAge(20);
        user.setEmail("111111.qq");
        user.setName("Jack");

        int result = userMapper.insert(user);
        System.out.println(result);
        System.out.println(user);
    }
```

![image-20201123172340730](mybatis_plus.assets/image-20201123172340730.png)

```java
public enum IdType {
    AUTO(0),	// 数据库id自增
    NONE(1),	// 未设置主键
    INPUT(2),	//手动输入
    ASSIGN_ID(3), 
    ASSIGN_UUID(4),
    /** @deprecated */
    @Deprecated
    ID_WORKER(3),	//默认全局唯一id
    /** @deprecated */
    @Deprecated
    ID_WORKER_STR(3),  //ID_WORKER 字符串表示法
    /** @deprecated */
    @Deprecated
    UUID(4);		//默认全局唯一id uuid
}
```



```tip
在插入数据时，若没有传入id值，BaseMapper便会采用雪花算法生成一个唯一的id值。
默认值为  @TableId(type = IdType.ID_WORKER_STR)   

SnowFlake 算法，是 Twitter 开源的分布式 id 生成算法。其核心思想就是：使用一个 64 bit 的 long 型的数字作为全局唯一 id。在分布式系统中的应用十分广泛，且ID 引入了时间戳，基本上保持自增。
```

![img](https://img2018.cnblogs.com/i-beta/738818/202002/738818-20200214212400220-553374202.png)

