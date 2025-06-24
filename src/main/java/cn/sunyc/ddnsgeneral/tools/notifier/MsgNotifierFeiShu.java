package cn.sunyc.ddnsgeneral.tools.notifier;

import cn.sunyc.ddnsgeneral.tools.feishu.FeishuWebhookClient;
import cn.sunyc.ddnsgeneral.tools.feishu.FeishuWebhookRequest;
import cn.sunyc.ddnsgeneral.tools.feishu.FeishuWebhookResponse;
import cn.sunyc.ddnsgeneral.enumeration.MsgType;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Component("FEISHU")
@AllArgsConstructor
public class MsgNotifierFeiShu extends MsgNotifierAbs {

    @Resource
    private final FeishuWebhookClient feishuWebhookClient;

    @Override
    protected boolean sendMsg(String hook, MsgType msgType, String msgContent) {
        final FeishuWebhookRequest.PostContent.Post.LanguageContent.Tag textTag = FeishuWebhookRequest.PostContent.Post.LanguageContent.Tag
                .createTextTag(msgContent);
        final FeishuWebhookRequest req = FeishuWebhookRequest.Factory.createRichTextMessage(msgType.getName(), textTag);
        final FeishuWebhookResponse resp = feishuWebhookClient.sendWebhookMessage(hook, req);
        log.info("[MSG_NOTIFIER][FEISHU] sendMsg resp: {}", JSON.toJSONString(resp));
        final int code = Optional.ofNullable(resp).map(FeishuWebhookResponse::getCode).orElse(-999);
        return code == 0;
    }
}
