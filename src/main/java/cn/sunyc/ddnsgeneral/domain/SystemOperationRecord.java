package cn.sunyc.ddnsgeneral.domain;

import cn.sunyc.ddnsgeneral.enumeration.OperationType;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统操作记录
 *
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 19:07
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemOperationRecord {
    /**
     * 操作类型
     */
    private OperationType operationType;
    /**
     * 操作时间
     */
    private long time;
    /**
     * 操作信息
     */
    private String msg;
    /**
     * 操作参数
     */
    private JSONObject param;

    public SystemOperationRecord(OperationType operationType,  String msg) {
        this.operationType = operationType;
        this.time = System.currentTimeMillis();
        this.msg = msg;
        this.param = null;
    }
}
