package cn.sunyc.ddnsgeneral;

import cn.sunyc.ddnsgeneral.core.server.impl.ALiYunDNSServer;
import cn.sunyc.ddnsgeneral.core.server.impl.HuaWeiDNSServer;
import cn.sunyc.ddnsgeneral.core.server.IDNSServer;
import cn.sunyc.ddnsgeneral.core.server.impl.TencentDNSServer;
import cn.sunyc.ddnsgeneral.core.server.param.*;
import cn.sunyc.ddnsgeneral.domain.resolution.BaseResolutionRecord;
import cn.sunyc.ddnsgeneral.domain.resolution.HuaWeiResolutionRecord;
import cn.sunyc.ddnsgeneral.utils.HttpUtil;
import cn.sunyc.ddnsgeneral.utils.IpUtil;
import cn.sunyc.ddnsgeneral.utils.ObjUtil;
import cn.sunyc.ddnsgeneral.utils.ValidateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author ：sun yu chao
 * @date ：Created in 2021/3/13 16:16
 * @description： desc
 * @modified By：none
 * @version: 1.0.0
 */
public class NoneContextTests {
    @Test
    void main() {
        System.out.println("hello");
    }

    @Test
    void testGetIp() throws Exception {
        String post = IpUtil.getOutSideIp();

        System.out.println(post);
    }

    @Test
    void testTencentDnsApi() throws Exception {

        /*
         * 参数名称 	                描述 	                                        必选
         * login_token 	            用于鉴权的 API Token。 	                        是
         * format {json,xml} 	    返回的数据格式，默认为xml，建议用 json。 	    否
         * lang {en,cn} 	        返回的错误语言，默认为en，建议用cn。 	        否
         * error_on_empty {yes,no} 	没有数据时是否返回错误，默认为yes，建议用no。    否
         * user_id 	用户的ID，仅代理接口需要，用户接口不需要提交此参数。 	            否
         */
        HashMap<String, String> param = new HashMap<>();
        param.put("login_token", "");
        param.put("format", "json");
        param.put("lang", "cn");
        param.put("error_on_empty", "no");
        param.put("domain", "sunyc.cn");

        String response = HttpUtil.post("https://dnsapi.cn/Record.List", param);


        System.out.println(response);
    }


    @Test
    void testTencentServer() throws Exception {
        String ak = "";
        String sk = "";
        String domainName = "sunasan.cn";
        String subDomainName = "www";
        String type = "A";


        IDNSServer<BaseResolutionRecord> idnsServer = new TencentDNSServer();
        JSONObject initializeParam = new JSONObject();
        initializeParam.put("ak", ak);
        initializeParam.put("sk", sk);
        idnsServer.init(initializeParam);

        List<BaseResolutionRecord> baseResolutionRecords = idnsServer.queryList(domainName);

        List<BaseResolutionRecord> targetRecords = baseResolutionRecords.stream()
                .filter(Objects::nonNull)
                .filter(record -> domainName.equals(record.getDomain()))
                .filter(record -> subDomainName.equals(record.getSubDomain()))
                .filter(record -> type.equals(record.getRecordType()))
                .collect(Collectors.toList());


        if (targetRecords.size() > 1) {
            throw new RuntimeException("您的输入条件查询出来了多条记录，俺不知道要修改哪一个了。");
        } else if (targetRecords.size() < 1) {
            throw new RuntimeException("您的输入条件没有查询到记录，俺不知道要修改哪一个了。");
        }
        BaseResolutionRecord baseResolutionRecord = targetRecords.get(0);
        System.out.println("查询出来的结果：");
        System.out.println(JSON.toJSONString(baseResolutionRecord));

        baseResolutionRecord.setValue("188.188.188.188");
        boolean result = idnsServer.updateResolutionRecord(baseResolutionRecord);
        System.out.println("修改结果：");
        System.out.println(result);
    }

    @Test
    void testAliYunServer() throws Exception {
        String ak = "";
        String sk = "";
        String endpoint = "alidns.cn-beijing.aliyuncs.com";
        String domainName = "springboot.top";
        String subDomainName = "www";
        String type = "A";


        IDNSServer<BaseResolutionRecord> idnsServer = new ALiYunDNSServer();
        JSONObject initializeParam = new JSONObject();
        initializeParam.put("endpoint", endpoint);
        initializeParam.put("ak", ak);
        initializeParam.put("sk", sk);
        idnsServer.init(initializeParam);

        List<BaseResolutionRecord> baseResolutionRecords = idnsServer.queryList(domainName);

        List<BaseResolutionRecord> targetRecords = baseResolutionRecords.stream()
                .filter(Objects::nonNull)
                .filter(record -> domainName.equals(record.getDomain()))
                .filter(record -> subDomainName.equals(record.getSubDomain()))
                .filter(record -> type.equals(record.getRecordType()))
                .collect(Collectors.toList());


        if (targetRecords.size() > 1) {
            throw new RuntimeException("您的输入条件查询出来了多条记录，俺不知道要修改哪一个了。");
        } else if (targetRecords.size() < 1) {
            throw new RuntimeException("您的输入条件没有查询到记录，俺不知道要修改哪一个了。");
        }
        BaseResolutionRecord baseResolutionRecord = targetRecords.get(0);
        System.out.println("查询出来的结果：");
        System.out.println(JSON.toJSONString(baseResolutionRecord));

        baseResolutionRecord.setValue("188.188.188.188");
        boolean result = idnsServer.updateResolutionRecord(baseResolutionRecord);
        System.out.println("修改结果：");
        System.out.println(result);
    }


    @Test
    void testHuaWeiServer() throws Exception {
        String ak = "";
        String sk = "";
        String domainName = "hadoop.net.cn";
        String subDomainName = "www";
        String type = "A";

        IDNSServer<HuaWeiResolutionRecord> idnsServer = new HuaWeiDNSServer();
        JSONObject initializeParam = new JSONObject();
        initializeParam.put("ak", ak);
        initializeParam.put("sk", sk);
        idnsServer.init(initializeParam);

        List<HuaWeiResolutionRecord> baseResolutionRecords = idnsServer.queryList(domainName);

        List<HuaWeiResolutionRecord> targetRecords = baseResolutionRecords.stream()
                .filter(Objects::nonNull)
                .filter(record -> domainName.equals(record.getDomain()))
                .filter(record -> subDomainName.equals(record.getSubDomain()))
                .filter(record -> type.equals(record.getRecordType()))
                .collect(Collectors.toList());


        if (targetRecords.size() > 1) {
            throw new RuntimeException("您的输入条件查询出来了多条记录，俺不知道要修改哪一个了。");
        } else if (targetRecords.size() < 1) {
            throw new RuntimeException("您的输入条件没有查询到记录，俺不知道要修改哪一个了。");
        }
        HuaWeiResolutionRecord baseResolutionRecord = targetRecords.get(0);
        System.out.println("查询出来的结果：");
        System.out.println(JSON.toJSONString(baseResolutionRecord));

        baseResolutionRecord.setValue("188.188.188.188");
        boolean result = idnsServer.updateResolutionRecord(baseResolutionRecord);
        System.out.println("修改结果：");
        System.out.println(result);
    }


    @Test
    public void testInitArgsCheck() {
        // 通过
        InitArgsAbs args = new InitArgsALi("a", "b", "c");
        ValidateUtil.validate(args);
        ValidateUtil.valid(args);

        // 通过
        args = new InitArgsALi("a", "b", null);
        ValidateUtil.validate(args);
        ValidateUtil.valid(args);

        // 不通过
        //args = new InitArgsALi("a", null, null);
        //      on field 'sk': rejected value [null]
        //ValidateUtil.validate(args);
        //      不能为空
        //ValidateUtil.validCheck(args);

        // 不通过
        //args = new InitArgsALi();
        //      on field 'ak': rejected value [null]  on field 'sk': rejected value [null]
        //ValidateUtil.validate(args);
        //      不能为空
        //ValidateUtil.validCheck(args);

        // 通过
        args = new InitArgsAkSK("a", "b");
        ValidateUtil.validate(args);
        ValidateUtil.valid(args);


        // 不通过
        args = new InitArgsAkSK(null, "b");
        //ValidateUtil.validate(args);
        //ValidateUtil.validCheck(args);


        // 通过
        args = new InitArgsToken("a");
        ValidateUtil.validate(args);
        ValidateUtil.valid(args);

        // 通过
        args = new InitArgsTencent("a", "b", null);
        ValidateUtil.validate(args);
        ValidateUtil.valid(args);

        // 通过
        args = new InitArgsTencent("a", "b", "ap-shanghai");
        ValidateUtil.validate(args);
        ValidateUtil.valid(args);

        /* 不通过
        //args = new InitArgsToken();
        //ValidateUtil.validate(args);
        //ValidateUtil.validCheck(args);
        */
    }


    @Test
    public void testReflect() {
        final ALiYunDNSServer aLiYunDNSServer = new ALiYunDNSServer();
        System.out.println(ObjUtil.getGenericType(aLiYunDNSServer));
        System.out.println(ObjUtil.getGenericType(aLiYunDNSServer, InitArgsAbs.class));
    }


    @Test
    public void testIp() {
        String outSideIp = IpUtil.getOutSideIp();
        System.out.println(outSideIp);
    }
}
