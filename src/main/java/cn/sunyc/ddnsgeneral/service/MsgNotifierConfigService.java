package cn.sunyc.ddnsgeneral.service;

import cn.sunyc.ddnsgeneral.domain.db.MsgNotifierConfigDO;
import cn.sunyc.ddnsgeneral.enumeration.MsgType;
import cn.sunyc.ddnsgeneral.sql.MsgNotifierConfigRepository;
import cn.sunyc.ddnsgeneral.tools.notifier.MsgNotifier;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 消息通知配置服务
 *
 * @author sun yu chao
 * @version 1.0
 */
@Slf4j
@Service
@AllArgsConstructor
public class MsgNotifierConfigService {

    @Resource
    private final MsgNotifierConfigRepository msgNotifierConfigRepository;

    @Resource
    private final Map<String, MsgNotifier> msgNotifierMap;

    public void sendMsg(MsgType msgType, JSONObject param) {
        final List<MsgNotifierConfigDO> configs = queryAllEnabled();
        final List<MsgNotifierConfigDO> configsByType = configs.stream()
                .filter(config -> config.getMsgType() == msgType)
                .toList();
        for (MsgNotifierConfigDO notifierConfigDO : configsByType) {
            final String notifierType = notifierConfigDO.getNotifierType().name();
            final String hook = notifierConfigDO.getHook();
            final String msgTemplate = notifierConfigDO.getMsgTemplate();
            if (hook == null || hook.isBlank()) {
                log.error("[MSG_NOTIFIER_SERVICE] hook is empty for notifierType: {}", notifierType);
            }
            if (msgTemplate == null || msgTemplate.isBlank()) {
                log.error("[MSG_NOTIFIER_SERVICE] msgTemplate is empty for notifierType: {}", notifierType);
            }
            try {
                this.sendMsg(msgType, notifierType, hook, msgTemplate, param);
            } catch (Exception e) {
                log.error(
                        "[MSG_NOTIFIER_SERVICE] sendMsg error, notifierType: {}, hook: {}, msgTemplate: {}, param: {}",
                        notifierType, hook, msgTemplate, param, e);
            }
        }
    }

    private boolean sendMsg(MsgType msgType, String notifierType, String hook, String msgTemplate, JSONObject param) {
        final MsgNotifier msgNotifier = msgNotifierMap.get(notifierType);
        if (msgNotifier == null) {
            throw new IllegalArgumentException("[MSG_NOTIFIER_SERVICE] notifierType: " + notifierType + " not found");
        }
        return msgNotifier.sendMsg(hook, msgType, msgTemplate, param);
    }

    /**
     * 查询所有消息通知配置
     *
     * @return 所有消息通知配置列表
     */
    public List<MsgNotifierConfigDO> queryAll() {
        return msgNotifierConfigRepository.findAll();
    }

    /**
     * 查询所有启用的消息通知配置
     *
     * @return 启用的消息通知配置列表
     */
    public List<MsgNotifierConfigDO> queryAllEnabled() {
        return msgNotifierConfigRepository.findAll().stream()
                .filter(config -> config.getEnable() != null && config.getEnable())
                .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 根据ID查询配置
     *
     * @param id 配置ID
     * @return 消息通知配置
     */
    public Optional<MsgNotifierConfigDO> queryById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return msgNotifierConfigRepository.findById(id);
    }

    /**
     * 保存消息通知配置
     *
     * @param msgNotifierConfigDO 消息通知配置
     * @return 保存后的配置
     */
    public synchronized MsgNotifierConfigDO save(MsgNotifierConfigDO msgNotifierConfigDO) {
        if (msgNotifierConfigDO == null) {
            throw new IllegalArgumentException("消息通知配置不能为空");
        }
        return msgNotifierConfigRepository.save(msgNotifierConfigDO);
    }

    /**
     * 根据ID更新配置信息
     *
     * @param id           配置ID
     * @param updateConfig 要更新的配置信息
     * @return 更新后的配置
     */
    public synchronized MsgNotifierConfigDO updateById(Long id, MsgNotifierConfigDO updateConfig) {
        if (id == null) {
            throw new IllegalArgumentException("配置ID不能为空");
        }
        if (updateConfig == null) {
            throw new IllegalArgumentException("更新配置不能为空");
        }

        Optional<MsgNotifierConfigDO> existingOpt = msgNotifierConfigRepository.findById(id);

        MsgNotifierConfigDO existing = generateUpdateDO(updateConfig, existingOpt);

        return msgNotifierConfigRepository.save(existing);
    }

    @NotNull
    private static MsgNotifierConfigDO generateUpdateDO(MsgNotifierConfigDO updateConfig,
                                                        Optional<MsgNotifierConfigDO> existingOpt) {
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("配置不存在");
        }
        MsgNotifierConfigDO existing = existingOpt.get();

        // 更新字段
        if (updateConfig.getConfigName() != null) {
            existing.setConfigName(updateConfig.getConfigName());
        }
        if (updateConfig.getNotifierType() != null) {
            existing.setNotifierType(updateConfig.getNotifierType());
        }
        if (updateConfig.getHook() != null) {
            existing.setHook(updateConfig.getHook());
        }
        if (updateConfig.getMsgTemplate() != null) {
            existing.setMsgTemplate(updateConfig.getMsgTemplate());
        }
        if (updateConfig.getEnable() != null) {
            existing.setEnable(updateConfig.getEnable());
        }
        return existing;
    }

    /**
     * 根据ID删除配置
     *
     * @param id 配置ID
     */
    public synchronized void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("配置ID不能为空");
        }

        if (!msgNotifierConfigRepository.existsById(id)) {
            throw new IllegalArgumentException("配置不存在，ID: " + id);
        }

        msgNotifierConfigRepository.deleteById(id);
    }

    /**
     * 启用/禁用配置
     *
     * @param id     配置ID
     * @param enable 是否启用
     * @return 更新后的配置
     */
    public synchronized MsgNotifierConfigDO toggleEnable(Long id, Boolean enable) {
        if (id == null) {
            throw new IllegalArgumentException("配置ID不能为空");
        }

        Optional<MsgNotifierConfigDO> configOpt = msgNotifierConfigRepository.findById(id);
        if (configOpt.isEmpty()) {
            throw new IllegalArgumentException("配置不存在，ID: " + id);
        }

        MsgNotifierConfigDO config = configOpt.get();
        config.setEnable(enable);
        return msgNotifierConfigRepository.save(config);
    }

    /**
     * 发送测试消息
     *
     * @param msgNotifierConfigDO 消息通知配置对象
     * @return 是否发送成功
     */
    public boolean sendMsg(MsgNotifierConfigDO msgNotifierConfigDO) {
        if (msgNotifierConfigDO == null) {
            throw new IllegalArgumentException("消息通知配置不能为空");
        }

        final String notifierType = msgNotifierConfigDO.getNotifierType().name();
        final String hook = msgNotifierConfigDO.getHook();
        final String msgTemplate = msgNotifierConfigDO.getMsgTemplate();
        final MsgType msgType = msgNotifierConfigDO.getMsgType();

        if (hook == null || hook.isBlank()) {
            throw new IllegalArgumentException("Hook不能为空");
        }
        if (msgTemplate == null || msgTemplate.isBlank()) {
            throw new IllegalArgumentException("消息模板不能为空");
        }
        if (msgType == null) {
            throw new IllegalArgumentException("消息类型不能为空");
        }

        try {
            final String msg = msgTemplate.replace("$", "");
            return this.sendMsg(msgType, notifierType, hook, msg, new JSONObject());
        } catch (Exception e) {
            log.error("[MSG_NOTIFIER_SERVICE] sendMsg error for config: {}",
                    msgNotifierConfigDO.getConfigName(), e);
            return false;
        }
    }
}