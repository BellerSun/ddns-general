## ddns-general
General ddns server code that locally stores configurations for your DNS resolution service providers using H2.

[English](https://github.com/BellerSun/ddns-general/blob/master/README.md) |
[简体中文](https://github.com/BellerSun/ddns-general/blob/master/README_CN.md)
----------
## Deployment:
Docker image deployment: [DockerHub](https://hub.docker.com/r/bellersun/ddns-general)
```shell
# Pull the image, choose the tag according to your needs, using the test tag here
docker pull bellersun/ddns-general:test-01
# [3364] is the management page port.
# [/root/ddns-general/h2] is the locally stored configuration; if not mounted, the configuration will be lost when restarting the image.
docker run -d -p 3364:3364 -v ~/ddns-general/h2:/root/ddns-general/h2 bellersun/ddns-general:test-01
```
Jar deployment:
```shell
java -jar ddns-general.jar
```
----------
## Links:
Local H2 database address:[http://localhost:3364/console](http://localhost:3364/console)  Account: SA, no password

Local backend management page address: [http://localhost:3364/html/index](http://localhost:3364/html/index)
----------
## Update Features:
More features will be added in the future:
  * Support for configuring environment variables for port, log directory, and configuration directory
  * Support for more domain resolution providers (If the one you want is not available, please raise an issue)
  * age management of DNS service provider configurations in H2 (Looking for someone to help with the page [Basic Page](http://localhost:3364/html/index)）

----------
## H2 Database Configuration Options:
* DNS resolution service provider type
  * TENCENT: Tencent Cloud
  * ALIYUN: Alibaba Cloud
  * HUAWEI: Huawei Cloud
* Mandatory parameters for DNS resolution service provider, in JSON format.
  * Tencent Cloud: `login_token` - Login token, generate on the Tencent Cloud official website [token setting page][tencent_token]
  ```json
  {"login_token": "yourToken"}
  ```
  * Alibaba Cloud: `ak` - Access key ID, `sk` - Secret access key, generate on the Alibaba Cloud personal center [User Information Management page][aliyun_token]
  ```json
  {"ak": "yourAk","sk": "yourSk"}
  ```
  * Huawei Cloud: `ak` - Access key ID, `sk` - Secret access key, generate on the Huawei Cloud personal center [My Credentials page][huawei_token]
  ```json
  {"ak": "yourAk","sk": "yourSk"}
  ```
* Main Domain: [baidu.com]
* Resolution Record Values: [www, wiki, @]
* Resolution Record Type: [A]
* Local Cache Time for Retrieved Records to Reduce ISP API Calls (seconds): [60000]
* Scheduled Task Cron String: [0 0/1 * * * ?]
* Is Scheduled Task Activated: [true]

----------

## Dependencies:
The project retrieves the public IP of the physical machine using the IP detection API by BellerSun [POST: http://ip.apache.plus](http://ip.apache.plus)


----------
## License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) Copyright (C) Apache Software Foundation

[tencent_token]: https://console.dnspod.cn/account/token
[aliyun_token]: https://usercenter.console.aliyun.com/?spm=api-workbench.API%20Explorer.0.0.113b1e0fG0CkQG#/manage/ak
[huawei_token]: https://console.huaweicloud.com/iam/?region=cn-north-4&locale=zh-cn#/mine/accessKey
