package cn.sunyc.ddnsgeneral.service.impl;

import cn.sunyc.ddnsgeneral.domain.GlobalConfigKey;
import cn.sunyc.ddnsgeneral.service.GlobalConfigService;
import cn.sunyc.ddnsgeneral.service.LocalIpService;
import cn.sunyc.ddnsgeneral.utils.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LocalIpServiceImpl implements LocalIpService {

    @Resource
    GlobalConfigService globalConfigService;

    String preIp = IpUtil.getOutSideIp();
    long preTime = System.currentTimeMillis();

    /**
     * 带缓存的获取本地的外网ip地址
     */
    public String getLocalOutSideIp() {
        final Long cacheTime = globalConfigService.queryConfig(GlobalConfigKey.LOCAL_IP_CACHE_SECONDS, Long.class, 10L);

        if ((System.currentTimeMillis() - preTime) < (cacheTime * 1000)) {
            return preIp;
        }

        preIp = IpUtil.getOutSideIp();
        preTime = System.currentTimeMillis();
        return preIp;
    }
}
