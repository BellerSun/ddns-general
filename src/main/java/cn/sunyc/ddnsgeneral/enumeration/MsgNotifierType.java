package cn.sunyc.ddnsgeneral.enumeration;

import cn.sunyc.ddnsgeneral.tools.notifier.*;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MsgNotifierType {
    FEISHU("飞书通知", "飞书机器人的HookID，不包括url部分", MsgNotifierFeiShu.class),
    DINGTALK("钉钉通知", "钉钉通知的access_token，不包括url部分", MsgNotifierDingTalk.class),

    ;

    private final String name;
    private final String hookDesc;
    private final Class<? extends MsgNotifier> notifierClass;
}
