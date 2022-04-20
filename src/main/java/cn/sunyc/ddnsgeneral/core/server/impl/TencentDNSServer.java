package cn.sunyc.ddnsgeneral.core.server.impl;

import cn.sunyc.ddnsgeneral.core.server.BaseDNSServer;
import cn.sunyc.ddnsgeneral.domain.resolution.BaseResolutionRecord;
import cn.sunyc.ddnsgeneral.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 17:14
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
@Slf4j
@Component
public class TencentDNSServer extends BaseDNSServer<BaseResolutionRecord> {
    private static final String API_BASE_URL = "https://dnsapi.cn/";

    private static String login_token;

    @Override
    public void init(JSONObject initializeParam) throws IllegalArgumentException {
        super.init(initializeParam);
        login_token = initializeParam.getString("login_token");

    }

    @Override
    public List<BaseResolutionRecord> queryList(String domainName) throws Exception {
        HashMap<String, String> param = getDefaultRequestParams();

        param.put("domain", domainName);

        String response = HttpUtil.post(API_BASE_URL + "Record.List", param);

        TencentCommonResp commonResp = JSON.parseObject(response, TencentCommonResp.class);

        if (!commonResp.isSuccess()) {
            throw new RuntimeException("响应结果失败.response:" + response);
        }
        List<JSONObject> records = commonResp.getRecords();

        List<BaseResolutionRecord> baseResolutionRecords = records.stream()
                .filter(Objects::nonNull)
                .map(recordObj -> {
                    BaseResolutionRecord resolutionRecord = new BaseResolutionRecord();
                    resolutionRecord.setRecordId(recordObj.getString("id"));
                    resolutionRecord.setDomain(domainName);
                    resolutionRecord.setMx(recordObj.getInteger("mx"));
                    resolutionRecord.setRecordLine(recordObj.getString("line"));
                    resolutionRecord.setRecordType(recordObj.getString("type"));
                    resolutionRecord.setValue(recordObj.getString("value"));
                    resolutionRecord.setSubDomain(recordObj.getString("name"));
                    return resolutionRecord;
                }).collect(Collectors.toList());
        log.info("[TencentDNSServer] resolutionRecords:{}", JSON.toJSONString(baseResolutionRecords));
        return baseResolutionRecords;
    }

    @Override
    public boolean updateResolutionRecord(BaseResolutionRecord resolutionRecord) throws Exception {
        HashMap<String, String> param = getDefaultRequestParams();

        param.put("record_id", resolutionRecord.getRecordId());
        param.put("domain", resolutionRecord.getDomain());
        param.put("sub_domain", resolutionRecord.getSubDomain());
        param.put("record_type", resolutionRecord.getRecordType());
        param.put("record_line", resolutionRecord.getRecordLine());
        param.put("value", resolutionRecord.getValue());
        param.put("mx", String.valueOf(resolutionRecord.getMx()));

        log.info("[TencentDNSServer] update request:{}", JSON.toJSONString(param));
        String response = HttpUtil.post(API_BASE_URL + "Record.Modify", param);
        log.info("[TencentDNSServer] update response:{}", JSON.toJSONString(response));
        TencentCommonResp commonResp = JSON.parseObject(response, TencentCommonResp.class);
        return commonResp.isSuccess();
    }

    @Override
    protected Collection<String> getCheckParams() {
        return Collections.singletonList("login_token");
    }

    private HashMap<String, String> getDefaultRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("login_token", login_token);
        params.put("format", "json");
        params.put("lang", "cn");
        params.put("error_on_empty", "no");
        return params;
    }

    @Data
    private static class TencentCommonResp {
        private TencentRespStatus status;
        private JSONObject info;
        private JSONObject domain;
        private List<JSONObject> records;

        public boolean isSuccess() {
            return status != null && "1".equalsIgnoreCase(status.code);
        }
    }

    @Data
    private static class TencentRespStatus {
        private String code;
        private String created_at;
        private String message;
    }
}
