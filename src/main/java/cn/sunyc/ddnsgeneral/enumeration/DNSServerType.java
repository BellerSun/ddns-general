package cn.sunyc.ddnsgeneral.enumeration;

import cn.sunyc.ddnsgeneral.core.server.IDNSServer;
import cn.sunyc.ddnsgeneral.core.server.TencentDNSServer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 支持的dns服务器api类型
 *
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 19:05
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
@Getter
@AllArgsConstructor
public enum DNSServerType {
    /**
     * 腾讯云
     */
    TENCENT("TENCENT", TencentDNSServer.class),
    /**
     * 华为云
     */
    HUAWEI("HUAWEI", null),
    /**
     * 阿里云
     */
    ALIBABA("ALIBABA", null),
    ;
    /**
     * 对应配置的名字
     */
    private final String name;
    /**
     * 对应的解析器
     */
    private final Class<? extends IDNSServer> dnsServerClass;

    /**
     * 根据配置名称给出一个对应的解析器类型
     * @param name 配置名称
     * @return 解析器类型
     */
    public static Class<? extends IDNSServer> getDNSServerTypeByName(String name) {
        DNSServerType[] serverTypes = values();
        for (DNSServerType serverType : serverTypes) {
            if (serverType.name.equalsIgnoreCase(name)){
                return serverType.dnsServerClass;
            }
        }
        return null;
    }

    /**
     * 获取所有支持类型的名称
     * @return 支持类型的名称
     */
    public static List<String> getValidNames(){
       return Arrays.stream(values()).map(DNSServerType::getName).collect(Collectors.toList());
    }
}
