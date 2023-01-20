package cn.sunyc.ddnsgeneral.service;

import cn.sunyc.ddnsgeneral.domain.GlobalConfigKey;
import cn.sunyc.ddnsgeneral.utils.IpUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 本地ip获取服务，主要是避免多个地方频繁重复调用ip查询接口
 *
 * @author sun yu chao
 * @version 1.0
 * @since 2023/1/21 06:13
 */
@Service
public class LocalIpService {

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
