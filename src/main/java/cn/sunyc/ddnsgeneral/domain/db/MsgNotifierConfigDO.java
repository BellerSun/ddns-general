package cn.sunyc.ddnsgeneral.domain.db;

import cn.sunyc.ddnsgeneral.enumeration.MsgNotifierType;
import cn.sunyc.ddnsgeneral.enumeration.MsgType;
import com.aliyun.credentials.http.MethodType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MSG_NOTIFIER_CONFIG")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MsgNotifierConfigDO {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 纯展示使用，名称
     */
    @Column(length = 500)
    private String configName;

    /**
     * 消息类型，通常是事件类型或通知类型.不区分大小写
     */
    @Column(length = 100)
    @Enumerated(EnumType.STRING)
    private MsgType msgType;

    /**
     * 通知器的类型，不区分大小写
     */
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private MsgNotifierType notifierType;

    /**
     * 通知的hook
     */
    @Column(length = 1000)
    private String hook;

    /**
     * 通知模板
     */
    @Column(length = 2000)
    private String msgTemplate;

    /**
     * 是否启用
     */
    @Column()
    private Boolean enable;
}
