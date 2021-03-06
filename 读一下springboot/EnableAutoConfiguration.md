### EnableAutoConfiguration

`@EnableAutoConfiguration`注解可以注册项目包外的bean。而`spring.factories`文件，则是用来记录项目包外需要注册的bean类名

项目中的配置类只需通过 @Configuration 即可



`@Import({AutoConfigurationImportSelector.class}) ` 为 `@EnableAutoConfiguration`  关键注解

借助`AutoConfigurationImportSelector`，`@EnableAutoConfiguration`可以帮助SpringBoot应用将所有符合条件(spring.factories)的**bean定义**（如Java Config@Configuration配置）都加载到当前SpringBoot创建并使用的IoC容器

```java
@Override
public String[] selectImports(AnnotationMetadata annotationMetadata) {
   if (!isEnabled(annotationMetadata)) {
      return NO_IMPORTS;
   }
    // 自动配置 元数据获取(即项目中需要注入的配置类获取)
   AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader
         .loadMetadata(this.beanClassLoader);
    // 获取自动配置条目
   AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(
         autoConfigurationMetadata, annotationMetadata);
    // 返回结果
   return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
}
```

```java
protected AutoConfigurationImportSelector.AutoConfigurationEntry getAutoConfigurationEntry(AutoConfigurationMetadata autoConfigurationMetadata, AnnotationMetadata annotationMetadata) {
    if (!this.isEnabled(annotationMetadata)) {
        return EMPTY_ENTRY;
    } else {
        AnnotationAttributes attributes = this.getAttributes(annotationMetadata);
        // 获取所有候选配置类(所有jar包)
        List<String> configurations = this.getCandidateConfigurations(annotationMetadata, attributes);
        // 移除重复的配置类
        configurations = this.removeDuplicates(configurations);
        // 获取被排除的候选配置类
        Set<String> exclusions = this.getExclusions(annotationMetadata, attributes);
        // 检查被排除的候选配置类是否存在于候选配置类中
        this.checkExcludedClasses(configurations, exclusions);
        // 移除被排除的候选配置类
        configurations.removeAll(exclusions);
        // 过滤获取项目需要的配置类(只留下需要被注入的配置类)
        configurations = this.filter(configurations, autoConfigurationMetadata);
        // 触发自动配置导入事件
        this.fireAutoConfigurationImportEvents(configurations, exclusions);
        return new AutoConfigurationImportSelector.AutoConfigurationEntry(configurations, exclusions);
    }
}
```

在工厂类中加载组件工厂，通过反射实例化bean

```java
private static <T> T instantiateFactory(String instanceClassName, Class<T> factoryClass, ClassLoader classLoader) {
    try {
        Class<?> instanceClass = ClassUtils.forName(instanceClassName, classLoader);
        if (!factoryClass.isAssignableFrom(instanceClass)) {
            throw new IllegalArgumentException("Class [" + instanceClassName + "] is not assignable to [" + factoryClass.getName() + "]");
        } else {
            return ReflectionUtils.accessibleConstructor(instanceClass, new Class[0]).newInstance();
        }
    } catch (Throwable var4) {
        throw new IllegalArgumentException("Unable to instantiate factory class: " + factoryClass.getName(), var4);
    }
}
```