package cn.sunyc.ddnsgeneral.tools.feishu;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class FeishuWebhookRequest {

    @JSONField(name = "msg_type")
    private String msgType;

    private Content content;

    private Card card; // 用于卡片消息


    public interface Content{

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextContent implements Content {
        private String text;

    }

    @Data
    @NoArgsConstructor
    public static class PostContent  implements Content {
        private Post post;

        @Data
        @NoArgsConstructor
        public static class Post {
            @JSONField(name = "zh_cn")
            private LanguageContent zhCn;

            @Data
            @NoArgsConstructor
            public static class LanguageContent {
                private String title;
                private List<List<Tag>> content;

                @Data
                @NoArgsConstructor
                public static class Tag {
                    private String tag;
                    private String text;
                    private String href;
                    @JSONField(name = "user_id")
                    private String userId;

                    /**
                     * 工厂方法：创建文本类型的Tag
                     */
                    public static Tag createTextTag(String text) {
                        Tag tag = new Tag();
                        tag.setTag("text");
                        tag.setText(text);
                        return tag;
                    }

                    /**
                     * 工厂方法：创建超链接类型的Tag
                     */
                    public static Tag createLinkTag(String text, String href) {
                        Tag tag = new Tag();
                        tag.setTag("a");
                        tag.setText(text);
                        tag.setHref(href);
                        return tag;
                    }

                    /**
                     * 工厂方法：创建@用户类型的Tag
                     */
                    public static Tag createAtTag(String userId) {
                        Tag tag = new Tag();
                        tag.setTag("at");
                        tag.setUserId(userId);
                        return tag;
                    }
                }
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class ShareChatContent  implements Content {
        @JSONField(name = "share_chat_id")
        private String shareChatId;
    }

    @Data
    @NoArgsConstructor
    public static class Card {
        private List<Element> elements;
        private Header header;

        @Data
        @NoArgsConstructor
        public static class Element {
            private String tag;
            private Text text;
            private List<Action> actions;

            @Data
            @NoArgsConstructor
            public static class Text {
                private String content;
                private String tag;
            }

            @Data
            @NoArgsConstructor
            public static class Action {
                private String tag;
                private Text text;
                private String url;
                private String type;
                private Map<String, Object> value;
            }
        }

        @Data
        @NoArgsConstructor
        public static class Header {
            private Title title;

            @Data
            @NoArgsConstructor
            public static class Title {
                private String content;
                private String tag;
            }
        }
    }

    public static class Factory{
        public static FeishuWebhookRequest createTextMessage(String text) {
            FeishuWebhookRequest request = new FeishuWebhookRequest();
            request.setMsgType("text");
            TextContent content = new TextContent();
            content.setText(text);
            request.setContent(content);
            return request;
        }

        public static FeishuWebhookRequest createRichTextMessage(String title, PostContent.Post.LanguageContent.Tag... tags) {
            FeishuWebhookRequest request = new FeishuWebhookRequest();
            request.setMsgType("post");

            // 创建 PostContent 和 LanguageContent
            PostContent postContent = new PostContent();
            PostContent.Post post = new PostContent.Post();
            PostContent.Post.LanguageContent languageContent = new PostContent.Post.LanguageContent();

            // 设置标题
            languageContent.setTitle(title);

            // 封装为双层数组结构
            languageContent.setContent(List.of(List.of(tags)));

            // 设置内容
            post.setZhCn(languageContent);
            postContent.setPost(post);
            request.setContent(postContent);

            return request;
        }


        public static FeishuWebhookRequest createShareChatMessage(String shareChatId) {
            FeishuWebhookRequest request = new FeishuWebhookRequest();
            request.setMsgType("share_chat");
            ShareChatContent content = new ShareChatContent();
            content.setShareChatId(shareChatId);
            request.setContent(content);
            return request;
        }


        public static FeishuWebhookRequest createInteractiveCardMessage(Card card) {
            FeishuWebhookRequest request = new FeishuWebhookRequest();
            request.setMsgType("interactive");
            request.setCard(card);
            return request;
        }
    }
}
