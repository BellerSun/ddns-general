package cn.sunyc.ddnsgeneral.controller;

import cn.sunyc.ddnsgeneral.core.DDNSRunnerManager;
import cn.sunyc.ddnsgeneral.domain.db.DDNSConfigDO;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/manager")
public class ManagerController {

    @Resource
    private DDNSRunnerManager ddnsRunnerManager;
    
    @RequestMapping({"/ddnsConfig/queryRunningList"})
    public List<DDNSConfigDO> queryRunningList() {
        return ddnsRunnerManager.queryRunningList();
    }

    @RequestMapping({"/ddnsConfig/save"})
    public boolean save(DDNSConfigDO ddnsConfigDO) {
        return ddnsRunnerManager.saveDDNSConfig(ddnsConfigDO);
    }
}
