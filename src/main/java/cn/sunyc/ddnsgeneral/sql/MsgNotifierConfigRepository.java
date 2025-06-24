package cn.sunyc.ddnsgeneral.sql;

import cn.sunyc.ddnsgeneral.domain.db.MsgNotifierConfigDO;
import cn.sunyc.ddnsgeneral.enumeration.MsgNotifierType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息通知配置仓库接口
 * @author sun yu chao
 * @version 1.0
 */
@Component
public interface MsgNotifierConfigRepository extends JpaRepository<MsgNotifierConfigDO, Long> {

    /**
     * 根据配置名称查询配置
     * @param configName 配置名称
     * @return 消息通知配置
     */
    MsgNotifierConfigDO findByConfigName(String configName);
} 