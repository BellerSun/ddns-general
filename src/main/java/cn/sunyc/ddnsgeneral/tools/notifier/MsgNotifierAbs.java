package cn.sunyc.ddnsgeneral.tools.notifier;

import com.alibaba.fastjson.JSONObject;

import cn.sunyc.ddnsgeneral.enumeration.MsgType;
import cn.sunyc.ddnsgeneral.utils.YcStringTemplateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class MsgNotifierAbs implements MsgNotifier {
    @Override
    public boolean sendMsg(String hook, MsgType msgType, String msgTemplate, JSONObject param) {
        log.info("[MSG_NOTIFIER] sendMsg hook: {}, msgTemplate: {}, param: {}", hook, msgTemplate, param.toJSONString());
        final String renderedMsg = YcStringTemplateUtil.BRACES.replace(msgTemplate, param);
        log.info("[MSG_NOTIFIER] renderedMsg: {}", renderedMsg);

        return this.sendMsg(hook, msgType, renderedMsg);
    }



    protected abstract boolean sendMsg(String hook, MsgType msgType, String msgContent);
}
