package cn.sunyc.ddnsgeneral.domain.resolution;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class HuaWeiResolutionRecord extends BaseResolutionRecord{
    /**
     * 记录的zoneId
     */
    private String zoneId;
}
