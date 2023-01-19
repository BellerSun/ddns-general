package cn.sunyc.ddnsgeneral.domain.db.key;

import cn.sunyc.ddnsgeneral.domain.SystemConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DDNSConfigKey implements Serializable {

    /**
     * 动态解析的主域名【baidu.com】
     */
    @Column(length = 100)
    private String domainName;

    /**
     * 动态解析的子域名【www、wiki、@、*】
     */
    @Column(length = 100)
    private String domainSubName;


    public String generateUniqueKey() {
        return this.getDomainSubName() + SystemConstant.DOMAIN_SPLIT + this.getDomainName();
    }
}