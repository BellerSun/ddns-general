package cn.sunyc.ddnsgeneral.core.server;

import cn.sunyc.ddnsgeneral.domain.ResolutionRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class HuaWeiDNSServer extends BaseDNSServer{
    @Override
    protected Collection<String> getCheckParams() {
        return null;
    }

    @Override
    public List<ResolutionRecord> queryList(String domainName) throws Exception {
        return null;
    }

    @Override
    public boolean updateResolutionRecord(ResolutionRecord resolutionRecord) throws Exception {
        return false;
    }
}
