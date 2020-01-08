<h1 align="center"><a href="https://github.com/xkcoding/magic-starter/tree/master/magic-starter-id" target="_blank">magic-starter-id</a></h1>
<p align="center">
<a href="https://travis-ci.com/xkcoding/magic-starter" target="_blank"><img alt="Travis-CI" src="https://travis-ci.com/xkcoding/magic-starter.svg?branch=master"/></a>
  <a href="https://search.maven.org/artifact/com.xkcoding/magic-starter-locker" target="_blank"><img alt="MAVEN" src="https://img.shields.io/maven-central/v/com.xkcoding/magic-starter-id.svg?color=brightgreen&label=Maven%20Central"></a>
  <a href="https://www.codacy.com/manual/xkcoding/magic-starter?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=xkcoding/magic-starter&amp;utm_campaign=Badge_Grade" target="_blank"><img alt="Codacy" src="https://api.codacy.com/project/badge/Grade/6b998c3a533e451690b4164ab1acd164"/></a>
  <a href="https://xkcoding.com" target="_blank"><img alt="author" src="https://img.shields.io/badge/author-Yangkai.Shen-blue.svg"/></a>
  <a href="https://www.oracle.com/technetwork/java/javase/downloads/index.html" target="_blank"><img alt="JDK" src="https://img.shields.io/badge/JDK-1.8.0_162-orange.svg"/></a>
  <a href="https://docs.spring.io/spring-boot/docs/2.1.8.RELEASE/reference/html/" target="_blank"><img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-2.1.8.RELEASE-brightgreen.svg"/></a>
  <a href="https://github.com/xkcoding/magic-starter/blob/master/LICENSE" target="_blank"><img alt="LICENSE" src="https://img.shields.io/github/license/xkcoding/magic-starter.svg"/></a>
</p>

## 简介

`magic-starter-id` 主要是提供了分布式主键生成器的功能，提供 3 种生成方式，分别是雪花算法、数据库、Redis。

## 使用

```xml
<dependency>
  <groupId>com.xkcoding</groupId>
  <artifactId>magic-starter-id</artifactId>
  <version>${magic-starter.version}</version>
</dependency>
```

## 快速上手

> magic-starter-id 提供了 3 种分布式主键生成的策略。
>
> 1. 雪花算法实现
> 2. 基于 MySQL 乐观锁实现
> 3. 基于 Redis 实现
>
> 懒人请直接看demo：https://github.com/xkcoding/magic-starter-id-demo

### 配置

#### 雪花算法

##### 基于配置文件配置

```yaml
magic:
  id:
    snowflake:
      worker-id: 1
      data-center-id: 1
```

##### 基于代码配置

```java
@Bean
public Id snowflakeId() {
    return SnowflakeIdFactory.create()
            .dataCenterId(1L)
            .workerId(1L)
            .prefix(new DefaultPrefix("2019-"))
            .getInstance();
}
```

#### 基于 MySQL 乐观锁实现

##### 添加依赖

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
</dependency>
```

##### 基于配置文件配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/spring-boot-demo?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
magic:
  id:
    database:
      step: 1
      retry-times: 3
      table-name: id_test
      business-name: magic-id
      prefix: 2019-
      step-start: 0
```

##### 基于代码配置

```java
@Bean
public Id databaseId(DataSource dataSource) {
    return DatabaseIdFactory.create()
            .businessName(() -> String.format("test_db_%s", DateUtil.today()))
            .prefix(() -> "2019-")
            .dataSource(dataSource)
            .step(1)
            .stepStart(100000)
            .retryTimes(3)
            .tableName("id_test")
            .getInstance();
}
```

#### 基于 Redis 实现

##### 添加依赖

```xml
<dependency>
  <groupId>redis.clients</groupId>
  <artifactId>jedis</artifactId>
</dependency>
```

##### 基于配置文件配置

```yaml
magic:
  id:
    redis:
      step: 1
      business-name: magic-id
      prefix: 2019-
      step-start: 0
      ip: 127.0.0.1
      port: 6379
      password:
```

##### 基于代码配置

```java
@Bean
public Id redisId() {
    return RedisIdFactory.create()
            .businessName(() -> String.format("test_redis_%s", DateUtil.today()))
            .prefix(() -> "2019-")
            .ip("localhost")
            .port(6379)
            .step(1)
            .stepStart(0)
            .getInstance();
}
```

### 使用

#### 雪花算法

```java
@Autowired
private Id snowflakeId;

log.info("【snowflakeId】= {}", snowflakeId.nextId());
log.info("【snowflakeId】= {}", snowflakeId.nextIdStr());

log.info("【snowflakeId】= {}", snowflakeId.nextId(new DateBusinessName()));
log.info("【snowflakeId】= {}", snowflakeId.nextIdStr(new DateBusinessName(), () -> "自定义"));
```

#### 基于 MySQL 乐观锁实现

```java
@Autowired
private Id databaseId;

log.info("【databaseId】= {}", databaseId.nextId());
log.info("【databaseId】= {}", databaseId.nextIdStr());

log.info("【databaseId】= {}", databaseId.nextId(() -> "自定义模块"));
log.info("【databaseId】= {}", databaseId.nextIdStr(() -> "自定义模块", () -> "自定义前缀-1-"));
```

#### 基于 Redis 实现

```java
@Autowired
private Id redisId;

log.info("【redisId】= {}", redisId.nextId());
log.info("【redisId】= {}", redisId.nextIdStr());

log.info("【redisId】= {}", redisId.nextId(() -> "自定义模块"));
log.info("【redisId】= {}", redisId.nextIdStr(() -> "自定义模块", () -> "自定义前缀-"));
```

## 特点

- 提供了 3 种分布式主键生成策略
- 提供配置文件和代码的方式进行自定义配置
- 对外 API 一致