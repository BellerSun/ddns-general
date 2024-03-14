package cn.sunyc.ddnsgeneral.domain.db;

import com.aliyun.credentials.http.MethodType;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "IP_CHECKER_CONFIG")
public class IPCheckerConfigDO {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String configName;

    /**
     * 检测服务url
     */
    @Column(length = 1000)
    private String url;

    /**
     * http的调用方式
     */
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private MethodType methodType;

    /**
     * 正则表达式，会从返回结果中匹配出IP
     */
    @Column(length = 1000)
    private String regex;

    /**
     * 是否启用，全局只会启用一个（后面也许会看情况决定是否支持每个ddns任务绑定一个IPChecker）
     */
    @Column()
    private Boolean enable;
}
