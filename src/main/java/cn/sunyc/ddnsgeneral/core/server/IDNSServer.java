package cn.sunyc.ddnsgeneral.core.server;

import cn.sunyc.ddnsgeneral.domain.ResolutionRecord;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 通用的dns服务API的
 *
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 16:37
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
public interface IDNSServer {
    /**
     * 初始化dns服务api连接
     *
     * @param initializeParam 需要的参数
     * @throws IllegalArgumentException 参数校验异常
     */
    void init(JSONObject initializeParam) throws IllegalArgumentException;

    /**
     * 查询解析列表
     *
     * @param domainName
     * @return 解析列表
     */
    List<ResolutionRecord> queryList(String domainName) throws Exception;

    /**
     * 修改某一条解析记录
     *
     * @param resolutionRecord 被修改的记录
     * @return 修改结果
     */
    boolean updateResolutionRecord(ResolutionRecord resolutionRecord) throws Exception;
}
