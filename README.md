## 在线教育平台包含的微服务

在线教育平台服务端基于Spring Boot构建，采用Spring Cloud微服务框架。

持久层：MySQL、MongoDB、Redis、ElasticSearch

数据访问层：使用Spring Data JPA 、Mybatis、Spring Data Mongodb等

业务层：Spring IOC、Aop事务控制、Spring Task任务调度、Feign、Ribbon、Spring AMQP、Spring Data Redis等。

控制层：Spring MVC、FastJSON、RestTemplate、Spring Security Oauth2+JWT等

微服务治理：Eureka、Zuul、Hystrix、Spring Cloud Config等

![1584103004929](https://s1.ax1x.com/2020/03/13/8KcGTK.png)
## 1.1   媒资管理微服务( [xc-service-manage-media](https://github.com/Binn-Xu/xc-online-backend/tree/master/xc-service-manage-media) ,  [xc-service-manage-media-processor](https://github.com/Binn-Xu/xc-online-backend/tree/master/xc-service-manage-media-processor) )

> 功能需求

​	  		每个教学机构都可以在媒资系统管理自己的教学资源，包括：视频、教案等文件。

- 媒资管理的主要管理对象是课程录播视频，包括：媒资文件的查询、视频上传、视频删除、视频处理等。
  - 媒资查询：教学机构查询自己所拥有的媒体文件。
  - 视频上传：将用户线下录制的教学视频上传到媒资系统。
  - 视频处理：视频上传成功，系统自动对视频进行编码处理。
  - 视频删除 ：如果该视频已不再使用，可以从媒资系统删除  

-   本项目采用 HLS 技术实现视频点播
  
  ​    1、使用FFmpeg对视频进行编码处理，生成m3u8文件及ts文件
  
  ​    2、使用Nginx作为媒体服务器 
  
  ​    3、客户端使用video.js播放视频 
## 1.2搜索微服务( [xc-service-search](https://github.com/Binn-Xu/xc-online-backend/tree/master/xc-service-search) )

![](https://s1.ax1x.com/2020/03/13/8Kc3ex.png)

## 1.3 文件上传微服务( [xc-service-oss-filesystem](https://github.com/Binn-Xu/xc-online-backend/tree/master/xc-service-oss-filesystem) )

使用阿里云oss服务实现图片上传的需求
## 1.4 网关微服务( [xc-govern-gateway](https://github.com/Binn-Xu/xc-online-backend/tree/master/xc-govern-gateway) )

> 架构图

![*](https://img-blog.csdnimg.cn/20181214102311483.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x5ajIwMThneXE=,size_16,color_FFFFFF,t_70)


不管是来自于客户端（PC或移动端）的请求，还是服务内部调用。一切对服务的请求都会经过Zuul这个网关，然后再由网关来实现 鉴权、动态路由等等操作。Zuul就是我们服务的统一入口。

服务网关是微服务架构中一个不可或缺的部分。通过服务网关统一向外系统提供REST API的过程中，除了具备服务路由、均衡负载功能之外，它还具备了`权限控制`等功能。为微服务架构提供了前门保护的作用，同时将权限控制这些较重的非业务逻辑内容迁移到服务路由层面，使得服务集群主体能够具备更高的可复用性和可测试性。

> 主要功能

身份认证与安全：识别每个资源的验证要求，并拒绝那些与要求不相符的请求。（对jwt鉴权）

![1584092965569](https://s1.ax1x.com/2020/03/13/8KcMl9.png)

动态路由：动态地将请求路由到不同的后端集群。

![1584092126872](https://s1.ax1x.com/2020/03/13/8KcGTK.png)

负载均衡(loadBalance)

## 1.5 授权中心微服务( [xc-service-ucenter-auth](https://github.com/Binn-Xu/xc-online-backend/tree/master/xc-service-ucenter-auth) )

> Spring security + Oauth2完成用户认证及用户授权

认证授权流程如下：
1、用户请求认证服务完成身份认证。
2、认证服务下发用户身份令牌和JWT令牌，拥有身份令牌表示身份合法，Jwt令牌用于完成授权。
3、用户携带jwt令牌请求资源服务。
4、网关校验用户身份令牌的合法，不合法表示用户没有登录，如果合法则放行继续访问。
5、资源服务获取jwt令牌，根据jwt令牌完成授权  

## 1.6 注册中心( [xc-govern-center](https://github.com/Binn-Xu/xc-online-backend/tree/master/xc-govern-center) )

> 基本架构

![img](https://img-blog.csdnimg.cn/2018121514422783.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2x5ajIwMThneXE=,size_16,color_FFFFFF,t_70)


- Eureka：就是服务注册中心（可以是一个集群），对外暴露自己的地址
- 提供者：启动后向Eureka注册自己信息（地址，提供什么服务）
- 消费者：向Eureka订阅服务，Eureka会将对应服务的所有提供者地址列表发送给消费者，并且定期更新
- 心跳(续约)：提供者定期通过http方式向Eureka刷新自己的状态

主要功能就是对各种服务进行管理。

## 如何启动项目

在虚拟机中通过docker进行以下中间件与数据库的配置：

- ES：搜索
- nginx：反向代理
- Rabbitmq：数据同步，消息中间件
- Redis：缓存
- mongodb：后端数据库

并将配置文件中所有和虚拟机相关的ip进行修改

本机中需要的配置：

- nginx：前端所有请求统一代理到网关，域名的反向代理
- host：实现域名访问

## 数据库与配置文件

封存在config文件夹下，包括项目中最终版本的nginx.conf，以及本项目所配套的完整mongodb和mysql数据库
