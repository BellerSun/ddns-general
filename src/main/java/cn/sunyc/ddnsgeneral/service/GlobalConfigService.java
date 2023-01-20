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


    public <T> T queryConfig(String key, Class<T> clazz) {
        final Optional<GlobalConfigDO> doOptional = globalConfigRepository.findById(key);
        final String val = doOptional.map(GlobalConfigDO::getConfigVal).orElse(null);
        return JSON.parseObject(val, clazz);
    }

}
