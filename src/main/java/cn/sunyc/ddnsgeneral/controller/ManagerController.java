package cn.sunyc.ddnsgeneral.controller;

import cn.sunyc.ddnsgeneral.config.SystemProperties;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    private SystemProperties systemProperties;

    /**
     * 激活/关闭 系统
     *
     * @param status 激活/关闭
     * @return 操作后的
     */
    @RequestMapping({"/active/{status}"})
    public String queryRecord(@PathVariable boolean status) {
        systemProperties.setActivate(status);
        return systemProperties.isActivate() ? "激活成功" : "停止成功";
    }

}
