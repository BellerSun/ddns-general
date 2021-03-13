package cn.sunyc.ddnsgeneral.controller;

import cn.sunyc.ddnsgeneral.config.SystemProperties;
import cn.sunyc.ddnsgeneral.domain.SystemOperationRecord;
import cn.sunyc.ddnsgeneral.domain.SystemStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 19:56
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
@RestController
@SuppressWarnings("unused")
@RequestMapping("/monitor")
public class MonitorController {

    @Resource
    private SystemStatus systemStatus;
    @Resource
    private SystemProperties systemProperties;

    /**
     * 查询操作记录
     *
     * @param number 倒数查询条数，不填为查询全部
     * @return 操作记录
     */
    @RequestMapping({"/queryRecord", "/queryRecord/{number}"})
    public List<SystemOperationRecord> queryRecord(@PathVariable(required = false) Integer number) {

        List<SystemOperationRecord> records = new LinkedList<>(systemStatus.getOperationRecords());
        if (number != null && records.size() > number) {
            records = records.subList(records.size() - number, records.size());
        }
        return records;
    }

    /**
     * 查询系统参数
     *
     * @return 系统参数
     */
    @RequestMapping({"/querySystemProperties"})
    public SystemProperties querySystemProperties() {
        return systemProperties;
    }

}
