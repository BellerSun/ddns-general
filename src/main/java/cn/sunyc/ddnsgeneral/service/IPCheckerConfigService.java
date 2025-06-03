package cn.sunyc.ddnsgeneral.service;

import cn.sunyc.ddnsgeneral.domain.db.IPCheckerConfigDO;
import cn.sunyc.ddnsgeneral.sql.IPCheckerConfigRepository;
import com.aliyun.credentials.http.MethodType;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class IPCheckerConfigService {

    @Resource
    IPCheckerConfigRepository ipCheckerConfigRepository;

    public List<IPCheckerConfigDO> queryAll() {
        final List<IPCheckerConfigDO> all = ipCheckerConfigRepository.findAll();
        final List<IPCheckerConfigDO> result = new ArrayList<>();
        IPCheckerConfigDO defaultConfig = this.generateDefaultConfig();
        defaultConfig.setEnable(all.stream().noneMatch(IPCheckerConfigDO::getEnable));
        result.add(defaultConfig);
        result.addAll(all);
        return result;
    }

    /**
     * 查询第一个enable字段为true的，如果没有，返回默认配置
     */
    public synchronized IPCheckerConfigDO queryEnable() {
        IPCheckerConfigDO config = ipCheckerConfigRepository.findFirstByEnableTrue();
        if (config == null) {
            config = this.generateDefaultConfig();
        }
        return config;
    }

    private IPCheckerConfigDO generateDefaultConfig() {
        final IPCheckerConfigDO config = new IPCheckerConfigDO();
        config.setId(-1L);
        config.setConfigName("DEFAULT");
        config.setUrl("http://ip.apache.plus");
        config.setMethodType(MethodType.POST);
        config.setRegex("\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b");
        config.setEnable(true);
        return config;
    }

    /**
     * 启动一个配置，同时会关闭其他所有配置
     *
     * @param id 启用id
     */
    public synchronized boolean enableById(Long id) {
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
    public synchronized IPCheckerConfigDO save(IPCheckerConfigDO query) {
        final Long id = query.getId();

        if (null != id) {
            if (id < 0) {
                throw new IllegalArgumentException("Could not update default IPChecker");
            }

            final Optional<IPCheckerConfigDO> existingOpt = ipCheckerConfigRepository.findById(id);
            if (existingOpt.isPresent()) {
                IPCheckerConfigDO existing = existingOpt.get();
                query.setEnable(existing.getEnable());
            } else {
                throw new IllegalArgumentException("IPChecker config not found with id: " + id);
            }
        } else {
            // 对于新增记录，确保ID为null，让数据库自动生成
            query.setId(null);
            query.setEnable(false);
        }
        return ipCheckerConfigRepository.save(query);
    }

    /**
     * 根据ID删除配置
     *
     * @param id 配置ID
     */
    public synchronized void deleteById(Long id) {
        if (null == id || id < 0) {
            throw new IllegalArgumentException("Could not delete default IPChecker");
        }
        ipCheckerConfigRepository.deleteById(id);
    }
}
