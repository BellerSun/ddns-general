package cn.sunyc.ddnsgeneral.core.server.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author sun yu chao
 * @version 1.0
 * @since 2023/1/17 14:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InitArgsHuaWei extends InitArgsAkSK {

    /**
     * dns区域
     */
    private String dnsRegion = "cn-east-2";

    public InitArgsHuaWei(@NotBlank String ak, @NotBlank String sk, String dnsRegion) {
        super(ak, sk);
        this.dnsRegion = dnsRegion;
    }
}
