package cn.sunyc.ddnsgeneral.core.server.impl;

import cn.sunyc.ddnsgeneral.core.server.BaseDNSServer;
import cn.sunyc.ddnsgeneral.core.server.param.InitArgsALi;
import cn.sunyc.ddnsgeneral.domain.resolution.BaseResolutionRecord;
import com.alibaba.fastjson.JSON;
import com.aliyun.alidns20150109.Client;
import com.aliyun.alidns20150109.models.*;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ALiYunDNSServer extends BaseDNSServer<BaseResolutionRecord, InitArgsALi> {

    private Client client;

    @Override
    protected void initSub(InitArgsALi initArgsALi) {
        final Config config = new Config()
                .setAccessKeyId(initArgsALi.getAk())
                .setAccessKeySecret(initArgsALi.getSk())
                .setEndpoint(initArgsALi.getEndpoint());
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
        // 这里尽量直接查出来所有,所以写了很多个
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
