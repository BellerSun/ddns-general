package cn.sunyc.ddnsgeneral.core;

import cn.sunyc.ddnsgeneral.domain.db.DDNSConfigDO;
import cn.sunyc.ddnsgeneral.domain.db.key.DDNSConfigKey;
import cn.sunyc.ddnsgeneral.service.LocalIpService;
import cn.sunyc.ddnsgeneral.sql.DDNSConfigRepository;
import cn.sunyc.ddnsgeneral.utils.MDCUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用来管理各个{@link DDNSRunner}的。
 */
@Slf4j
@Component
public class DDNSRunnerManager implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Resource
    private DDNSConfigRepository ddnsConfigRepository;

    @Resource
    private LocalIpService localIpService;

    /**
     * 当前正在运行的runner。既方便查看、管理当前运行的runner，也可以减少新建runner
     */
    private final HashMap<String, DDNSRunner<?>> runnerCacheMap = new HashMap<>();

    @PostConstruct()
    public void init() {
        MDCUtil.setRequestId();
        log.info("[DDNS_RUNNER_MANAGER] start.");

        //首先查询所有配置
        final List<DDNSConfigDO> ddnsConfigList = ddnsConfigRepository.findAll();
        if (CollectionUtils.isEmpty(ddnsConfigList)) {
            log.info("[DDNS_RUNNER_MANAGER] ddns config empty.");
            return;
        }
        log.info("[DDNS_RUNNER_MANAGER] ddns config count:{}", ddnsConfigList.size());
        ddnsConfigList.forEach(config -> log.info("[DDNS_RUNNER_MANAGER] ddns config detail:{}", JSON.toJSONString(config)));


        // 启动所有，并根据唯一键添加到cache
        ddnsConfigList.stream().filter(Objects::nonNull)
                .forEach(this::refreshDDNSRunner);


        log.info("[DDNS_RUNNER_MANAGER] end.");
    }


    public void refreshDDNSRunner(DDNSConfigDO ddnsConfigDO) {
        final String uniqueKey = ddnsConfigDO.generateUniqueKey();
        log.info("[DDNS_RUNNER_MANAGER] refresh ddns runner. uniqueKey:{}", uniqueKey);
        final DDNSRunner<?> ddnsRunner = runnerCacheMap.getOrDefault(uniqueKey, new DDNSRunner<>(this.applicationContext, localIpService));
        log.info("[DDNS_RUNNER_MANAGER] refresh ddns runner. before:{}, after:{}", JSON.toJSONString(ddnsRunner.getDdnsConfigDO()), JSON.toJSONString(ddnsConfigDO));
        try {
            ddnsRunner.init(ddnsConfigDO);
            log.info("[DDNS_RUNNER_MANAGER] refresh ddns runner. success. ");
        } catch (Exception e) {
            log.error("[DDNS_RUNNER_MANAGER] init fail. ddnsProperties:{}", JSON.toJSONString(ddnsConfigDO), e);
        }
        runnerCacheMap.put(uniqueKey, ddnsRunner);
    }

    public void removeDDNSRunner(DDNSConfigKey ddnsConfigKey) {
        final String uniqueKey = ddnsConfigKey.generateUniqueKey();
        final DDNSRunner<?> ddnsRunner = runnerCacheMap.get(uniqueKey);
        if (null == ddnsRunner) {
            return;
        }
        ddnsRunner.close();
    }


    @Override
    @SuppressWarnings("all")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public List<DDNSConfigDO> queryRunningList() {
        return runnerCacheMap.values().stream().map(DDNSRunner::getDdnsConfigDO).collect(Collectors.toList());
    }


    public void runNow(DDNSConfigKey ddnsConfigKey) {
        final String uniqueKey = ddnsConfigKey.generateUniqueKey();
        final DDNSRunner<?> ddnsRunner = runnerCacheMap.get(uniqueKey);
        if (null == ddnsRunner) {
            log.error("[DDNS_RUNNER_MANAGER] runNow fail. ddns runner not found. uniqueKey:{}", uniqueKey);
            throw new RuntimeException("ddns runner not found. uniqueKey:" + uniqueKey);
        }
        ddnsRunner.runNow();
    }

}
