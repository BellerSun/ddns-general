package cn.sunyc.ddnsgeneral.core;

import cn.sunyc.ddnsgeneral.core.server.IDNSServer;
import cn.sunyc.ddnsgeneral.domain.SystemConstant;
import cn.sunyc.ddnsgeneral.domain.db.DDNSConfigDO;
import cn.sunyc.ddnsgeneral.domain.resolution.BaseResolutionRecord;
import cn.sunyc.ddnsgeneral.enumeration.DNSServerType;
import cn.sunyc.ddnsgeneral.service.LocalIpService;
import cn.sunyc.ddnsgeneral.sql.DDNSConfigRepository;
import cn.sunyc.ddnsgeneral.utils.MDCUtil;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.Assert;

import java.io.Closeable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * ddns执行器，每一个数据库的配置，会生成一个runner。
 */
@Slf4j
public class DDNSRunner<T extends BaseResolutionRecord> implements Closeable {
    private final ApplicationContext applicationContext;
    private final LocalIpService localIpService;
    private final DDNSConfigRepository ddnsConfigRepository;

    @Getter
    private DDNSConfigDO ddnsConfigDO;
    private IDNSServer<T> dnsServer;

    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public DDNSRunner(ApplicationContext applicationContext, LocalIpService localIpService, DDNSConfigRepository ddnsConfigRepository) {
        this.applicationContext = applicationContext;
        this.localIpService = localIpService;
        this.ddnsConfigRepository = ddnsConfigRepository;
    }

    @SuppressWarnings("unchecked")
    public void init(DDNSConfigDO ddnsConfigDO) {
        log.info("[DDNS_RUNNER] init DDNSRunner start. DDNSProperties:{}", ddnsConfigDO.toString());
        this.ddnsConfigDO = ddnsConfigDO;
        // 重复调用init的幂等逻辑
        close();
        Class<? extends IDNSServer<T>> dnsServerTypeByName = (Class<? extends IDNSServer<T>>) DNSServerType.getDNSServerTypeByName(ddnsConfigDO.getDnsServerType());
        if (null == dnsServerTypeByName) {
            throw new IllegalArgumentException("ddns server type config error, support ddns server type :" + DNSServerType.getValidNames());
        }

        try {
            //注意，实现类的Scope都是protoType
            this.dnsServer = applicationContext.getBean(dnsServerTypeByName);
            log.info("[DDNS_RUNNER] getBeanFinished. type:{}, instance:{}", dnsServerTypeByName.getSimpleName(), dnsServer);
        } catch (Exception ignore) {
            throw new IllegalArgumentException("ddns server bean get error, dis server type:" + dnsServerTypeByName + ", support ddns server type:" + DNSServerType.getValidNames());
        }
        log.info("[DDNS_RUNNER] init ddns server start.");
        this.dnsServer.init(JSON.parseObject(ddnsConfigDO.getDnsServerParam()));
        log.info("[DDNS_RUNNER] init ddns server end.");

        log.info("[DDNS_RUNNER] init thread pool task scheduler start.");
        this.threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        this.threadPoolTaskScheduler.initialize();
        this.threadPoolTaskScheduler.schedule(new DoDDNSRunnable(), new CronTrigger(this.ddnsConfigDO.getSchedulerCron()));
        log.info("[DDNS_RUNNER] init thread pool task scheduler end.");

        log.info("[DDNS_RUNNER] init DDNSRunner success.");
    }

    @Override
    public void close() {
        if (this.threadPoolTaskScheduler != null) {
            try {
                log.info("[DDNS_RUNNER] shutdown old threadPoolTaskScheduler.");
                this.threadPoolTaskScheduler.shutdown();
            } catch (Exception e) {
                log.error("[DDNS_RUNNER] shutdown threadPoolTaskScheduler error.", e);
            }
        }
    }

    @NoArgsConstructor
    private final class DoDDNSRunnable implements Runnable {

        private boolean runForce = false;

        public DoDDNSRunnable(boolean runForce) {
            this.runForce = runForce;
        }

        /**
         * 上次解析出来的ip
         */
        private String preRecordIp = "127.0.0.1";
        /**
         * 上次执行查询的时间
         */
        private long preRecordQueryTime = Long.MIN_VALUE;
        @Override
        public void run() {
            MDCUtil.setRequestId(UUID.randomUUID() + SystemConstant.COMMON_STR_SPLIT + ("【" + ddnsConfigDO.generateUniqueKey() + "】"));
            log.info("[DDNS_RUNNER] start. DDNSProperties:{}", ddnsConfigDO.toString());
            if (!runForce && !ddnsConfigDO.isActivate()) {
                log.info("[DDNS_RUNNER] end. not active");
                return;
            }
            try {
                this.runDDNS();
            } catch (Exception e) {
                // 运行中发现异常，异常信息
                log.error("[DDNS_RUNNER] error.", e);
            }
            log.info("[DDNS_RUNNER] standard end.");
        }

        private void runDDNS() throws Exception {
            // 查询当前外网ip
            final String nowOutSideIp = localIpService.getLocalOutSideIp();
            
            // 更新数据库中的lastQueryTime和lastIp
            this.updateDBIp(nowOutSideIp);

            // 当查询结果与上次记录解析结果相同，并且上次结果在有效期内，就等待(是担心别人通过其他渠道修改了解析记录，可是我们这里却不刷新)。
            Long aliveTime = ddnsConfigDO.getDdnsRecordAliveTime();
            if (nowOutSideIp.equalsIgnoreCase(preRecordIp) && aliveTime != null && (System.currentTimeMillis() - preRecordQueryTime) < aliveTime) {
                log.info("[DDNS_RUNNER] in cache time. end.");
            }

            // 查询域名解析记录
            final List<T> baseResolutionRecords = dnsServer.queryList(ddnsConfigDO.getDdnsConfigKey().getDomainName());
            Assert.notNull(baseResolutionRecords, "查询解析记录失败，返回结果为null。");
            final List<T> targetRecords = baseResolutionRecords.stream().filter(Objects::nonNull)
                    .filter(record -> ddnsConfigDO.getDdnsConfigKey().getDomainName().equals(record.getDomain()))
                    .filter(record -> ddnsConfigDO.getDdnsConfigKey().getDomainSubName().equals(record.getSubDomain()))
                    .filter(record -> ddnsConfigDO.getDdnsDomainRecordType().equals(record.getRecordType()))
                    .collect(Collectors.toList());
            Assert.isTrue(targetRecords.size() <= 1, "您的输入条件查询出来了多条记录，俺不知道要修改哪一个了。");
            Assert.isTrue(!targetRecords.isEmpty(), "您的输入条件没有查询到记录，俺不知道要修改哪一个了。");

            //  记录查询成功，刷新查询结果，和查询时间
            final T baseResolutionRecord = targetRecords.get(0);
            preRecordIp = baseResolutionRecord.getValue();
            preRecordQueryTime = System.currentTimeMillis();

            //  如果发现不相等，就修改解析记录
            if (!nowOutSideIp.equalsIgnoreCase(preRecordIp)) {
                // 这里发现不相等，不会刷新上次查询时间、结果。因为正好下次循环就重新查一下，这里修改成功了没。
                baseResolutionRecord.setValue(nowOutSideIp);
                dnsServer.updateResolutionRecord(targetRecords.get(0));
                log.info("[DDNS_RUNNER] UPDATE_RECORD. type:{}, domain:【{}】, fromIP:{}, toIP:{}", ddnsConfigDO.getDnsServerType(), ddnsConfigDO.generateUniqueKey(), preRecordIp, nowOutSideIp);
            }else {
                log.info("[DDNS_RUNNER] NOT_UPDATE_RECORD. type:{}, domain:【{}】, fromIP:{}, toIP:{}", ddnsConfigDO.getDnsServerType(), ddnsConfigDO.generateUniqueKey(), preRecordIp, nowOutSideIp);
            }
        }

        private void updateDBIp(String nowOutSideIp) {
            try {
                ddnsConfigDO.setLastQueryTime(System.currentTimeMillis());
                ddnsConfigDO.setLastIp(nowOutSideIp);
                ddnsConfigRepository.save(ddnsConfigDO);
                log.info("[DDNS_RUNNER] UPDATE_DB_IP_INFO. domain:【{}】, ip:{}, time:{}", ddnsConfigDO.generateUniqueKey(), nowOutSideIp, ddnsConfigDO.getLastQueryTime());
            } catch (Exception e) {
                log.error("[DDNS_RUNNER] UPDATE_DB_IP_INFO_ERROR. domain:【{}】", ddnsConfigDO.generateUniqueKey(), e);
            }
        }
    }

    /**
     * 立即执行一次 
     */
    public void runNow() {
        log.info("[DDNS_RUNNER] runNow start.");
        new DoDDNSRunnable(true).run();
        log.info("[DDNS_RUNNER] runNow end.");
    }


}
