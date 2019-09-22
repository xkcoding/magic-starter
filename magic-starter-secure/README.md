<h1 align="center"><a href="https://github.com/xkcoding/magic-starter/magic-starter-secure" target="_blank">magic-starter-secure</a></h1>
<p align="center">
<a href="https://travis-ci.com/xkcoding/magic-starter" target="_blank"><img alt="Travis-CI" src="https://travis-ci.com/xkcoding/magic-starter.svg?branch=master"/></a>
  <a href="https://search.maven.org/artifact/com.xkcoding/magic-starter-secure" target="_blank"><img alt="MAVEN" src="https://img.shields.io/maven-central/v/com.xkcoding/magic-starter-secure.svg?color=brightgreen&label=Maven%20Central"></a>
  <a href="https://www.codacy.com/manual/xkcoding/magic-starter?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=xkcoding/magic-starter&amp;utm_campaign=Badge_Grade" target="_blank"><img alt="Codacy" src="https://api.codacy.com/project/badge/Grade/6b998c3a533e451690b4164ab1acd164"/></a>
  <a href="https://xkcoding.com" target="_blank"><img alt="author" src="https://img.shields.io/badge/author-Yangkai.Shen-blue.svg"/></a>
  <a href="https://www.oracle.com/technetwork/java/javase/downloads/index.html" target="_blank"><img alt="JDK" src="https://img.shields.io/badge/JDK-1.8.0_162-orange.svg"/></a>
  <a href="https://docs.spring.io/spring-boot/docs/2.1.8.RELEASE/reference/html/" target="_blank"><img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-2.1.8.RELEASE-brightgreen.svg"/></a>
  <a href="https://github.com/xkcoding/magic-starter/blob/master/LICENSE" target="_blank"><img alt="LICENSE" src="https://img.shields.io/github/license/xkcoding/magic-starter.svg"/></a>
</p>

## 简介

`magic-starter-secure` 是一个极简的权限控制框架，使用 AOP + SpEL + 拦截器 实现。

## 使用

```xml
<dependency>
  <groupId>com.xkcoding</groupId>
  <artifactId>magic-starter-secure</artifactId>
  <version>${magic-starter.version}</version>
</dependency>
```

## 快速上手

> magic-starter-secure 有三种设置权限拦截的方案。
>
> 1. 基于注解 + SpEL 的权限配置(`基于AOP方案`)
> 2. 基于配置文件的权限配置(`基于拦截器方案`)
> 3. 基于代码配置的权限配置(`基于拦截器方案`)
>
> 需要注意的是：
>
> 1. 1、2 和 1、3 可以共存，但是 2、3 是互斥的，代码配置会覆盖配置文件的配置。
> 2. 基于AOP方案 SpEL 表达式使用 Spring Bean 的时候，支持自定义参数。
> 3. 基于拦截器方案 SpEL 表达式使用 Spring Bean 的时候，参数固定写`(#request,#response)`。

### 基于注解 + SpEL 的权限配置(AOP)

> 懒人请看：https://github.com/xkcoding/magic-stater-secure-demo/tree/master/annotation-demo

*注意：这种方式通过`AOP`实现，也就是说这种方式的权限控制可以将权限的细粒度控制到最细，比如一个用户在一个接口执行了若干个操作，我们可以在最终的 DAO 层的方法上添加 `@Secure` 注解，更细粒度的控制执行权限。*

- 注解

```java
@Secure
```

- 内置的鉴权函数

```java
hasLogin()
anon()
permitAll()
denyAll()
hasPermission()
hasAnyPermission()
```

- 使用 Spring Bean 自定义鉴权

```java
// 在 Spring 容器中定义一个名为 test 的 Bean
@Slf4j
@Component("test")
public class TestComponent {
    /**
     * 自定义鉴权方法
     *
     * @param permission 权限表达式
     * @return {@code true} / {@code false}
     */
    @SuppressWarnings("unused")
    public boolean hasPermission(String permission) {
        log.info("【permission】= {}", permission);
        return StrUtil.equals(permission, "admin");
    }
}

// 具体鉴权方法上添加 @Secure 注解，通过 @test 去引用
@Secure("@test.hasPermission(#permission)")
```

### 基于配置文件的权限配置(拦截器)

> 懒人请看：https://github.com/xkcoding/magic-stater-secure-demo/tree/master/properties-demo

```yaml
secure:
  jwt:
    algorithm: es256
    secret: magic
    timeout: 1d
  white-list: /login
  rule-list:
    - path: /class
      expression: "anon()"
    - path: /me
      method: GET
      expression: "hasLogin()"
    - path: /spring
      expression: "@test.hasPermission(#request,#response)"
```

### 基于代码配置的权限配置(拦截器)

> 懒人请看：https://github.com/xkcoding/magic-stater-secure-demo/tree/master/configuration-demo

```java
@Configuration
public class SecureConfig {

    @Bean
    public SecureRuleRegistry secureRuleRegistry() {
        return new SecureRuleRegistry()
                .exclude("/login")
                .addRule("/me", HttpMethod.GET,"hasLogin()")
                .addRule("/**", HttpMethod.ANY, "@test.hasPermission(#request,#response)");
    }
}
```

### 自定义权限控制表达式处理

> 懒人请看：https://github.com/xkcoding/magic-starter-secure-demo/tree/master/custom-secure-handler

```java
/**
 * <p>
 * 自定义权限控制表达式处理
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/21 10:20
 */
@Slf4j
public class CustomSecureExpressionHandler implements SecureExpressionHandler {
    public boolean testSecure(String key) {
        int randomInt = RandomUtil.randomInt(1, 10);
        log.info("【randomInt】= {}", randomInt);

        boolean ret = randomInt > 5;
        log.info("【key】= {}，{} 权限", key, ret ? "有" : "无");
        return ret;
    }
}

@Configuration
public class SecureConfig {
    @Bean
    public SecureExpressionHandler secureExpressionHandler(){
        return new CustomSecureExpressionHandler();
    }
}
```

## 特点

- 基于Jwt
- 支持 SpEL 表达式，基于AOP的方案，支持解析自定义参数，基于拦截器方案，内置 request 及 response 参数
- 暴露表达式处理端点，支持自定义表达式处理，同时支持自定义 Spring Bean 处理
- 不关心登录逻辑，只关心登录后的权限控制

