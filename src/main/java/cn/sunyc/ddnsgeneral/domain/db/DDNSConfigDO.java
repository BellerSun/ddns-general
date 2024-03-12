package cn.sunyc.ddnsgeneral.domain.db;

import cn.sunyc.ddnsgeneral.domain.db.key.DDNSConfigKey;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * H2数据表
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DDNS_CONFIG")
public class DDNSConfigDO implements Serializable {

    @EmbeddedId
    private DDNSConfigKey ddnsConfigKey;

    /**
     * dns服务器类型
     */
    @Column(length = 50)
    private String dnsServerType;

    /**
     * dns服务器必须参数.这个字段不打印，因为会有各种隐私信息
     */
    @Column(length = 1000)
    @JSONField(serialize = false)
    private String dnsServerParam;


    /**
     * 查询出来的解析记录，生存时间。避免每次都去查，过了这个时间长度就不相信该结果了
     */
    private long ddnsRecordAliveTime;

    /**
     * 记录类型
     */
    @Column(length = 50)
    private String ddnsDomainRecordType;

    /**
     * 定时调度字符串
     */
    @Column(length = 50)
    private String schedulerCron;

    /**
     * 是否启动
     */
    @Column
    private boolean activate = true;

    @Override
    public String toString() {
        return "{" +
                "dnsServerType:'" + dnsServerType + '\'' +
                ", ddnsDomainName:'" + (ddnsConfigKey == null ? null : ddnsConfigKey.getDomainName()) + '\'' +
                ", ddnsDomainSubName:'" + (ddnsConfigKey == null ? null : ddnsConfigKey.getDomainSubName()) + '\'' +
                ", ddnsRecordAliveTime:" + ddnsRecordAliveTime +
                ", ddnsDomainRecordType:'" + ddnsDomainRecordType + '\'' +
                ", schedulerCron:'" + schedulerCron + '\'' +
                ", activate:" + activate +
                '}';
    }

    /**
     * 生成能够唯一代表这个对象的key
     */
    public String generateUniqueKey() {
        return this.getDdnsConfigKey().generateUniqueKey();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DDNSConfigDO that = (DDNSConfigDO) o;
        return ddnsConfigKey != null && Objects.equals(ddnsConfigKey, that.ddnsConfigKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ddnsConfigKey);
    }
}
