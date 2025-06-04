package cn.sunyc.ddnsgeneral.controller;

import cn.sunyc.ddnsgeneral.core.DDNSRunnerManager;
import cn.sunyc.ddnsgeneral.domain.db.DDNSConfigDO;
import cn.sunyc.ddnsgeneral.domain.db.IPCheckerConfigDO;
import cn.sunyc.ddnsgeneral.domain.db.key.DDNSConfigKey;
import cn.sunyc.ddnsgeneral.service.DDNSConfigService;
import cn.sunyc.ddnsgeneral.service.IPCheckerConfigService;
import cn.sunyc.ddnsgeneral.service.LocalIpService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

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
}
