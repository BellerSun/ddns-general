package cn.sunyc.ddnsgeneral.core.server.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author sun yu chao
 * @version 1.0
 * @since 2023/1/17 11:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InitArgsToken extends InitArgsAbs {
    
    @NotBlank
    private String loginToken;

}
