package cn.sunyc.ddnsgeneral.core.server.impl;

import cn.sunyc.ddnsgeneral.core.server.BaseDNSServer;
import cn.sunyc.ddnsgeneral.domain.SystemConstant;
import cn.sunyc.ddnsgeneral.domain.resolution.HuaWeiResolutionRecord;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.dns.v2.DnsClient;
import com.huaweicloud.sdk.dns.v2.model.*;
import com.huaweicloud.sdk.dns.v2.region.DnsRegion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HuaWeiDNSServer extends BaseDNSServer<HuaWeiResolutionRecord> {

    /**
     * 您的AccessKey ID
     */
    private static final String AK_KEY = "ak";
    /**
     * 您的AccessKey Secret
     */
    private static final String SK_KEY = "sk";
    /**
     * 访问的域名
     */
    private static final String DNS_REGION_KEY = "dnsRegion";

    private DnsClient client;

    @Override
    protected Collection<String> getCheckParams() {
        return Arrays.asList(AK_KEY, SK_KEY);
    }


    @Override
    public void init(JSONObject initializeParam) throws IllegalArgumentException {
        super.init(initializeParam);
        try {
            ICredential auth = new BasicCredentials().withAk(initializeParam.getString(AK_KEY)).withSk(initializeParam.getString(SK_KEY));
            this.client = DnsClient.newBuilder().withCredential(auth).withRegion(DnsRegion.valueOf((String) initializeParam.getOrDefault(DNS_REGION_KEY, "cn-east-2"))).build();
        } catch (Exception e) {
            log.error("[HuaWeiDNSServer] init error.", e);
            throw new IllegalArgumentException("HuaWeiDNSServer 初始化失败:" + e.getMessage());
        }
        log.info("[HuaWeiDNSServer] init success.");
    }

    @Override
    public List<HuaWeiResolutionRecord> queryList(String domainName) throws Exception {
        ListRecordSetsRequest request = new ListRecordSetsRequest();
        request.setName(domainName);
        try {
            ListRecordSetsResponse response = client.listRecordSets(request);

            List<ListRecordSetsWithTags> records = Optional.ofNullable(response).map(ListRecordSetsResponse::getRecordsets).orElse(new ArrayList<>());
            final List<HuaWeiResolutionRecord> baseResolutionRecords = records.stream().map(record -> {
                HuaWeiResolutionRecord baseResolutionRecord = new HuaWeiResolutionRecord();
                baseResolutionRecord.setRecordId(record.getId());
                baseResolutionRecord.setDomain(Optional.ofNullable(record.getZoneName()).map(name -> name.substring(0, name.length() - 1)).orElse(""));
                baseResolutionRecord.setSubDomain(record.getName().replaceFirst(record.getZoneName(), "").replaceFirst("\\.", ""));
                baseResolutionRecord.setRecordType(record.getType());
                baseResolutionRecord.setValue(Optional.ofNullable(record.getRecords()).filter(recordValues -> recordValues.size() > 0).map(recordValues -> recordValues.get(0)).orElse(""));
                baseResolutionRecord.setZoneId(record.getZoneId());
                return baseResolutionRecord;
            }).collect(Collectors.toList());
            log.info("[HuaWeiDNSServer] resolutionRecords:{}", JSON.toJSONString(baseResolutionRecords));
            return baseResolutionRecords;
        } catch (Exception e) {
            log.error("[HuaWeiDNSServer] queryList error.", e);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean updateResolutionRecord(HuaWeiResolutionRecord baseResolutionRecord) throws Exception {
        UpdateRecordSetRequest request = new UpdateRecordSetRequest();
        try {
            request.setZoneId(baseResolutionRecord.getZoneId());
            request.setRecordsetId(baseResolutionRecord.getRecordId());
            UpdateRecordSetReq body = new UpdateRecordSetReq();
            request.setBody(body);
            body.setName(baseResolutionRecord.getSubDomain() + SystemConstant.DOMAIN_SPLIT + baseResolutionRecord.getDomain());
            body.setRecords(Collections.singletonList(baseResolutionRecord.getValue()));
            log.info("[HuaWeiDNSServer] update request:{}", JSON.toJSONString(request));
            UpdateRecordSetResponse updateRecordSetResponse = client.updateRecordSet(request);
            log.info("[HuaWeiDNSServer] update response:{}", JSON.toJSONString(updateRecordSetResponse));
            return updateRecordSetResponse != null;
        } catch (Exception e) {
            log.error("[HuaWeiDNSServer] queryList error.", e);
        }
        return false;
    }
}
