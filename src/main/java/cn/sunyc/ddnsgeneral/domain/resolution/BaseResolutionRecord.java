package cn.sunyc.ddnsgeneral.domain.resolution;

import lombok.Data;

/**
 * 存储域名解析记录的对象，这里是根据经验整理出来的一些都会有的。但是某些云服务商的字段、甚至数据结构都不一致，那到时候可生成子类。
 *
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 17:01
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
@Data
public class BaseResolutionRecord {
    /**
     * 记录的唯一id
     */
    private String recordId;
    /**
     * 主域名：【baidu.cn】
     */
    private String domain;
    /**
     * 子域名：【www、wiki、@、*】等
     */
    private String subDomain;
    /**
     * 记录类型，通过API记录类型获得，大写英文，比如：A。
     */
    private String recordType;
    /**
     * 记录线路，通过API记录线路获得，中文，比如：默认。
     */
    private String recordLine = "默认";
    /**
     * MX优先级, 当记录类型是 MX 时有效，范围1-20。
     */
    private Integer mx;
    /**
     * 记录的值
     */
    private String value;

}
