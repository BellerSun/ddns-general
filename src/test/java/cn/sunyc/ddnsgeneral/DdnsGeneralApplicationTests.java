package cn.sunyc.ddnsgeneral;

import cn.sunyc.ddnsgeneral.domain.db.DDNSConfigDO;
import cn.sunyc.ddnsgeneral.sql.DDNSConfigRepository;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class DdnsGeneralApplicationTests {

    @Test
    void contextLoads() {
    }


    @Resource
    DDNSConfigRepository ddnsConfigRepository;

    @Test
    public void testSQL(){
        List<DDNSConfigDO> all = ddnsConfigRepository.findAll();
        System.out.println(JSON.toJSONString(all));
    }
}
