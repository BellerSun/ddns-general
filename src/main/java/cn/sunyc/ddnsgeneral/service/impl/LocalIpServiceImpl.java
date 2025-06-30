package cn.sunyc.ddnsgeneral.service.impl;

import cn.sunyc.ddnsgeneral.domain.GlobalConfigKey;
import cn.sunyc.ddnsgeneral.domain.db.IPCheckerConfigDO;
import cn.sunyc.ddnsgeneral.service.GlobalConfigService;
import cn.sunyc.ddnsgeneral.service.IPCheckerConfigService;
import cn.sunyc.ddnsgeneral.service.LocalIpService;
import cn.sunyc.ddnsgeneral.utils.HttpUtil;
import com.aliyun.credentials.http.MethodType;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LocalIpServiceImpl implements LocalIpService {

    @Resource
    GlobalConfigService globalConfigService;
    @Resource
    IPCheckerConfigService ipCheckerConfigService;

    private static final ConcurrentHashMap<String, Pattern> patternCache = new ConcurrentHashMap<>();

    private static final Map<Long, Pair<String, Long>> idResultCache = new ConcurrentHashMap<>();

    /**
     * 带缓存的获取本地的外网ip地址
     */
    @Override
    public String getLocalOutSideIp() {
        final Long cacheTime = globalConfigService.queryConfig(GlobalConfigKey.LOCAL_IP_CACHE_SECONDS, Long.class, 10L);
        final IPCheckerConfigDO ipCheckerConfig = ipCheckerConfigService.queryEnable();
        final Long id = ipCheckerConfig.getId();
        // 要是缓存有，并且没过期，直接返回
        if (idResultCache.containsKey(id) && (System.currentTimeMillis() - idResultCache.get(id).getSecond()) < (cacheTime * 1000)) {
            return idResultCache.get(id).getFirst();
        }

        return getLocalOutSideIp(ipCheckerConfig);
    }

    @Override
    public String getLocalOutSideIp(IPCheckerConfigDO ipCheckerConfig) {
        final Long id = Optional.ofNullable(ipCheckerConfig.getId()).orElse(-2L);
        final String url = ipCheckerConfig.getUrl();
        final MethodType methodType = ipCheckerConfig.getMethodType();
        final String regex = ipCheckerConfig.getRegex();

        final String resp = this.queryHttpResult(url, methodType);
        Assert.notNull(resp, "[IP_SERVICE] queryHttpResult error, url:" + url);

        // 根据配置的正则，找到ip。加入或者更新缓存，返回结果
        final Pattern pattern = patternCache.computeIfAbsent(regex, Pattern::compile);
        final Matcher matcher = pattern.matcher(resp);
        if (matcher.find()) {
            String ip = matcher.group();
            idResultCache.put(id, Pair.of(ip, System.currentTimeMillis()));
            return ip;
        }
        throw new RuntimeException("[IP_SERVICE] No IP found in the regex:【" + regex + "】,\tresp:【" + resp + "】");
    }

    private String queryHttpResult(String url, MethodType methodType) {
        int maxRetries = 5;
        int retryInterval = 2000; // 2秒
        
        for (int i = 0; i < maxRetries; i++) {
            try {
                if (methodType == MethodType.GET) {
                    return HttpUtil.get(url, new HashMap<>());
                } else {
                    return HttpUtil.post(url, new HashMap<>());
                }
            } catch (Exception e) {
                if (i == maxRetries - 1) {
                    // 最后一次重试失败，抛出异常
                    throw new RuntimeException("[IP_SERVICE] getLocalOutSideIp error after " + maxRetries + " retries, url:" + url, e);
                }
                
                // 不是最后一次重试，等待后继续
                try {
                    Thread.sleep(retryInterval);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("[IP_SERVICE] Retry interrupted, url:" + url, interruptedException);
                }
            }
        }
        
        // 理论上不会执行到这里
        throw new RuntimeException("[IP_SERVICE] Unexpected error, url:" + url);
    }

}
