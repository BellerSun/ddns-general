package cn.sunyc.ddnsgeneral.sql;

import cn.sunyc.ddnsgeneral.domain.db.IPCheckerConfigDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

@Component
public interface IPCheckerConfigRepository extends JpaRepository<IPCheckerConfigDO, Long> {
    @Query(value = "SELECT * FROM IP_CHECKER_CONFIG WHERE enable = true LIMIT 1", nativeQuery = true)
    IPCheckerConfigDO findFirstByEnableTrue();
}
