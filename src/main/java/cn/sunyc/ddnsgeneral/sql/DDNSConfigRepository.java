package cn.sunyc.ddnsgeneral.sql;

import cn.sunyc.ddnsgeneral.domain.db.DDNSConfigDO;
import cn.sunyc.ddnsgeneral.domain.db.key.DDNSConfigKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface DDNSConfigRepository extends JpaRepository<DDNSConfigDO, DDNSConfigKey> {
}
