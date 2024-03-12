package cn.sunyc.ddnsgeneral.service;

import cn.sunyc.ddnsgeneral.domain.db.IPCheckerConfigDO;
import cn.sunyc.ddnsgeneral.sql.IPCheckerConfigRepository;
import com.aliyun.credentials.http.MethodType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class IPCheckerConfigService {

    @Resource
    IPCheckerConfigRepository ipCheckerConfigRepository;

    public List<IPCheckerConfigDO> queryAll() {
        return ipCheckerConfigRepository.findAll();
    }

    /**
     * 查询第一个enable字段为true的，如果没有，返回默认配置
     */
    public IPCheckerConfigDO queryEnable() {
        IPCheckerConfigDO config = ipCheckerConfigRepository.findFirstByEnableTrue();
        if (config == null) {
            config = new IPCheckerConfigDO();
            config.setId(-1L);
            config.setConfigName("DEFAULT");
            config.setUrl("http://ip.apache.plus/");
            config.setMethodType(MethodType.POST);
            config.setRegex("\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b");
            config.setEnable(true);
        }
        return config;
    }

    /**
     * 启动一个配置，同时会关闭其他所有配置
     *
     * @param id 启用id
     */
    public boolean enableById(Long id) {
        if (null == id) {
            return false;
        }
        final List<IPCheckerConfigDO> all = ipCheckerConfigRepository.findAll();
        for (IPCheckerConfigDO ipCheckerConfigDO : all) {
            ipCheckerConfigDO.setEnable(Objects.equals(ipCheckerConfigDO.getId(), id));
        }
        ipCheckerConfigRepository.saveAll(all);
        return true;
    }

    /**
     * 保存配置，不会保存enable状态
     */
    public IPCheckerConfigDO save(IPCheckerConfigDO query) {
        final Long id = query.getId();
        if (null != id) {
            final IPCheckerConfigDO existing = ipCheckerConfigRepository.getById(id);
            query.setEnable(existing.getEnable());
        } else {
            query.setEnable(false);
        }
        return ipCheckerConfigRepository.save(query);
    }
}
