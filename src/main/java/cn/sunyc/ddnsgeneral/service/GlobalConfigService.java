package cn.sunyc.ddnsgeneral.service;

import cn.sunyc.ddnsgeneral.domain.db.GlobalConfigDO;
import cn.sunyc.ddnsgeneral.sql.GlobalConfigRepository;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author sun yu chao
 * @version 1.0
 * @since 2023/1/21 06:24
 */
@Service
public class GlobalConfigService {

    @Resource
    GlobalConfigRepository globalConfigRepository;

    /**
     * 查询配置
     *
     * @param key        配置key
     * @param clazz      配置类型
     * @param <T>        配置类型
     * @return 配置转换后的值
     */
    public <T> T queryConfig(String key, Class<T> clazz) {
        final Optional<GlobalConfigDO> doOptional = globalConfigRepository.findById(key);
        final String val = doOptional.map(GlobalConfigDO::getConfigVal).orElse(null);
        return JSON.parseObject(val, clazz);
    }

    /**
     * 查询配置
     *
     * @param key        配置key
     * @param clazz      配置类型
     * @param defaultVal 默认值
     * @param <T>        配置类型
     * @return 配置转换后的值
     */
    public <T> T queryConfig(String key, Class<T> clazz, T defaultVal) {
        return Optional.ofNullable(queryConfig(key, clazz)).orElse(defaultVal);
    }
}
