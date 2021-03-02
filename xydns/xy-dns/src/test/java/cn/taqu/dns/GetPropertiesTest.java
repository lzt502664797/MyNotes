package cn.taqu.dns;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Properties;

/**
 * @Description:
 * @Author linzt
 * @Date 2021年1月12日 0012
 */
public class
GetPropertiesTest {
    public static void main(String[] args) {
        Properties prop = new Properties();
        try {

            prop.load(new FileInputStream("src/main/resources/test1.properties"));
            LocalDateTime beginTime = LocalDateTime.now();
            String[] mysqls = prop.getProperty("pc-jiaoliuqu.rwlb.zhangbei.rds.aliyuncs.com").split(",");
            Long opetime = Duration.between(beginTime,LocalDateTime.now()).toMillis();
            System.out.println("获取propertis为： "+opetime+"ms");
//            for (String mysql:mysqls)
//                System.out.println(mysql);
            // 设置新的properties配置
//            prop.setProperty("hyaha","true");
//            prop.store(new FileOutputStream("src/main/resources/test1.properties"),null);
//            System.out.println(prop.getProperty("222"));
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
