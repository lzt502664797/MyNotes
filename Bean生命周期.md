

`Spring`的依赖注入的最大亮点就是你所有的`Bean`对`Spring`容器的存在是没有意识的。即你可以将你的容器替换成别的容器，例如`Goggle Guice`,这时`Bean`之间的耦合度很低。

但是在实际的项目中，我们不可避免的要用到`Spring`容器本身的功能资源，这时候`Bean`必须要意识到`Spring`容器的存在，才能调用`Spring`所提供的资源，这就是所谓的`Spring Aware`。其实`Spring` `Aware`本来就是`Spring`设计用来框架内部使用的，若使用了`Spring Aware`，你的`Bean`将会和`Spring`框架耦合。

![preview](https://pic2.zhimg.com/v2-e4fa050899e7207e7b13f2f911779781_r.jpg)