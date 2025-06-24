package cn.sunyc.ddnsgeneral.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 消息类型 DTO
 * 用于前端下拉列表展示
 *
 * @author sun yu chao
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgTypeDTO {
    
    /**
     * 枚举值名称
     */
    private String name;
    
    /**
     * 显示名称
     */
    private String displayName;
    
    /**
     * 描述信息
     */
    private String desc;
    
    /**
     * 参数说明
     */
    private Map<String, String> params;
} 