package cn.sunyc.ddnsgeneral.core.server;

import cn.sunyc.ddnsgeneral.core.server.param.InitArgsAbs;
import cn.sunyc.ddnsgeneral.domain.resolution.BaseResolutionRecord;
import cn.sunyc.ddnsgeneral.utils.ObjUtil;
import cn.sunyc.ddnsgeneral.utils.ValidateUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * DNS服务器的基本类型。主要是定义了公用的初始化方法等
 *
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 17:16
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
public abstract class BaseDNSServer<T_RECORD extends BaseResolutionRecord, T_ARGS extends InitArgsAbs> implements IDNSServer<T_RECORD> {

    /**
     * 初始化参数
     */
    protected InitArgsAbs initArgs;


    @SuppressWarnings("unchecked")
    public T_ARGS getInitArgs() {
        return (T_ARGS) this.initArgs;
    }

    @Override
    public void init(JSONObject initializeParam) throws IllegalArgumentException {
        if (null == initializeParam) {
            throw new IllegalArgumentException("输入参数不能为空");
        }
        this.initArgs = initializeParam.toJavaObject(ObjUtil.getGenericType(this, InitArgsAbs.class));
        if (null == this.initArgs) {
            throw new IllegalArgumentException("输入参数不能为空");
        }

        // 校验参数，不合格会抛出异常
        ValidateUtil.validate(this.initArgs);
        this.initSub(this.getInitArgs());
    }

    /**
     * 子类自定义初始化内容，可以没有
     *
     * @param args 初始化参数
     */
    protected void initSub(T_ARGS args) {
    }


}
