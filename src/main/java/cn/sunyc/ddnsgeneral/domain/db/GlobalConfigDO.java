package cn.sunyc.ddnsgeneral.domain.db;

import lombok.*;

import javax.persistence.*;

/**
 * @author sun yu chao
 * @version 1.0
 * @since 2023/1/21 06:17
 */
@Getter
@Setter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "GLOBAL_CONFIG")
public class GlobalConfigDO {

    @Id
    @Column(length = 500, nullable = false)
    private String configKey;

    /**
     * dns服务器类型
     */
    @Column(length = 500)
    private String configVal;

}
