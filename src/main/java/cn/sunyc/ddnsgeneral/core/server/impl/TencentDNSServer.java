package cn.sunyc.ddnsgeneral.core.server.impl;

import cn.sunyc.ddnsgeneral.core.server.BaseDNSServer;
import cn.sunyc.ddnsgeneral.core.server.param.InitArgsTencent;
import cn.sunyc.ddnsgeneral.domain.resolution.BaseResolutionRecord;
import com.alibaba.fastjson.JSON;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.dnspod.v20210323.DnspodClient;
import com.tencentcloudapi.dnspod.v20210323.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 腾讯云DNSPod服务器实现类 - 使用最新SDK
 * 
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 17:14
 * @modified By：2024/1/1 更新使用最新腾讯云DNSPod SDK
 * @version: 2.0.0
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TencentDNSServer extends BaseDNSServer<BaseResolutionRecord, InitArgsTencent> {

    private DnspodClient client;

    @Override
    protected void initSub(InitArgsTencent initArgsTencent) {
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey
            Credential cred = new Credential(initArgsTencent.getAk(), initArgsTencent.getSk());
            
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("dnspod.tencentcloudapi.com");
            
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            
            // 实例化要请求产品的client对象，clientProfile是可选的
            this.client = new DnspodClient(cred, initArgsTencent.getRegion(), clientProfile);
            
            log.info("[TencentDNSServer] init success.");
        } catch (Exception e) {
            log.error("[TencentDNSServer] init error.", e);
            throw new IllegalArgumentException("TencentDNSServer 初始化失败:" + e.getMessage());
        }
    }

    @Override
    public List<BaseResolutionRecord> queryList(String domainName) throws Exception {
        try {
            // 实例化一个请求对象，每个接口都会对应一个request对象
            DescribeRecordListRequest req = new DescribeRecordListRequest();
            req.setDomain(domainName);
            req.setLimit(3000L); // 设置较大的限制来获取所有记录
            
            // 返回的resp是一个DescribeRecordListResponse的实例，与请求对象对应
            DescribeRecordListResponse resp = client.DescribeRecordList(req);
            
            List<RecordListItem> records = Optional.ofNullable(resp.getRecordList())
                    .map(java.util.Arrays::asList)
                    .orElse(new ArrayList<>());
            
            List<BaseResolutionRecord> baseResolutionRecords = records.stream()
                    .map(record -> {
                        BaseResolutionRecord resolutionRecord = new BaseResolutionRecord();
                        resolutionRecord.setRecordId(String.valueOf(record.getRecordId()));
                        resolutionRecord.setDomain(domainName);
                        resolutionRecord.setSubDomain(record.getName());
                        resolutionRecord.setRecordLine(record.getLine());
                        resolutionRecord.setRecordType(record.getType());
                        resolutionRecord.setValue(record.getValue());
                        
                        // MX记录的优先级
                        Optional.ofNullable(record.getMX()).ifPresent(mx -> 
                                resolutionRecord.setMx(Math.toIntExact(mx)));
                        
                        return resolutionRecord;
                    }).collect(Collectors.toList());
            
            log.info("[TencentDNSServer] resolutionRecords:{}", JSON.toJSONString(baseResolutionRecords));
            return baseResolutionRecords;
            
        } catch (TencentCloudSDKException e) {
            log.error("[TencentDNSServer] queryList error.", e);
            throw new RuntimeException("查询解析记录失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateResolutionRecord(BaseResolutionRecord resolutionRecord) throws Exception {
        try {
            // 实例化一个请求对象
            ModifyRecordRequest req = new ModifyRecordRequest();
            req.setDomain(resolutionRecord.getDomain());
            req.setRecordId(Long.valueOf(resolutionRecord.getRecordId()));
            req.setRecordType(resolutionRecord.getRecordType());
            req.setRecordLine(resolutionRecord.getRecordLine());
            req.setValue(resolutionRecord.getValue());
            req.setSubDomain(resolutionRecord.getSubDomain());
            
            // 设置MX记录的优先级
            Optional.ofNullable(resolutionRecord.getMx())
                    .ifPresent(mx -> req.setMX(mx.longValue()));
            
            log.info("[TencentDNSServer] update request:{}", JSON.toJSONString(req));
            
            // 返回的resp是一个ModifyRecordResponse的实例，与请求对象对应
            ModifyRecordResponse resp = client.ModifyRecord(req);
            
            log.info("[TencentDNSServer] update response:{}", JSON.toJSONString(resp));
            
            // 检查响应是否成功，如果有RecordId返回则表示成功
            return resp.getRecordId() != null && resp.getRecordId() > 0;
            
        } catch (TencentCloudSDKException e) {
            log.error("[TencentDNSServer] updateResolutionRecord error.", e);
            throw new RuntimeException("更新解析记录失败: " + e.getMessage(), e);
        }
    }
}
