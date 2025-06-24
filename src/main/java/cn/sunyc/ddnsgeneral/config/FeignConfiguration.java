package cn.sunyc.ddnsgeneral.config;

import cn.sunyc.ddnsgeneral.tools.feishu.FeishuWebhookClient;
import com.alibaba.fastjson.JSON;
import feign.Feign;
import feign.RequestTemplate;
import feign.codec.Decoder;
import feign.form.FormEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

@Configuration
public class FeignConfiguration {

    @Bean
    public FeishuWebhookClient feishuWebhookClient() {
        final String cloudEndpoint = "https://open.feishu.cn";
        return Feign.builder()
                .encoder(new FastjsonEncoder())
                .decoder(new FastjsonDecoder())
                .logger(new Slf4jLogger())
                .target(FeishuWebhookClient.class, cloudEndpoint);
    }


    public static class FastjsonEncoder extends FormEncoder {
        @Override
        public void encode(Object object, Type bodyType, RequestTemplate template) {
            if (object != null) {
                byte[] body = JSON.toJSONString(object).getBytes(StandardCharsets.UTF_8);
                template.body(body, StandardCharsets.UTF_8);
            }
        }
    }

    public static class FastjsonDecoder implements Decoder {
        @Override
        public Object decode(feign.Response response, Type type) {
            try (InputStream bodyStream = response.body().asInputStream()) {
                byte[] bodyBytes = bodyStream.readAllBytes();
                String bodyString = new String(bodyBytes, StandardCharsets.UTF_8);

                if (bodyString.startsWith("{") && bodyString.endsWith("}")) {
                    return JSON.parseObject(bodyString, type);
                }
                if (bodyString.startsWith("[") && bodyString.endsWith("]")) {
                    return JSON.parseObject(bodyString, type);
                }

                HttpHeaders headers = new HttpHeaders();
                for (Map.Entry<String, Collection<String>> entry : response.headers().entrySet()) {
                    headers.put(entry.getKey(), entry.getValue().stream().toList());
                }
                StreamingResponseBody streamingResponseBody = outputStream -> {
                    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bodyBytes)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                };
                return new ResponseEntity<>(streamingResponseBody, headers, response.status());

            } catch (Exception e) {
                throw new RuntimeException("Failed to decode response body", e);
            }
        }
    }
}
