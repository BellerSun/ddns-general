package cn.sunyc.ddnsgeneral.tools.feishu;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

/**
 * <a href="https://open.feishu.cn/document/client-docs/bot-v3/add-custom-bot">飞书Webhook API</a>
 */
public interface FeishuWebhookClient {

    @RequestLine("POST /open-apis/bot/v2/hook/{webhook}")
    @Headers("Content-Type: application/json")
    FeishuWebhookResponse sendWebhookMessage(@Param("webhook") String webhook, FeishuWebhookRequest req);

}

