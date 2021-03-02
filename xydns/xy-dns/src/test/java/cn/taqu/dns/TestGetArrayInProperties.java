package cn.taqu.dns;

import cn.taqu.dns.util.PropertiesUtil;

/**
 * @Description:
 * @Author linzt
 * @Date 2021年2月20日 0020
 */
public class TestGetArrayInProperties {
    public static void main(String[] args) {
        String arrName = PropertiesUtil.getPropType("unStableType");
        String[] split = arrName.split(", ");
        String arrName1 = PropertiesUtil.getPropType("stableType");
        String[] split1 = arrName1.split(", ");
        System.out.println();
    }
}
