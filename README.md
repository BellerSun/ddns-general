## ddns-general
通用的ddns服务器代码


----------
## 更新特性:
后续会添加更多功能： 
  * 通过启动参数指定域名解析商配置
  * docker镜像启动
  * 更多域名解析商支持

----------
## 配置项:
* dns解析服务商类型
    * ${sunyc.dns.server.type}
        * TENCENT：腾讯云
        * ALIYUN：阿里云
        * HUAWEI：华为云
* dns解析服务商必须参数,json格式字符串。
    * ${sunyc.dns.server.param}
        * 腾讯云：login_token 登陆令牌，在腾讯云官网 [token设置页][tencent_token]生成login_token
        * 阿里云：ak访问id，sk访问密匙，在阿里云个人中心 [用户信息管理页][aliyun_token]生成ak、sk
        * 华为云：ak访问id，sk访问密匙，在华为云个人中心 [我的凭证页][huawei_token]生成ak、sk

* 动态解析的主域名【baidu.com】
    * ${sunyc.ddns.domain.name}

* 动态解析的子域名【www、wiki、@、】
    * ${sunyc.ddns.domain.subName}

* 查询出来的解析记录，生存时间。避免每次都去查，过了这个时间长度就不相信该结果了
    * ${sunyc.ddns.record.aliveTime:600000}

* 记录类型
    * ${sunyc.ddns.domain.recordType:A}

* 定时调度字符串
    * ${sunyc.scheduler.cron}

* 是否默认激活定时任务
    * ${sunyc.system.activate:true}

----------
## 依赖项:
项目中获取物理机外网ip调用的是孙玉朝的ip检测接口[post:   http://ip.sunyc.cn](http://ip.sunyc.cn)

----------
## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) Copyright (C) Apache Software Foundation

[tencent_token]: https://console.dnspod.cn/account/token
[aliyun_token]: https://usercenter.console.aliyun.com/?spm=api-workbench.API%20Explorer.0.0.113b1e0fG0CkQG#/manage/ak
[huawei_token]: https://console.huaweicloud.com/iam/?region=cn-north-4&locale=zh-cn#/mine/accessKey