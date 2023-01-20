## ddns-general
  通用的ddns服务器代码，使用H2在本地存储你的DNS解析服务商配置。   
  H2数据库地址:[http://localhost:3364/console]  账号SA，无密码。    
  后台管理页面地址:[http://localhost:3364/html/index]     
----------
## 更新特性:
后续会添加更多功能： 
  * 通过启动参数指定域名解析商配置
  * docker镜像启动
  * 更多域名解析商支持（如果没有你想要的，可以提Issues）
  * 页面管理H2中的DNS服务解析商配置（哪位小伙伴可以帮忙做个页面~）

----------
## H2数据库配置项:
* dns解析服务商类型
    * ${dnsServerType}
        * TENCENT：腾讯云
        * ALIYUN：阿里云
        * HUAWEI：华为云
* dns解析服务商必须参数,json格式字符串。
    * ${dnsServerParam}
        * 腾讯云：login_token 登陆令牌，在腾讯云官网 [token设置页][tencent_token]生成login_token 
        ```json
        {"login_token": "yourToken"}
        ```
        * 阿里云：ak访问id，sk访问密匙，在阿里云个人中心 [用户信息管理页][aliyun_token]生成ak、sk
        ```json
        {"ak": "yourAk","sk": "yourSk"}
        ```
        * 华为云：ak访问id，sk访问密匙，在华为云个人中心 [我的凭证页][huawei_token]生成ak、sk
        ```json
        {"ak": "yourAk","sk": "yourSk"}
        ```

* 动态解析的主域名:【baidu.com】
    * ${domainName}

* 动态解析的子域名:【www、wiki、@】
    * ${domainSubName}

* 查询出来的记录本地缓存时间。避免每次都查，超过该时间就不相信本地结果（秒）:【60000】
    * ${ddnsRecordAliveTime}

* 记录类型:【A】
    * ${ddnsDomainRecordType}

* 定时调度字符串:【0 0/1 * * * ? 】
    * ${schedulerCron}

* 是否默认激活定时任务:【true】
    * ${activate}

----------
## 依赖项:
项目中获取物理机外网ip调用的是孙玉朝的ip检测接口[post:   http://ip.apache.plus](http://ip.sunyc.cn)

----------
## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) Copyright (C) Apache Software Foundation

[tencent_token]: https://console.dnspod.cn/account/token
[aliyun_token]: https://usercenter.console.aliyun.com/?spm=api-workbench.API%20Explorer.0.0.113b1e0fG0CkQG#/manage/ak
[huawei_token]: https://console.huaweicloud.com/iam/?region=cn-north-4&locale=zh-cn#/mine/accessKey
