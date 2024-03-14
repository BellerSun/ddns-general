package cn.sunyc.ddnsgeneral.controller;

import cn.sunyc.ddnsgeneral.service.LocalIpService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 19:56
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
@RestController
@AllArgsConstructor
@SuppressWarnings("unused")
@RequestMapping("/api/monitor")
public class MonitorController {

    private final LocalIpService localIpService;

    @GetMapping({"/ip"})
    public String queryRunningList() {
        return localIpService.getLocalOutSideIp();
    }

}
