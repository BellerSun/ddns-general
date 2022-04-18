package cn.sunyc.ddnsgeneral.core.server;

import cn.sunyc.ddnsgeneral.domain.resolution.BaseResolutionRecord;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alidns20150109.Client;
import com.aliyun.alidns20150109.models.*;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ALiYunDNSServer extends BaseDNSServer<BaseResolutionRecord> {

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
    private static final String ENDPOINT_KEY = "endpoint";

    private Client client;

    @Override
    protected Collection<String> getCheckParams() {
        return Arrays.asList(AK_KEY, SK_KEY);
    }

    @Override
    public void init(JSONObject initializeParam) throws IllegalArgumentException {
        super.init(initializeParam);
        log.info("[ALiYunDNSServer] initializeParam:{}", JSON.toJSONString(initializeParam));
        Config config = new Config()
                .setAccessKeyId(initializeParam.getString(AK_KEY))
                .setAccessKeySecret(initializeParam.getString(SK_KEY))
                .setEndpoint((String) initializeParam.getOrDefault(ENDPOINT_KEY, "dns.aliyuncs.com"));
        try {
            this.client = new Client(config);
        } catch (Exception e) {
            log.error("[ALiYunDNSServer] init error.", e);
            throw new IllegalArgumentException("ALiYunDNSServer 初始化失败:" + e.getMessage());
        }
        log.info("[ALiYunDNSServer] init success.");
    }

    @Override
    public List<BaseResolutionRecord> queryList(String domainName) throws Exception {
        final DescribeDomainRecordsRequest describeDomainRecordsRequest = new DescribeDomainRecordsRequest();
        // 这里尽量直接查出来所有,所以写了1W个
        describeDomainRecordsRequest.setPageSize(500L);
        describeDomainRecordsRequest.setDomainName(domainName);
        // 得到响应
        final DescribeDomainRecordsResponse recordsResponse = client.describeDomainRecords(describeDomainRecordsRequest);
        final List<DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord> records = Optional.ofNullable(recordsResponse).map(DescribeDomainRecordsResponse::getBody).map(DescribeDomainRecordsResponseBody::getDomainRecords).map(DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecords::getRecord).orElse(new ArrayList<>());
        final List<BaseResolutionRecord> resolutionRecords = records.stream().map(record -> {
            BaseResolutionRecord resolutionRecord = new BaseResolutionRecord();
            resolutionRecord.setRecordId(record.getRecordId());
            resolutionRecord.setDomain(record.getDomainName());
            resolutionRecord.setSubDomain(record.getRR());
            resolutionRecord.setRecordLine(record.getLine());
            resolutionRecord.setRecordType(record.getType());
            resolutionRecord.setValue(record.getValue());

            Optional.ofNullable(record.getPriority()).ifPresent(priority -> resolutionRecord.setMx(Math.toIntExact(priority)));

            return resolutionRecord;
        }).collect(Collectors.toList());
        log.info("[ALiYunDNSServer] resolutionRecords:{}", JSON.toJSONString(resolutionRecords));
        return resolutionRecords;
    }

    @Override
    public boolean updateResolutionRecord(BaseResolutionRecord resolutionRecord) throws Exception {
        final UpdateDomainRecordRequest updateDomainRecordRequest = new UpdateDomainRecordRequest();
        updateDomainRecordRequest.setRecordId(resolutionRecord.getRecordId());
        updateDomainRecordRequest.setType(resolutionRecord.getRecordType());
        updateDomainRecordRequest.setLine(resolutionRecord.getRecordLine());
        updateDomainRecordRequest.setValue(resolutionRecord.getValue());
        updateDomainRecordRequest.setRR(resolutionRecord.getSubDomain());

        Optional.ofNullable(resolutionRecord.getMx()).ifPresent(mx -> updateDomainRecordRequest.setPriority(mx.longValue()));

        log.info("[ALiYunDNSServer] update request:{}", JSON.toJSONString(updateDomainRecordRequest));
        final UpdateDomainRecordResponse updateResponse = client.updateDomainRecord(updateDomainRecordRequest);
        log.info("[ALiYunDNSServer] update response:{}", JSON.toJSONString(updateResponse));

        return Optional.ofNullable(updateResponse).map(UpdateDomainRecordResponse::getBody).map(UpdateDomainRecordResponseBody::getRecordId).isPresent();
    }
}
