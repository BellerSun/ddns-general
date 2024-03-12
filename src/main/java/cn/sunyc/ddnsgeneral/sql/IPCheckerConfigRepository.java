package cn.sunyc.ddnsgeneral.sql;

import cn.sunyc.ddnsgeneral.domain.db.IPCheckerConfigDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface IPCheckerConfigRepository extends JpaRepository<IPCheckerConfigDO, Long> {
    IPCheckerConfigDO findFirstByEnableTrue();
}
