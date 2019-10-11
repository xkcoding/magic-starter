<h1 align="center"><a href="https://github.com/xkcoding/magic-starter/tree/master/magic-core-auto" target="_blank">magic-core-auto</a></h1>
<p align="center">
<a href="https://travis-ci.com/xkcoding/magic-starter" target="_blank"><img alt="Travis-CI" src="https://travis-ci.com/xkcoding/magic-starter.svg?branch=master"/></a>
  <a href="https://search.maven.org/artifact/com.xkcoding/magic-core-auto" target="_blank"><img alt="MAVEN" src="https://img.shields.io/maven-central/v/com.xkcoding/magic-core-auto.svg?color=brightgreen&label=Maven%20Central"></a>
  <a href="https://www.codacy.com/manual/xkcoding/magic-starter?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=xkcoding/magic-starter&amp;utm_campaign=Badge_Grade" target="_blank"><img alt="Codacy" src="https://api.codacy.com/project/badge/Grade/6b998c3a533e451690b4164ab1acd164"/></a>
  <a href="https://xkcoding.com" target="_blank"><img alt="author" src="https://img.shields.io/badge/author-Yangkai.Shen-blue.svg"/></a>
  <a href="https://www.oracle.com/technetwork/java/javase/downloads/index.html" target="_blank"><img alt="JDK" src="https://img.shields.io/badge/JDK-1.8.0_162-orange.svg"/></a>
  <a href="https://docs.spring.io/spring-boot/docs/2.1.8.RELEASE/reference/html/" target="_blank"><img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-2.1.8.RELEASE-brightgreen.svg"/></a>
  <a href="https://github.com/xkcoding/magic-starter/blob/master/LICENSE" target="_blank"><img alt="LICENSE" src="https://img.shields.io/github/license/xkcoding/magic-starter.svg"/></a>
</p>

## 简介

`magic-core-auto` 借鉴了 [春哥](https://github.com/ChunMengLu) 的 [mica-auto](https://github.com/lets-mica/mica-auto)，是 `magic-starter` 的一个基础组件，通过扫描 `@Component`、`@Configuration` 等注解，用自动生成 `Spring Boot Starter` 的一些基础配置，包括 `spring.factories`、`spring-autoconfigure-metadata.properties`、`spring-configuration-metadata.json`、`spring-devtools.properties` 等文件的自动生成。

## 使用

> 注意：如果你项目中使用了 `Lombok` 请将 `magic-auto` 的依赖放置到 `Lombok` 后面。

```xml
<dependency>
  <groupId>com.xkcoding</groupId>
  <artifactId>magic-core-auto</artifactId>
  <version>${magic-starter.version}</version>
  <scope>provided</scope>
</dependency>
```

## 参考

- mica-atuo：https://github.com/lets-mica/mica-auto
- Google Auto: https://github.com/google/auto
- Spring 5 - spring-context-indexer：https://github.com/spring-projects/spring-framework/tree/master/spring-context-indexer

