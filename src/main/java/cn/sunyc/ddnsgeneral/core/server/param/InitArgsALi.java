package cn.sunyc.ddnsgeneral.core.server.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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
public class InitArgsALi extends InitArgsAkSK {

    /**
     * 访问的域名
     */
    private String endpoint = " dns.aliyuncs.com";


    public InitArgsALi(@NotBlank String ak, @NotBlank String sk, String endpoint) {
        super(ak, sk);
        this.endpoint = endpoint;
    }
}
