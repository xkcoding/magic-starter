<h1 align="center"><a href="https://github.com/xkcoding/magic-starter/tree/master/magic-starter-oss" target="_blank">magic-starter-oss</a></h1>
<p align="center">
<a href="https://travis-ci.com/xkcoding/magic-starter" target="_blank"><img alt="Travis-CI" src="https://travis-ci.com/xkcoding/magic-starter.svg?branch=master"/></a>
  <a href="https://search.maven.org/artifact/com.xkcoding/magic-starter-oss" target="_blank"><img alt="MAVEN" src="https://img.shields.io/maven-central/v/com.xkcoding/magic-starter-oss.svg?color=brightgreen&label=Maven%20Central"></a>
  <a href="https://www.codacy.com/manual/xkcoding/magic-starter?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=xkcoding/magic-starter&amp;utm_campaign=Badge_Grade" target="_blank"><img alt="Codacy" src="https://api.codacy.com/project/badge/Grade/6b998c3a533e451690b4164ab1acd164"/></a>
  <a href="https://xkcoding.com" target="_blank"><img alt="author" src="https://img.shields.io/badge/author-Yangkai.Shen-blue.svg"/></a>
  <a href="https://www.oracle.com/technetwork/java/javase/downloads/index.html" target="_blank"><img alt="JDK" src="https://img.shields.io/badge/JDK-1.8.0_162-orange.svg"/></a>
  <a href="https://docs.spring.io/spring-boot/docs/2.1.8.RELEASE/reference/html/" target="_blank"><img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-2.1.8.RELEASE-brightgreen.svg"/></a>
  <a href="https://github.com/xkcoding/magic-starter/blob/master/LICENSE" target="_blank"><img alt="LICENSE" src="https://img.shields.io/github/license/xkcoding/magic-starter.svg"/></a>
</p>

## 简介

`magic-starter-oss` 主要是对一些常用的对象存储的封装，支持`阿里云OSS、腾讯云COS、七牛云存储、MinIO`、`AwsS3`。

## 使用

```xml
<dependency>
  <groupId>com.xkcoding</groupId>
  <artifactId>magic-starter-oss</artifactId>
  <version>${magic-starter.version}</version>
</dependency>
```

## 快速上手

> magic-starter-oss 提供了 5 种常见的对象云存储的支持。
>
> 1. 阿里云 OSS
> 2. 腾讯云 COS
> 3. 七牛云存储
> 4. MinIO 自建
> 5. AwsS3 服务
>
> 懒人请直接看demo：https://github.com/xkcoding/magic-starter-oss-demo

### 配置

#### 阿里云 OSS

##### 引入依赖

```xml
<dependency>
  <groupId>com.aliyun.oss</groupId>
  <artifactId>aliyun-sdk-oss</artifactId>
  <version>${aliyun.oss.version}</version>
</dependency>
```

##### 配置文件

```yaml
magic:
  oss:
    ali-oss:
      enabled: true
      access-key: LTA**************WHXtC
      secret-key: PQw*************************r3
      endpoint: oss-cn-hangzhou.aliyuncs.com
      bucket-name: test
      https: true
```

#### 腾讯云 COS

##### 引入依赖

```xml
<dependency>
  <groupId>com.qcloud</groupId>
  <artifactId>cos_api</artifactId>
  <version>${qcloud.oss.version}</version>
</dependency>
```

##### 配置文件

```yaml
magic:
  oss:
    tencent-cos:
      enabled: true
      app-id: 125****51
      access-key: AK****************************at1pg
      secret-key: npJ****************************nqz
      bucket-name: test
      region: ap-shanghai
      https: true
```

#### 七牛云

##### 引入依赖

```xml
<dependency>
  <groupId>com.qiniu</groupId>
  <artifactId>qiniu-java-sdk</artifactId>
  <version>${qiniu.oss.version}</version>
</dependency>
```

##### 配置文件

```yaml
magic:
  oss:
    qiniu-cloud:
      enabled: true
      access-key: 9Qx*****************9jtENhZ-sTGV**********f5Rd
      secret-key: 8izWd*****************************Ccgd
      bucket-name: test
      endpoint: http://q3**********cho.bkt.clouddn.com
      region: z0
```

#### MinIO

##### 引入依赖

```xml
<dependency>
  <groupId>io.minio</groupId>
  <artifactId>minio</artifactId>
  <version>${minio.oss.version}</version>
</dependency>
```

##### 配置文件

```yaml
magic:
  oss:
    min-io:
      enabled: true
      access-key: minioadmin
      secret-key: minioadmin
      bucket-name: test
      endpoint: http://192.168.31.8:9000
```

#### AwsS3

##### 引入依赖

```xml
<dependency>
  <groupId>com.amazonaws</groupId>
  <artifactId>aws-java-sdk-s3</artifactId>
  <version>${awss3.oss.version}</version>
</dependency>
```

##### 配置文件

```yaml
magic:
  oss:
    aws-s3:
      enabled: true
      access-key: accesskey
      secret-key: secretkey
      bucket-name: tmp
      endpoint: https://***.amazonaws.com
      https: true
      region: xxx-west-1 # 自建服务,可不填
```

### 使用

#### 阿里云 OSS

```java
@Autowired
private AliOssTemplate aliOssTemplate;
```

#### 腾讯云 COS

```java
@Autowired
private TencentCosTemplate tencentCosTemplate;
```

#### 七牛云

```java
@Autowired
private QiNiuCloudTemplate qiNiuCloudTemplate;
```

#### MinIO

```java
@Autowired
private MinIoTemplate minIoTemplate;
```

#### AwsS3

```java
@Autowired
private AwsS3Template awsS3Template;
```

## 特点

- 提供了存储桶生成策略、及文件名生成策略，用户自定义只需要自定义 `com.xkcoding.magic.oss.support.rule.OssRule`，同时加入到 Spring 容器中即可
- 提供了统一的操作接口，`com.xkcoding.magic.oss.OssTemplate` 后续集成其他对象存储，只需要实现该接口即可
- 虽然有 5 种实现，但是对外暴露的 API 一致，减去记忆的烦恼