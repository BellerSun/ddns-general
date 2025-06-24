package cn.sunyc.ddnsgeneral.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息通知类型 DTO
 * 用于前端下拉列表展示
 *
 * @author sun yu chao
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgNotifierTypeDTO {
    
    /**
     * 枚举值名称
     */
    private String name;
    
    /**
     * 显示名称
     */
    private String displayName;
    
    /**
     * Hook 描述
     */
    private String hookDesc;
} 