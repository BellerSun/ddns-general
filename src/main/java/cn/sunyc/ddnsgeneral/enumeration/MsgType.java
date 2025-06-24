package cn.sunyc.ddnsgeneral.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum MsgType {
    DDNS_UPDATE("ddns更新", "当ddns更新时候会发送该提醒消息"
            , Map.of(
            "timeStr", "更新时间的字符串，格式为yyyy-MM-dd HH:mm:ss",
            "domain", "主域名",
            "subDomain", "子域名",
            "ip_old", "旧IP地址",
            "ip_new", "新IP地址"
    )),
    ERROR("运行错误", "当任务发生错误时候会发送该提醒消息"
            , Map.of(
            "timeStr", "错误发生时间的字符串，格式为yyyy-MM-dd HH:mm:ss",
            "msg", "错误信息"
    )),
    ;

    private final String name;
    private final String desc;
    private final Map<String, String> params;
}
