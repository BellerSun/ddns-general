package cn.sunyc.ddnsgeneral.domain;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 系统的运行状态
 *
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 19:03
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
@Getter
@Component
public class SystemStatus {

    /**
     * 系统操作记录表
     */
    private final BlockingQueue<SystemOperationRecord> operationRecords = new LinkedBlockingQueue<>();



}
