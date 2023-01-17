package cn.sunyc.ddnsgeneral.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * ip相关工具类
 *
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 16:29
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
@Slf4j
public class IpUtil {

    /**
     * ip查询服务的地址
     */
    private static final String IP_SERVER_URL = "http://ip.apache.plus/";

    /**
     * 获取本机的外网ip，出现任何异常都会返回默认ip 127.0.0.1
     *
     * @return 本机的外网ip
     */
    public static String getOutSideIp() {
        try {
            return HttpUtil.post(IP_SERVER_URL, null);
        } catch (Exception e) {
            throw new RuntimeException("[IP_UTIL] getOutSideIp error.", e);
        }
    }
}
