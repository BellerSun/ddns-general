package cn.sunyc.ddnsgeneral.tools.notifier;

import com.alibaba.fastjson.JSONObject;

import cn.sunyc.ddnsgeneral.enumeration.MsgType;

public interface MsgNotifier {
    
    /**
     * 发送消息通知
     *
     * @return 是否发送成功
     */
    boolean sendMsg(String hook, MsgType msgType, String msgTemplate, JSONObject param);
}
