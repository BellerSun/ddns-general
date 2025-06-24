package cn.sunyc.ddnsgeneral.service;

import cn.sunyc.ddnsgeneral.domain.db.MsgNotifierConfigDO;
import cn.sunyc.ddnsgeneral.enumeration.MsgNotifierType;
import cn.sunyc.ddnsgeneral.enumeration.MsgType;
import cn.sunyc.ddnsgeneral.sql.MsgNotifierConfigRepository;
import cn.sunyc.ddnsgeneral.tools.notifier.MsgNotifier;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * MsgNotifierConfigService 测试类
 * 测试消息通知配置服务的核心功能
 *
 * @author sun yu chao
 */
@SpringBootTest
@DisplayName("消息通知配置服务测试")
class MsgNotifierConfigServiceTest {

    @MockBean
    private MsgNotifierConfigRepository msgNotifierConfigRepository;

    @Mock
    private Map<String, MsgNotifier> msgNotifierMap;

    @Mock
    private MsgNotifier mockMsgNotifier;

    @Autowired
    private MsgNotifierConfigService msgNotifierConfigService;

    private MsgNotifierConfigDO enabledConfig;
    private MsgNotifierConfigDO disabledConfig;
    private JSONObject testParam;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        final String hookFeishu = System.getenv().get("TEST_HOOK_ID_FEISHU");
        if (hookFeishu == null || hookFeishu.isBlank()) {
            throw new IllegalStateException("TEST_HOOK_ID environment variable must be set for testing");
        }
        final String hookDingtalk = System.getenv().get("TEST_HOOK_ID_DINGTALK");
        if (hookDingtalk == null || hookDingtalk.isBlank()) {
            throw new IllegalStateException("TEST_HOOK_ID_DINGTALK environment variable must be set for testing");
        }
        
        // 初始化测试数据
        enabledConfig = new MsgNotifierConfigDO();
        enabledConfig.setId(1L);
        enabledConfig.setConfigName("测试配置1");
        enabledConfig.setNotifierType(MsgNotifierType.FEISHU);
        enabledConfig.setMsgType(MsgType.DDNS_UPDATE);
        enabledConfig.setHook(hookFeishu);
        enabledConfig.setMsgTemplate("DDNS更新成功：IP地址已更新为 ${ip}");
        enabledConfig.setEnable(true);

        disabledConfig = new MsgNotifierConfigDO();
        disabledConfig.setId(2L);
        disabledConfig.setConfigName("测试配置2");
        disabledConfig.setNotifierType(MsgNotifierType.DINGTALK);
        disabledConfig.setMsgType(MsgType.DDNS_UPDATE);
        disabledConfig.setHook(hookDingtalk);
        disabledConfig.setMsgTemplate("DDNS更新失败：错误信息 ${error}");
        disabledConfig.setEnable(true);

        testParam = new JSONObject();
        testParam.put("ip", "192.168.1.100");
        testParam.put("domain", "example.com");
        testParam.put("error", "某个测试的错误");
    }

    @Test
    @DisplayName("发送消息 - 根据消息类型发送 - 成功")
    void sendMsg_ByMsgType_Success() {
        // Given - 使用真实的Spring注入的service，但仍然mock repository来控制测试数据
        List<MsgNotifierConfigDO> allConfigs = Arrays.asList(enabledConfig, disabledConfig);
        when(msgNotifierConfigRepository.findAll()).thenReturn(allConfigs);

        // When - 调用真实的Spring注入的service
        msgNotifierConfigService.sendMsg(MsgType.DDNS_UPDATE, testParam);

        // Then - 验证repository被正确调用
        verify(msgNotifierConfigRepository, times(1)).findAll();
        
        // 注意：这里使用的是真实的Spring注入的service实例，而不是手动创建的mock对象
        // 消息发送的具体逻辑会由真实的notifier处理（如果配置了相应的bean）
    }

} 