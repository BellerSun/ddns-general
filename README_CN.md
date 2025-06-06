## ddns-general
通用的ddns服务器代码，使用H2在本地存储你的DNS解析服务商配置。

[English](https://github.com/BellerSun/ddns-general/blob/master/README.md) |
[简体中文](https://github.com/BellerSun/ddns-general/blob/master/README_CN.md)
----------
## 部署:
Docker镜像部署:[DockerHub](https://hub.docker.com/r/bellersun/ddns-general)
```shell
# 拉镜像，tag需要根据情况自行选择，这里使用测试tag
docker pull bellersun/ddns-general:test-01
# [3364] 是管理页面端口.
# [/root/ddns-general/h2] 是存储到本地的配置，如果不挂载出来，重新启动镜像会丢失配置。
docker run -d -p 3364:3364 -v ~/ddns-general/h2:/root/ddns-general/h2  bellersun/ddns-general:test-01
```
Jar部署:
```shell
java -jar ddns-general.jar
```
----------
## 链接:
本地H2数据库地址:[http://localhost:3364/console](http://localhost:3364/console)  账号SA，无密码

本地后台管理页面地址:[http://localhost:3364/html/index](http://localhost:3364/html/index)
![image](https://github.com/user-attachments/assets/efb9c3ad-db33-4b86-a00b-3146441b5095)
----------
## 更新特性:
后续会添加更多功能：
  * 端口、日志目录、配置目录支持环境变量配置
  * 更多域名解析商支持（如果没有你想要的，可以提Issues）
  * 页面管理H2中的DNS服务解析商配置（哪位小伙伴可以帮忙做页面~已经搭建[基础页面](http://localhost:3364/html/index)）

----------
## H2数据库配置项:
* dns解析服务商类型
  * TENCENT：腾讯云
  * ALIYUN：阿里云
  * HUAWEI：华为云
* dns解析服务商必须参数,json格式字符串。
  * 腾讯云：ak访问id，sk访问密匙，在腾讯云个人中心 [访问管理页][tencent_token]生成SecretId(ak)、SecretKey(sk)
  ```json
  {"ak": "yourSecretId","sk": "yourSecretKey"}
  ```
  * 阿里云：ak访问id，sk访问密匙，在阿里云个人中心 [用户信息管理页][aliyun_token]生成ak、sk
  ```json
  {"ak": "yourAk","sk": "yourSk"}
  ```
  * 华为云：ak访问id，sk访问密匙，在华为云个人中心 [我的凭证页][huawei_token]生成ak、sk
  ```json
  {"ak": "yourAk","sk": "yourSk"}
  ```
* 主域名:【baidu.com】
* 解析记录值:【www、wiki、@】
* 解析记录类型:【A】
* 查询出来的记录本地的缓存时间。减少调运营商API频次（秒）:【60000】
* 定时任务Cron字符串:【0 0/1 * * * ? 】
* 是否激活定时任务:【true】
  ![image](https://github.com/user-attachments/assets/196e908b-ad37-405d-afef-8fdeab759790)

----------
## 依赖项:
项目中获取物理机外网ip调用的是孙玉朝的ip检测接口[POST:  http://ip.apache.plus](http://ip.apache.plus)

----------
## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) Copyright (C) Apache Software Foundation

[tencent_token]: https://console.cloud.tencent.com/cam/capi
[aliyun_token]: https://usercenter.console.aliyun.com/?spm=api-workbench.API%20Explorer.0.0.113b1e0fG0CkQG#/manage/ak
[huawei_token]: https://console.huaweicloud.com/iam/?region=cn-north-4&locale=zh-cn#/mine/accessKey
