package cn.sunyc.ddnsgeneral.tools.notifier;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;

import cn.sunyc.ddnsgeneral.enumeration.MsgType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component("DINGTALK")
@AllArgsConstructor
public class MsgNotifierDingTalk extends MsgNotifierAbs {


    @Override
    protected boolean sendMsg(String hook, MsgType msgType, String msgContent) {
        final DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send?access_token=" + hook);
        final OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(msgType.getName());
        markdown.setText("# " + msgType.getName() + "\n\n" + msgContent);
        request.setMarkdown(markdown);
        OapiRobotSendResponse response;
        try {
            response = client.execute(request);
            log.info("[MSG_NOTIFIER][DINGTALK] sendMsg resp: {}", JSON.toJSONString(response));
        } catch (ApiException e) {
            log.error("[MSG_NOTIFIER][DINGTALK] sendMsg error: {}", e.getMessage());
            return false;
        }
        return response.isSuccess();
    }

    public void sendMessageWebhook() throws ApiException {
    }

}
