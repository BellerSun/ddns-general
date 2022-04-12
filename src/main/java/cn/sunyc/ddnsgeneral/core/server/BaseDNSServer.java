package cn.sunyc.ddnsgeneral.core.server;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * DNS服务器的基本类型。主要是定义了公用的初始化方法等
 *
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 17:16
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
public abstract class BaseDNSServer implements IDNSServer {
    /**
     * 初始化参数
     */
    @Getter
    protected JSONObject initializeParam;

    @Override
    public void init(JSONObject initializeParam) throws IllegalArgumentException {
        if (null == initializeParam) {
            throw new IllegalArgumentException("输入参数不能为空");
        }
        this.initializeParam = initializeParam;

        Collection<String> checkParams = getCheckParams();
        if (!CollectionUtils.isEmpty(checkParams)) {
            for (String checkParam : checkParams) {
                if (!initializeParam.containsKey(checkParam)) {
                    throw new IllegalArgumentException("DNSServer 缺少初始参数:" + checkParam);
                }
            }
        }
    }

    /**
     * 获取子类要检验的参数列表
     *
     * @return 参数列表
     */
    protected abstract Collection<String> getCheckParams();

}
