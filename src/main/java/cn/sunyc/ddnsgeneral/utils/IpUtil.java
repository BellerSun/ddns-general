package cn.sunyc.ddnsgeneral.utils;

/**
 * ip相关工具类
 *
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 16:29
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
public class IpUtil {
    /**
     * 本机默认ip
     */
    private static final String DEFAULT_IP = "127.0.0.1";
    /**
     * ip查询服务的地址
     */
    private static final String IP_SERVER_URL = "http://ip.sunyc.cn";

    /**
     * 获取本机的外网ip，出现任何异常都会返回默认ip 127.0.0.1
     *
     * @return 本机的外网ip
     */
    public static String getOutSideIp() {
        try {
            return HttpUtil.post(IP_SERVER_URL, null);
        } catch (Exception ignore) {
        }
        return DEFAULT_IP;
    }
}
