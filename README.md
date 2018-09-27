# oceans

oceans ，英文 ocean 大海的复数。没有什么特殊含义，就觉得好听。

自己称这个项目为“与海”，渴望自由。

初衷是希望做一个 B2C 的商城 demo 项目，并且搭配一些可口的文章。但是，貌似困难有点大，所以很可能是不了了之。一个人堆代码，可能有点无趣。我的梦想是，星辰大海。

当然，无论怎么样，还是会通过这个 README 文件，简单介绍下这个项目。

# 1. 概述

## 1.1 大体架构

![架构图](https://github.com/YunaiV/oceans/blob/4a660f7c206dc8058a08b1558d0a58fcdca8dd12/_blog/images/%E6%9E%B6%E6%9E%84%E5%9B%BE.png?raw=true)

考虑到大多数团队的体量，暂时没有必要引入 Gateway ，所以仅仅分成 bff 和 service 两层。其中：

1. bff 层，负责原有网关功能，以及 Dubbo RPC 的 HTTP 封装、拼装等功能。
2. service 层，实现具体的业务逻辑，提供 Dubbo RPC 接口。

## 1.2 代码分层

TODO 此处等待老梁来补充。我去鞭笞老梁。哈哈哈哈

# 2. 全局错误码

可参见如下几个类：

* `cn.iocoder.oceans.core.exception.ServiceException`
* `cn.iocoder.oceans.core.util.ServiceExceptionUtil`
* `cn.iocoder.oceans.user.service.config.ServiceExceptionConfiguration`
* `cn.iocoder.oceans.user.api.constants.ErrorCodeEnum`
* `cn.iocoder.oceans.core.constants.ErrorCodeEnum`

# 3. Passport 功能

## 3.1 User Passport

原本在 User Passport 准备基于 Spring Security + Spring Security OAuth2 框架进行实现。但是考虑到，User Passport 使用 OAuth2 略显笨重，使用 Spring Security 也略显笨重。并且，User Passport 往往有比较飘逸的注册需求，例如：手机验证码登陆，并且未注册的情况下，自动注册。

所以考虑再三，并且尝试使用 Spring Security + Spring Security OAuth2 进行了一次实现后决定，还是选择自己基于 OAuth2 的思想，实现一个 Lite 版。具体可参见如下几个类：

* `cn.iocoder.oceans.user.service.impl.OAuth2ServiceImpl`
* `cn.iocoder.oceans.user.service.impl.MobileCodeServiceImpl`
* `cn.iocoder.oceans.user.service.impl.UserServiceImpl`
* `cn.iocoder.oceans.webapp.bff.interceptor.SecurityInterceptor`
* `cn.iocoder.oceans.webapp.bff.context.SecurityContext`
* `cn.iocoder.oceans.webapp.bff.context.SecurityContextHolder`
* `cn.iocoder.oceans.webapp.bff.controller.passport.PassportController`
* `cn.iocoder.oceans.webapp.bff.annotation.@PermitAll`

当然，如上实现是直接基于 MySQL 实现，实际最好引入 Redis 进行缓存，以提高性能。

## 3.2 Admin Passport

TODO 未完成

# 4. Rest 响应格式

统一响应格式如下：

```JSON
{
    code: , // 错误码，使用全局错误码。成功时，使用 0 。
    msg: , // 错误提示。成功时，返回 "" 。
    data: // 具体业务数据。不定具体的格式，可以是 String ，也可以是对象，也可以是数组。
}
```

可参见类：

* `cn.iocoder.oceans.webapp.bff.vo.RestResult`
* `cn.iocoder.oceans.webapp.bff.config.GlobalResponseBodyAdvice`
* `cn.iocoder.oceans.webapp.bff.config.GlobalExceptionHandler`

# 5. 各种 POJO

Java 各种 O ，也是心烦。

* Controller 返回，使用 VO 或者 DTO 。
* Dubbo Service API 入参和返回，统一 DTO 。
* Dubbo Service Impl 之间，可以返回 PO 。
* DAO 入参和返回，统一 PO 。

# 6. 分布式事务

说起分布式，理论的文章很多，落地的实践很少。笔者翻阅了各种分布式事务组件的选型，大体如下：

* TCC 模型：TCC-Transaction、Hmily( How much i love you )
* XA 模型：Sharding Sphere、MyCAT
* 2PC 模型：raincat、lcn
* MQ 模型：RocketMQ
* BED 模型：Sharding Sphere
* Saga 模型：ServiceComb Saga

那怎么选择呢？目前社区对于分布式事务的选择，暂时没有定论，至少笔者没有看到。笔者的想法如下：

* 从覆盖场景来说，TCC 无疑是最优秀的，但是大家觉得相对复杂。实际上，复杂场景下，使用 TCC 来实现，反倒会容易很多。另外，TCC 模型，一直没有大厂开源，也是一大痛点。
* 从使用建议来说，MQ 可能是相对合适的( 不说 XA 的原因还是性能问题 )，并且基本轮询了一圈朋友，发现大多都是使用 MQ 实现最终一致性居多。
* 2PC 模型的实现，笔者觉得非常新奇，奈何隔离性是一个硬伤。
* Saga 模型，可以认为是 TCC 模型的简化版，所以在理解和编写的难度上，简单非常多。因为 ServiceComb Saga **好像**不支持 Dubbo ，所以并未深入去研究。

最终，笔者选择了 MQ 模型。但是 MQ 模型会有一个问题，在于前置的资源锁定问题。那么怎么解决呢？笔者以下单业务为例，可参见代码如下：

* `cn.iocoder.oceans.order.service.impl.OrderServiceImpl`
* `cn.iocoder.oceans.order.service.po.OrderPO`
* `cn.iocoder.oceans.core.base.mq.TransactionMQProducer`

大体流程是：

1. 扣除库存（考虑模型简单，直接使用商品服务管理库存）
2. 使用优惠劵
3. 创建订单

实现方式：

1. 在 `OrderServiceImpl#createInitOrder(...)` 方法中，**事务 A** ，创建一个处于**初始化**状态的订单。该状态下的订单，用户不可见。什么用呢？下面说。
2. 在 `OrderServiceImpl#initOrder(...)` 方法中，**事务 B**，远程调用=>扣除库存，远程调用=>使用优惠劵，更新订单状态为**待支付**。那么可能会有胖友会说，远程调用会失败呢？确实是，并且更麻烦的是，这是跨进程跨事务，无法直接事务回滚！这个时候，**初始化**状态的订单的作用就出来了。后台定时任务 `OrderServiceImpl#pollingTimeoutOrderApply()` ，不断轮询超过 n 分钟( 例如说 5 分钟 )未初始化完成的订单( 即订单状态修改为**待支付**的订单 )。理论来说，订单 n 分钟内肯定可以初始化完成，如果未初始化完成，说明已经失败了，所以需要回滚扣除的库存和使用的优惠劵。但是，要注意，回滚扣除库存的方法和使用的优惠劵的方法，需要保证幂等性，因为可能会存在重复调用。通过这样的方式，我们解决了，MQ 方式无法前置的资源锁定的问题。
3. 考虑到订单创建成功后，可能还有后置的逻辑，例如说，通知商家、赠送积分、赠送优惠劵等等。并且考虑到性能，那么我们适合发送 MQ 消息。但是为了 MQ 消息的稳定送达，需要使用 RocketMQ 事务消息。详细见，`OrderServiceImpl#createOrder(...)` 方法。
4. 考虑到【2】中，使用定时任务轮询超时初始化的订单，锁定的资源太久，所以在 `OrderServiceImpl#createOrder(...)` 方法中，在捕捉到 `OrderServiceImpl#initOrder(...)` 方法中发生异常时，发送一条消息，异步取消资源的锁定。

😈 这块写的比较匆忙，如果有不理解的胖友，欢迎关注我的公众号【芋道源码】给我留言。因为时间关系，示例代码并没有完整实现完。嘿嘿。

😈 实际上，如果下单流程能够引入 TCC 模型的框架，代码会容易实现非常多。感兴趣的胖友，可以自己思考下。

# 7. 定时任务

TODO 

# 8. 消息队列

TODO

# 9. 配置中心

TODO Apollo

# 10. 链路追踪

TODO SkyWalking

# 11. 服务保障

TODO Sentinel

# 12. 分库分表

TODO Sharding Sphere

# 13. 服务网关

TODO Spring Cloud Gateway

# 666. 未完