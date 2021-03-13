package cn.sunyc.ddnsgeneral.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统进行的操作类型
 *
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 19:05
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
@Getter
@AllArgsConstructor
public enum OperationType {
    /**
     * 查询解析记录
     */
    QUERY_RECORD,
    /**
     * 创建解析记录
     */
    CREATE_RECORD,
    /**
     * 修改解析记录
     */
    UPDATE_RECORD,
    /**
     * 查询外网ip
     */
    QUERY_IP,
    /**
     * 异常操作
     */
    ERROR,
    /**
     * 开始操作
     */
    START,
    /**
     * 未激活
     */
    INACTIVATE,
    /**
     * 结束操作
     */
    END,

    ;
}
