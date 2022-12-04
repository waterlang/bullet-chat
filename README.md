# bullet-chat
# 直播弹幕系统
该弹幕系统主要四大组件组成:
comet 接入层：该模块主要负责与client端（web浏览器）建立websocket长链接并维持长链接稳定性; <br>
logic 逻辑处理层：该模块主要提供：消息转发（mq）、登录逻辑处理、记录client与comet实例之间的关系、为client端提供可用的comet实例、为外部
提供http直接发送消息的能力;
push 消息路由推送层：用户发送消到到logic,logic将消息发送到Mq中，push从Mq获取消息进行处理，然后通过logic获取消息的目的地（具体的comet实例），
并获取对应实例（rpc路由）并消息推送至comet，再由comet长链接推送到client端;
client 客户端：与comet进行建立链接，接收消息和发送消息;

该项目只提供了基本的收发消息能力，比如像消息的敏感词、消息过滤及限流、client询址（server）等能力并没有提供。启动该项目还需要依赖
kafka,redis,nacos 这几个中间件。本项目原则上支持水平扩展，但这个到了一定量级后要考虑的是db的路由链接等能力。

## 架构图：
<img width="771" alt="图片" src="https://user-images.githubusercontent.com/9714046/205480401-2d42e61d-73a9-4b29-a167-4f84d54ad951.png">
