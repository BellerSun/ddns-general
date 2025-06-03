package cn.sunyc.ddnsgeneral.service;

import cn.sunyc.ddnsgeneral.domain.db.IPCheckerConfigDO;

/**
 * 本地ip获取服务，主要是避免多个地方频繁重复调用ip查询接口
 *
 * @author sun yu chao
 * @version 1.0
 * @since 2023/1/21 06:13
 */
public interface LocalIpService {

    /**
     * 带缓存的获取本地的外网ip地址
     */
    String getLocalOutSideIp();

    String getLocalOutSideIp(IPCheckerConfigDO ipCheckerConfig);
}
