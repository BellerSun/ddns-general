package cn.sunyc.ddnsgeneral.sql;

import cn.sunyc.ddnsgeneral.domain.db.GlobalConfigDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author sun yu chao
 * @version 1.0
 * @since 2023/1/21 06:22
 */
@Component
public interface GlobalConfigRepository extends JpaRepository<GlobalConfigDO, String> {
}
