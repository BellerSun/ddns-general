package cn.sunyc.ddnsgeneral.controller;

import cn.sunyc.ddnsgeneral.core.DDNSRunnerManager;
import cn.sunyc.ddnsgeneral.domain.db.DDNSConfigDO;
import cn.sunyc.ddnsgeneral.domain.db.IPCheckerConfigDO;
import cn.sunyc.ddnsgeneral.domain.db.MsgNotifierConfigDO;
import cn.sunyc.ddnsgeneral.domain.db.key.DDNSConfigKey;
import cn.sunyc.ddnsgeneral.domain.dto.MsgNotifierTypeDTO;
import cn.sunyc.ddnsgeneral.domain.dto.MsgTypeDTO;
import cn.sunyc.ddnsgeneral.enumeration.MsgNotifierType;
import cn.sunyc.ddnsgeneral.enumeration.MsgType;
import cn.sunyc.ddnsgeneral.service.DDNSConfigService;
import cn.sunyc.ddnsgeneral.service.IPCheckerConfigService;
import cn.sunyc.ddnsgeneral.service.LocalIpService;
import cn.sunyc.ddnsgeneral.service.MsgNotifierConfigService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 管理系统的控制器
 *
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 19:56
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
@RestController
@SuppressWarnings("unused")
@RequestMapping("/api/manager")
public class ManagerController {

    @Resource
    private DDNSRunnerManager ddnsRunnerManager;
    @Resource
    private DDNSConfigService ddnsConfigService;
    @Resource
    private IPCheckerConfigService ipCheckerConfigService;
    @Resource
    private LocalIpService localIpService;
    @Resource
    private MsgNotifierConfigService msgNotifierConfigService;

    // ========== DDNS配置相关接口 ==========

    @RequestMapping({"/ddnsConfig/queryRunningList"})
    public List<DDNSConfigDO> queryRunningList() {
        return ddnsRunnerManager.queryRunningList();
    }

    /**
     * <a href="http://localhost:3364/api/manager/ddnsConfig/queryAll">查询所有DDNS配置</a>
     */
    @RequestMapping({"/ddnsConfig/queryAll"})
    public List<DDNSConfigDO> queryAll() {
        return ddnsConfigService.queryAll();
    }

    @RequestMapping({"/ddnsConfig/remove"})
    public boolean remove(DDNSConfigKey ddnsConfigKey) {
        ddnsConfigService.remove(ddnsConfigKey);
        return true;
    }

    @RequestMapping({"/ddnsConfig/save"})
    public boolean save(@RequestBody DDNSConfigDO ddnsConfigDO) {
        return ddnsConfigService.saveDDNSConfig(ddnsConfigDO);
    }

    @RequestMapping({"/ddnsConfig/runNow"})
    public boolean runNow(DDNSConfigKey ddnsConfigKey) {
        ddnsConfigService.runNow(ddnsConfigKey);
        return true;
    }

    // ========== IP检查器配置相关接口 ==========

    /**
     * 查询所有IP检查器配置<p>
     * <a href="http://localhost:3364/api/manager/ipCheckerConfig/queryAll">查询所有IP检查器配置</a>
     */
    @RequestMapping({"/ipCheckerConfig/queryAll"})
    public List<IPCheckerConfigDO> queryAllIpCheckerConfig() {
        return ipCheckerConfigService.queryAll();
    }

    /**
     * 查询当前启用的IP检查器配置
     */
    @RequestMapping({"/ipCheckerConfig/queryEnable"})
    public IPCheckerConfigDO queryEnableIpCheckerConfig() {
        return ipCheckerConfigService.queryEnable();
    }

    /**
     * 保存IP检查器配置
     */
    @RequestMapping({"/ipCheckerConfig/save"})
    public IPCheckerConfigDO saveIpCheckerConfig(@RequestBody IPCheckerConfigDO ipCheckerConfigDO) {
        return ipCheckerConfigService.save(ipCheckerConfigDO);
    }

    /**
     * 删除IP检查器配置
     */
    @RequestMapping({"/ipCheckerConfig/remove"})
    public boolean removeIpCheckerConfig(@RequestParam Long id) {
        ipCheckerConfigService.deleteById(id);
        return true;
    }

    /**
     * 启用指定的IP检查器配置
     */
    @RequestMapping({"/ipCheckerConfig/enable"})
    public boolean enableIpCheckerConfig(@RequestParam Long id) {
        return ipCheckerConfigService.enableById(id);
    }


    /**
     * 测试IP检查器配置<p>
     */
    @RequestMapping({"/ipCheckerConfig/test"})
    public String testIpCheckerConfig(@RequestBody IPCheckerConfigDO ipCheckerConfigDO) {
        try {
            return localIpService.getLocalOutSideIp(ipCheckerConfigDO);
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    // ========== 消息通知配置相关接口 ==========

    /**
     * 查询所有消息通知配置<p>
     * <a href="http://localhost:3364/api/manager/msgNotifierConfig/queryAll">查询所有消息通知配置</a>
     */
    @RequestMapping({"/msgNotifierConfig/queryAll"})
    public List<MsgNotifierConfigDO> queryAllMsgNotifierConfig() {
        return msgNotifierConfigService.queryAll();
    }

    /**
     * 保存消息通知配置
     */
    @RequestMapping({"/msgNotifierConfig/save"})
    public MsgNotifierConfigDO saveMsgNotifierConfig(@RequestBody MsgNotifierConfigDO msgNotifierConfigDO) {
        return msgNotifierConfigService.save(msgNotifierConfigDO);
    }

    /**
     * 删除消息通知配置
     */
    @RequestMapping({"/msgNotifierConfig/remove"})
    public boolean removeMsgNotifierConfig(@RequestParam Long id) {
        msgNotifierConfigService.deleteById(id);
        return true;
    }

    /**
     * 启用/禁用消息通知配置
     */
    @RequestMapping({"/msgNotifierConfig/toggleEnable"})
    public MsgNotifierConfigDO toggleEnableMsgNotifierConfig(@RequestParam Long id, @RequestParam Boolean enable) {
        return msgNotifierConfigService.toggleEnable(id, enable);
    }

    /**
     * 发送测试消息
     */
    @RequestMapping({"/msgNotifierConfig/sendMsg"})
    public boolean sendMsg(@RequestBody MsgNotifierConfigDO msgNotifierConfigDO) {
        return msgNotifierConfigService.sendMsg(msgNotifierConfigDO);
    }

    /**
     * 获取消息通知类型枚举列表<p>
     * <a href="http://localhost:3364/api/manager/msgNotifierConfig/getNotifierTypes">获取消息通知类型枚举列表</a>
     */
    @RequestMapping({"/msgNotifierConfig/getNotifierTypes"})
    public List<MsgNotifierTypeDTO> getNotifierTypes() {
        return Arrays.stream(MsgNotifierType.values())
                .map(type -> new MsgNotifierTypeDTO(
                        type.name(),
                        type.getName(),
                        type.getHookDesc()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 获取消息类型枚举列表<p>
     * <a href="http://localhost:3364/api/manager/msgNotifierConfig/getMsgTypes">获取消息类型枚举列表</a>
     */
    @RequestMapping({"/msgNotifierConfig/getMsgTypes"})
    public List<MsgTypeDTO> getMsgTypes() {
        return Arrays.stream(MsgType.values())
                .map(type -> new MsgTypeDTO(
                        type.name(),
                        type.getName(),
                        type.getDesc(),
                        type.getParams()
                ))
                .collect(Collectors.toList());
    }
}
