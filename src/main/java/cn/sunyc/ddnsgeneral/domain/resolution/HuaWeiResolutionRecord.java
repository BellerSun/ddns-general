package cn.sunyc.ddnsgeneral.domain.resolution;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 华为的解析记录，必须要有一个zoneId
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuaWeiResolutionRecord extends BaseResolutionRecord{
    /**
     * 记录的zoneId
     */
    private String zoneId;
}
