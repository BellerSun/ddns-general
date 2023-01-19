package cn.sunyc.ddnsgeneral.service;

import cn.sunyc.ddnsgeneral.core.DDNSRunnerManager;
import cn.sunyc.ddnsgeneral.domain.db.DDNSConfigDO;
import cn.sunyc.ddnsgeneral.domain.db.key.DDNSConfigKey;
import cn.sunyc.ddnsgeneral.sql.DDNSConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 配置管理实现
 * 项目较小，这里就不区分接口和实现层了。
 */
@Slf4j
@Service
public class DDNSConfigService {

    @Resource
    DDNSRunnerManager ddnsRunnerManager;

    @Resource
    private DDNSConfigRepository ddnsConfigRepository;

    public boolean saveDDNSConfig(DDNSConfigDO ddnsConfigDO) {
        log.info("[DDNSConfigService] save db ddns runner. ddnsConfigDO:{}", ddnsConfigDO);
        final DDNSConfigDO configDO = ddnsConfigRepository.save(ddnsConfigDO);
        ddnsRunnerManager.refreshDDNSRunner(configDO);
        return true;
    }

    public List<DDNSConfigDO> queryAll() {
        return ddnsConfigRepository.findAll();
    }


    public void remove(DDNSConfigKey ddnsConfigKey) {
        ddnsConfigRepository.deleteById(ddnsConfigKey);
        ddnsRunnerManager.removeDDNSRunner(ddnsConfigKey);
    }
}
