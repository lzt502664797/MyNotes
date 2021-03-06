####大宝鉴后台管理系统开发规范
1. controller 注释
2. method 注释
3. 视图层传参对象 统一 module名+VO
4. 以集成mybatis plus AutoGenerateCodeUtils 直接执行会生成对应mapper ，dao
5. 自定义mapping 映射文件请在/resources/mappper/custom
6. 请在 feature1214 提交代码
7. 每个接口对应测试用例 ，测试用例需要作为接口验收的依据
8. 不要将不相关的代码迁移过来
9. 每天下午5点之前提交代码 ，我会code review
10. 补充之前的接口文档





```
@Configuration
public class MybatisPlusPageConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
```