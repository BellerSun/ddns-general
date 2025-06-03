package cn.sunyc.ddnsgeneral.core.server.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 腾讯云DNSPod参数类
 * 
 * @author sun yu chao
 * @version 1.0
 * @since 2024/1/1 10:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InitArgsTencent extends InitArgsAkSK {

    /**
     * 区域信息，可选参数，默认为北京
     */
    private String region = "ap-beijing";

    public InitArgsTencent(@NotBlank String ak, @NotBlank String sk, String region) {
        super(ak, sk);
        this.region = region;
    }
} 