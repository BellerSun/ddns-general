package cn.sunyc.ddnsgeneral.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 18:30
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
@Getter
@Component
public class SystemProperties {
    /**
     * dns服务器类型
     */
    @Value("${sunyc.dns.server.type}")
    private String dnsServerType;

    /**
     * dns服务器必须参数
     */
    @Value("${sunyc.dns.server.param}")
    private String dnsServerParam;

    /**
     * 动态解析的主域名【baidu.com】
     */
    @Value("${sunyc.ddns.domain.name}")
    private String ddnsDomainName;

    /**
     * 动态解析的子域名【www、wiki、@、*】
     */
    @Value("${sunyc.ddns.domain.subName}")
    private String ddnsDomainSubName;

    /**
     * 查询出来的解析记录，生存时间。避免每次都去查，过了这个时间长度就不相信该结果了
     */
    @Value("${sunyc.ddns.record.aliveTime:600000}")
    private long ddnsRecordAliveTime;

    /**
     * 记录类型
     */
    @Value("${sunyc.ddns.domain.recordType:A}")
    private String ddnsDomainRecordType;

    /**
     * 定时调度字符串
     */
    @Value("${sunyc.scheduler.cron}")
    private String schedulerCron;

    @Setter
    @Value("${sunyc.system.activate:true}")
    private boolean activate = true;

}
