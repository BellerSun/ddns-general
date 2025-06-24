package cn.sunyc.ddnsgeneral.tools.feishu;

import lombok.Data;

@Data
public class FeishuWebhookResponse {
    private int code;              // 状态码
    private String msg;            // 消息
    private Object data;           // 数据（如果不使用可留作Object）
}
