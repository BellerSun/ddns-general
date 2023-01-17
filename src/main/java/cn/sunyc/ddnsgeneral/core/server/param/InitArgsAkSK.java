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
public class InitArgsAkSK extends InitArgsAbs {

    /**
     * 您的AccessKey ID
     */
    @NotBlank
    private String ak;

    /**
     * 您的AccessKey Secret
     */
    @NotBlank
    private String sk;

}
