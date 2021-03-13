# ddns-general
通用的ddns服务器代码
（目前支持腾讯云）

# 配置项:
    
* dns服务器类型
 
    * ${sunyc.dns.server.type}

* dns服务器必须参数,json格式字符串。

    * ${sunyc.dns.server.param}
            
        * 腾讯云：login_token 登陆令牌，在腾讯云官网 [token设置页][tencent_token]生成login_token
        * 华为云：等待支持... 
        * 阿里云：等待支持...

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

# 依赖项:

项目中获取物理机外网ip调用的是孙玉朝的ip检测接口[post:   http://ip.sunyc.cn](http://ip.sunyc.cn)


[tencent_token]: https://console.dnspod.cn/account/token