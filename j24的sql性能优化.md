j24菜单迁移至测试环境

添加接口自动化测试用例

修改日志和异常





对SOA请求方法进行分类处理

添加控制层的测试用例

迁移master分支白名单列表页面 新增 白名单设备 功能、部分 LiveRpc 接口。

分析查看数美检测日志报错异常





多次使用contains时，建议将list转换为set



```
isChat  getInfo
```







1.数美音频连接超时  v1/shumeiCheck/shumeiAudioCheck

posturl=http://api-audio-bj.fengkongcloud.com/v2/saas/anti_fraud/audio,err= - 璇锋眰鍙傛暟:["private_msg","da5pgymfrwg5m9","ck5irzkkq179","da34o1kqben6","https:\/\/msg-voice.jiaoliuqu.com\/taqu_android_msg-voice_118_1609852696532_1_0_77350.m4a","MESSAGE"]\njava.net.SocketException: **Connection timed out** (Read failed)\n\tat java.net.SocketInputStream.socketRead0(Native Method)\n\tat java.net.SocketInputStream.socketRead(SocketInputStream.java:116)\n\tat java.net.SocketInputStream.read(SocketInputStream.java:171)\n\tat java.net.SocketInputStream.read(SocketInputStream.java:141)\n\tat org.apache.http.impl.io.SessionInputBufferImpl.streamRead(SessionInputBufferImpl.java:136)\n\tat org.apache.http.impl.io.SessionInputBufferImpl.fillBuffer(SessionInputBufferImpl.java:152)\n\tat org.apache.http.impl.io.SessionInputBufferImpl.readLine(SessionInputBufferImpl.java:270)\n\tat org.apache.http.impl.conn.DefaultHttpResponseParser.parseHead(DefaultHttpResponseParser.java:140)\n\tat org.apache.http.impl.conn.DefaultHttpResponseParser.parseHead(DefaultHttpResponseParser.java:57)\n\tat org.apache.http.impl.io.AbstractMessageParser.parse(AbstractMessageParser.java:260)\n\tat org.apache.http.impl.DefaultBHttpClientConnection.receiveResponseHeader(DefaultBHttpClientConnection.java:161)\n\tat sun.reflect.GeneratedMethodAccessor101.invoke(Unknown Source)\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.lang.reflect.Method.invoke(Method.java:498)\n\tat org.apache.http.impl.conn.CPoolProxy.invoke(CPoolProxy.java:138)\n\tat com.sun.proxy.$Proxy127.receiveResponseHeader(Unknown Source)\n\tat org.apache.http.protocol.HttpRequestExecutor.doReceiveResponse(HttpRequestExecutor.java:271)\n\tat org.apache.http.protocol.HttpRequestExecutor.execute(HttpRequestExecutor.java:123)\n\tat org.apache.http.impl.execchain.MainClientExec.execute(MainClientExec.java:253)\n\tat org.apache.http.impl.execchain.ProtocolExec.execute(ProtocolExec.java:194)\n\tat org.apache.http.impl.execchain.RetryExec.execute(RetryExec.java:85)\n\tat org.apache.http.impl





2.   	客户端读取超时关闭了连接，这时服务器往客户端再写数据就发生了broken pipe异常						v1/shumeiCheck/shumeiAudioCheck

exception not handled caught by springmvc\norg.apache.catalina.connector.ClientAbortException: java.io.IOException: **Broken pipe**\n\tat org.apache.catalina.connector.OutputBuffer.realWriteBytes(OutputBuffer.java:396)\n\tat org.apache.tomcat.util.buf.ByteChunk.flushBuffer(ByteChunk.java:426)\n\tat org.apache.catalina.connector.OutputBuffer.doFlush(OutputBuffer.java:345)\n\tat org.apache.catalina.connector.OutputBuffer.flush(OutputBuffer.java:320)\n\tat org.apache.catalina.connector.CoyoteOutputStream.flush(CoyoteOutputStream.java:110)\n\tat com.fasterxml.jackson.core.json.UTF8JsonGenerator.flush(UTF8JsonGenerator.java:998)\n\tat com.fasterxml.jackson.databind.ObjectWriter.writeValue(ObjectWriter.java:932)\n\tat org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter.writeInternal(AbstractJackson2HttpMessageConverter.java:264)\n\tat org.springframework.http.converter.AbstractGenericHttpMessageConverter.write(AbstractGenericHttpMessageConverter.java:100)\n\tat org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor.writeWithMessageConverters(AbstractMessageConverterMethodProcessor.java:167)\n\tat org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor.writeWithMessageConverters(AbstractMessageConverterMethodProcessor.java:100)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor.handleReturnValue(RequestResponseBodyMethodProcessor.java:166)\n\tat org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite.handleReturnValue(HandlerMethodReturnValueHandlerComposite.java:80)\n\tat org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:127)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:806)\n\tat org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.jav





3.   							v1/shumeiCheck/shumeiAudioCheck

Failed to invoke @ExceptionHandler method: public java.lang.String



message-fcf97d889-ll7gk | online | 10.66.191.98 | cn-zhangjiakou.10.63.97.31 | [soa-call:v1/shumeiCheck/shumeiAudioCheck] retry soa-json 1 times; exception: **curl request timeout:Operation timed out after 10001 milliseconds with 0 out of -1 bytes received[**debug_info:array (  'url' => 'http://jantispam.soa.internal.taqu.cn/tq-anti-spam/api?service=shumeiCheck&distinctRequestId=8c017ef8383d0e972147bbf1b66c3008&env=0&log_origin=rest&origin_system=message&token=Mf723e4cefaef38aa560011df62c31d0d&timestamp=1609850853&platform_name=android&platform_id=1&appcode=1&app_version=7388&channel=xingjiabi&access=wifi&gender=2&longitude=117.69725076&latitude=39.91398896&lang=zh_cn&mtracer_id=-&ip=106.119.233.16&uuid=cklpupxslbdt&ticket_id=&client_uri=v2/Message/send&cloned=1&city=0&debug=0&ctime=1609850853542&soa_come_from=php&serial_sequence=0.0-1.0-5.0&uri=v1/shumeiCheck/shumeiAudioCheck&method=shumeiAudioCheck',  'content_type' => NULL,  'http_code' => 0,  'header_size' => 0,  'request_size' => 951,  'filetime' => -1,  'ssl_verify_result' => 0,  'redirect_count' => 0,  'total_time' => 10.001479,  'namelookup_time' => 0.004139,  'connect_time' => 0.004528,  'pretransfer_time' => 0.004588,  'size_upload' => 225.0,  'size_download' => 0.0,  'speed_download' => 0.0,  'speed_upload' => 22.0,  'download_content_length' => -1.0,  'upload_content_length' => 225.0,  'starttransfer_time' => 0.0,  'redirect_time' => 0.0,  'redirect_url' => '',  'primary_ip' => '192.168.230.47',  'certinfo' =>   array (  ),  'primary_port' => 80,  'local_ip' => '10.66.191.98',  'local_port' => 40736,)]; url: http://jantispam.soa.internal.taqu.cn/tq-anti-spam/api?service=shumeiCheck&distinctRequestId=8c017ef8383d0e972147bbf1b66c3008&env=0&log_origin=rest&origin_system=message&token=Mf723e4cefaef38aa560011df62c31d0d&timestamp=1609850853&platform_name=android&platform_id=1&appcode=1&app_version=7388&channel=xingjiabi&access=wifi&gender=2&longitude=117.69725076&latitude=39.91398896&lang=zh_cn&mtracer_id=-&ip=106.119.233.16&uuid=cklpupxslbdt&ticket_id=&client_



















