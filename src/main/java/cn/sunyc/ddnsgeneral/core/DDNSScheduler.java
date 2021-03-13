package cn.sunyc.ddnsgeneral.core;

import cn.sunyc.ddnsgeneral.config.SystemProperties;
import cn.sunyc.ddnsgeneral.core.server.IDNSServer;
import cn.sunyc.ddnsgeneral.domain.ResolutionRecord;
import cn.sunyc.ddnsgeneral.domain.SystemOperationRecord;
import cn.sunyc.ddnsgeneral.domain.SystemStatus;
import cn.sunyc.ddnsgeneral.enumeration.DNSServerType;
import cn.sunyc.ddnsgeneral.enumeration.OperationType;
import cn.sunyc.ddnsgeneral.utils.IpUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * DDNS的定时调度器
 *
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 18:26
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
@Component
public class DDNSScheduler implements ApplicationContextAware {
    @Resource
    private SystemStatus systemStatus;
    @Resource
    private SystemProperties systemProperties;

    private ApplicationContext applicationContext;
    private IDNSServer dnsServer;


    private String preRecordIp = "127.0.0.1";
    private long preRecordQueryTime = Long.MIN_VALUE;


    @PostConstruct()
    public void init() {
        System.out.println("尝试启动定时调度~");

        Class<? extends IDNSServer> dnsServerTypeByName = DNSServerType.getDNSServerTypeByName(systemProperties.getDnsServerType());
        if (null == dnsServerTypeByName) {
            throw new IllegalArgumentException("服务类型配置错误。key: sunyc.dns.server.type,支持类型:" + DNSServerType.getValidNames());
        }

        try {
            this.dnsServer = applicationContext.getBean(dnsServerTypeByName);
            if (null == this.dnsServer) {
                throw new IllegalArgumentException("服务类型没有找到对应的bean,配置类型:" + dnsServerTypeByName + "，支持类型:" + DNSServerType.getValidNames());
            }
        } catch (Exception ignore) {
            throw new IllegalArgumentException("服务类型没有找到对应的bean,配置类型:" + dnsServerTypeByName + "，支持类型:" + DNSServerType.getValidNames());
        }
        System.out.println("初始化dnsAPI服务...");
        this.dnsServer.init(JSON.parseObject(systemProperties.getDnsServerParam()));
        System.out.println("初始化dnsAPI服务完成！");
        System.out.println("定时调度启动成功！");

    }

    @Scheduled(cron = "#{systemProperties.schedulerCron}")
    public void doDDNS() {
        systemStatus.getOperationRecords().offer(new SystemOperationRecord(OperationType.START, "开始"));
        if (!systemProperties.isActivate()) {
            systemStatus.getOperationRecords().offer(new SystemOperationRecord(OperationType.INACTIVATE, "系统未激活"));
            systemStatus.getOperationRecords().offer(new SystemOperationRecord(OperationType.END, "结束"));
            return;
        }
        try {
            // 查询当前外网ip
            String nowOutSideIp = IpUtil.getOutSideIp();
            systemStatus.getOperationRecords().offer(new SystemOperationRecord(OperationType.QUERY_IP, nowOutSideIp));

            // 当查询结果与上次记录解析结果相同，并且上次结果在有效期内，就等待(是担心别人通过其他渠道修改了解析记录，可是我们这里却不刷新)。
            if (nowOutSideIp.equalsIgnoreCase(preRecordIp) && (System.currentTimeMillis() - preRecordQueryTime) < systemProperties.getDdnsRecordAliveTime()) {
                return;
            }

            // 查询域名解析记录
            List<ResolutionRecord> resolutionRecords = dnsServer.queryList(systemProperties.getDdnsDomainName());
            systemStatus.getOperationRecords().offer(new SystemOperationRecord(OperationType.QUERY_RECORD, JSON.toJSONString(resolutionRecords)));
            List<ResolutionRecord> targetRecords = resolutionRecords.stream()
                    .filter(Objects::nonNull)
                    .filter(record -> systemProperties.getDdnsDomainName().equals(record.getDomain()))
                    .filter(record -> systemProperties.getDdnsDomainSubName().equals(record.getSubDomain()))
                    .filter(record -> systemProperties.getDdnsDomainRecordType().equals(record.getRecordType()))
                    .collect(Collectors.toList());
            if (targetRecords.size() > 1) {
                throw new RuntimeException("您的输入条件查询出来了多条记录，俺不知道要修改哪一个了。");
            } else if (targetRecords.size() < 1) {
                throw new RuntimeException("您的输入条件没有查询到记录，俺不知道要修改哪一个了。");
            }
            //  记录查询成功，刷新查询结果，和查询时间
            ResolutionRecord resolutionRecord = targetRecords.get(0);
            this.preRecordIp = resolutionRecord.getValue();
            this.preRecordQueryTime = System.currentTimeMillis();

            //  如果发现不相等，就修改解析记录
            if (!nowOutSideIp.equalsIgnoreCase(preRecordIp)) {
                // 这里发现不相等，不会刷新上次查询时间、结果。因为正好下次循环就重新查一下，这里修改成功了没。
                resolutionRecord.setValue(nowOutSideIp);
                dnsServer.updateResolutionRecord(resolutionRecord);
                systemStatus.getOperationRecords().offer(new SystemOperationRecord(OperationType.UPDATE_RECORD, "fromIP:" + preRecordIp + "\ttoIP:" + nowOutSideIp));
            }
        } catch (Exception e) {
            // 运行中发现异常，异常信息压入队列
            systemStatus.getOperationRecords().offer(new SystemOperationRecord(OperationType.ERROR, e.getMessage()));
        }
        systemStatus.getOperationRecords().offer(new SystemOperationRecord(OperationType.END, "结束"));
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
