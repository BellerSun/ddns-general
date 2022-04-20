package cn.sunyc.ddnsgeneral.utils;


import cn.sunyc.ddnsgeneral.domain.SystemConstant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * MDC简单使用的util
 *
 * @author SunYuChao
 * @date 2022/4208 02:20
 * credits [SunYuChao,,]
 */
@SuppressWarnings("all")
public class MDCUtil {

    /**
     * 为本线程生成一个随机的requestId
     */
    public static String setRequestId() {
        final String reId = UUID.randomUUID().toString();
        MDC.put(SystemConstant.REQUEST_ID, reId);
        return reId;
    }

    /**
     * 为本线程设置一个requestId
     */
    public static String setRequestId(String requestId) {
        MDC.put(SystemConstant.REQUEST_ID, StringUtils.defaultIfBlank(requestId, ""));
        return requestId;
    }

    /**
     * 删除本线程的requestId
     */
    public static void clearRequestId() {
        MDC.remove(SystemConstant.REQUEST_ID);
    }


    public static String getRequestId() {
        return MDC.get(SystemConstant.REQUEST_ID);
    }
}
