<h1 align="center"><a href="https://github.com/xkcoding/magic-starter/tree/master/magic-starter-log" target="_blank">magic-starter-log</a></h1>
<p align="center">
<a href="https://travis-ci.com/xkcoding/magic-starter" target="_blank"><img alt="Travis-CI" src="https://travis-ci.com/xkcoding/magic-starter.svg?branch=master"/></a>
  <a href="https://search.maven.org/artifact/com.xkcoding/magic-starter-log" target="_blank"><img alt="MAVEN" src="https://img.shields.io/maven-central/v/com.xkcoding/magic-starter-log.svg?color=brightgreen&label=Maven%20Central"></a>
  <a href="https://www.codacy.com/manual/xkcoding/magic-starter?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=xkcoding/magic-starter&amp;utm_campaign=Badge_Grade" target="_blank"><img alt="Codacy" src="https://api.codacy.com/project/badge/Grade/6b998c3a533e451690b4164ab1acd164"/></a>
  <a href="https://xkcoding.com" target="_blank"><img alt="author" src="https://img.shields.io/badge/author-Yangkai.Shen-blue.svg"/></a>
  <a href="https://www.oracle.com/technetwork/java/javase/downloads/index.html" target="_blank"><img alt="JDK" src="https://img.shields.io/badge/JDK-1.8.0_162-orange.svg"/></a>
  <a href="https://docs.spring.io/spring-boot/docs/2.1.8.RELEASE/reference/html/" target="_blank"><img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-2.1.8.RELEASE-brightgreen.svg"/></a>
  <a href="https://github.com/xkcoding/magic-starter/blob/master/LICENSE" target="_blank"><img alt="LICENSE" src="https://img.shields.io/github/license/xkcoding/magic-starter.svg"/></a>
</p>

## 简介

`magic-starter-log` 主要是通过对操作日志封装，使用一个注解记录操作日志，使用 spring event 异步处理日志，同时提供 logback 的通用配置文件。

## 使用

```xml
<dependency>
  <groupId>com.xkcoding</groupId>
  <artifactId>magic-starter-log</artifactId>
  <version>${magic-starter.version}</version>
</dependency>
```

## 快速上手

> magic-starter-log 提供了四种类型的日志方案。
>
> 1. 操作日志(`基于注解 + AOP`)
> 2. 自定义日志(`注入 MagicLogger 发送自定义日志`)
> 3. 错误日志(`使用 LogEventPublisher.publishErrorLogEvent(exception) 发送异常日志 `)
> 4. 请求/响应日志(`默认获取返回值是 R 同时是 Controller 或 RestController 的请求/响应日志，默认关闭，可以在配置文件开启`)
>
> 懒人请直接看demo：https://github.com/xkcoding/magic-starter-log-demo

### 日志收集

- 操作日志

在方法上添加注解 `@OperateLog` 收集操作日志

- 自定义日志

使用 `@Autowired` 注入 `MagicLogger` 发布自定义日志

- 错误日志

常见做法：① 在全局异常拦截的地方，使用 `LogEventPublisher.publishErrorLogEvent(exception);` 记录错误日志 ② 在 catch 代码块记录错误日志

- 请求/响应日志

在配置文件配置 `magic.log.request.enabled` 为 `true` 开启请求/响应日志

### 日志处理

> 1. 继承 `LogHandler` 实现对应日志类型的方法
> 2. 注册为 Spring Bean

```java
/**
 * <p>
 * 日志执行逻辑
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/24 19:43
 */
@Slf4j
public class DemoLogHandler implements LogHandler {
    @Override
    public void handleOperateLog(OperateLogModel logModel) {
        log.info("【OperateLogModel】= {}", JSONUtil.toJsonStr(logModel));
    }

    @Override
    public void handleCustomLog(CustomLogModel logModel) {
        log.info("【CustomLogModel】= {}", JSONUtil.toJsonStr(logModel));
    }

    @Override
    public void handleErrorLog(ErrorLogModel logModel) {
        log.info("【ErrorLogModel】= {}", JSONUtil.toJsonStr(logModel));
    }
}


/**
 * <p>
 * 日志配置类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/24 19:43
 */
@Configuration
public class LogConfig {
    @Bean
    public LogHandler logHandler() {
        return new DemoLogHandler();
    }
}
```

### logback 配置文件

> 1. 会生成 3 种日志文件，①全量日志，便于排查问题，保存7天，最大100M ②INFO日志 ③ERROR日志
> 2. 日志文件名字根据 `spring.application.name` 获取
> 3. 日志文件生成路径根据 `logging.path` 获取

使用方式：

配置 `logging.config` 为 `classpath:com/xkcoding/magic/log/logback/logback-spring.xml` 即可

示例：

```yaml
spring:
  application:
    name: magic-starter-log-demo
logging:
  config: classpath:com/xkcoding/magic/log/logback/logback-spring.xml
```

## 特点

- 基于 Spring Event 异步操作事件
- 基于 Ip2Region 获取对方IP的省市区信息及网络信息(要支持这个功能需要引入 `magic-core-tool` 依赖，同时在将 IP 数据库文件放在 `src/main/resources/ip` 目录下，数据库文件可以从 [这里](https://github.com/xkcoding/magic-starter/tree/master/magic-core-tool/src/main/resources/ip/ip2region.db) 下载)
- 自定义日志方便在任意位置记录日志
- 错误日志可以记录堆栈信息及错误行号等信息
- 请求/响应日志默认关闭，不影响性能，方便开发时调试
- 提供通用 logback 配置文件