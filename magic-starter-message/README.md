<h1 align="center"><a href="https://github.com/xkcoding/magic-starter/tree/master/magic-starter-message" target="_blank">magic-starter-message</a></h1>
<p align="center">
<a href="https://travis-ci.com/xkcoding/magic-starter" target="_blank"><img alt="Travis-CI" src="https://travis-ci.com/xkcoding/magic-starter.svg?branch=master"/></a>
  <a href="https://search.maven.org/artifact/com.xkcoding/magic-starter-message" target="_blank"><img alt="MAVEN" src="https://img.shields.io/maven-central/v/com.xkcoding/magic-starter-message.svg?color=brightgreen&label=Maven%20Central"></a>
  <a href="https://www.codacy.com/manual/xkcoding/magic-starter?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=xkcoding/magic-starter&amp;utm_campaign=Badge_Grade" target="_blank"><img alt="Codacy" src="https://api.codacy.com/project/badge/Grade/6b998c3a533e451690b4164ab1acd164"/></a>
  <a href="https://xkcoding.com" target="_blank"><img alt="author" src="https://img.shields.io/badge/author-Yangkai.Shen-blue.svg"/></a>
  <a href="https://www.oracle.com/technetwork/java/javase/downloads/index.html" target="_blank"><img alt="JDK" src="https://img.shields.io/badge/JDK-1.8.0_162-orange.svg"/></a>
  <a href="https://docs.spring.io/spring-boot/docs/2.1.8.RELEASE/reference/html/" target="_blank"><img alt="Spring Boot" src="https://img.shields.io/badge/Spring Boot-2.1.8.RELEASE-brightgreen.svg"/></a>
  <a href="https://github.com/xkcoding/magic-starter/blob/master/LICENSE" target="_blank"><img alt="LICENSE" src="https://img.shields.io/github/license/xkcoding/magic-starter.svg"/></a>
</p>

## 简介

`magic-starter-message` 主要是对一些常用的发送消息通知的封装，包括钉钉机器人、邮件、短信、微信(`暂不支持`)等。

## 使用

```xml
<dependency>
  <groupId>com.xkcoding</groupId>
  <artifactId>magic-starter-message</artifactId>
  <version>${magic-starter.version}</version>
</dependency>
```

## 快速上手

> 懒人建议直接查看 demo：https://github.com/xkcoding/magic-starter-message-demo

### 邮件发送

- 引入依赖

邮件发送需要额外引入邮件依赖，如果需要使用模板邮件，则还需要引入 `thymeleaf` 依赖

```xml
<!-- Spring Boot 邮件依赖 -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-mail</artifactId>
</dependency>

<!-- Spring Boot 模板依赖 -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

- 基础配置

```yaml
spring:
  mail:
    host: smtp.mxhichina.com
    port: 465
    username: spring-boot-demo@xkcoding.com
    password: Just4Test!
    protocol: smtp
    test-connection: true
    default-encoding: UTF-8
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true
      mail.smtp.starttls.required: true
      mail.smtp.ssl.enable: true
      mail.display.sendmail: spring-boot-demo
magic:
  message:
    email:
      enabled: true
      # 邮件模板位置：默认为 classpath:/email/
      prefix: classpath:/templates/email
      # 邮件模板后缀：默认为 .html
      suffix: .html
```

- 简单邮件

```java
/**
 * 测试简单邮件
 */
@Test
public void testSimpleEmail() {
    EmailMessage simple = EmailMessage.simpleEmail();
    simple.setFrom("xkcoding");
    simple.setSubject("主题：测试简单邮件");
    simple.setContent("内容：测试简单邮件");
    simple.setTos(Collections.singletonList("237497819@qq.com"));
    emailMessageSender.send(simple);
}
```

- 复杂邮件

```java
/**
 * 测试复杂邮件
 */
@Test
public void testMimeEmail() throws URISyntaxException {
    EmailMessage mime = EmailMessage.mimeEmail();
    mime.setFrom("xkcoding");
    mime.setSubject("主题：测试复杂邮件");
    // 设置模板
    mime.setTemplate("test");
    // 设置参数
    Map<String, Object> params = new HashMap<>();
    params.put("project", "Spring Boot Demo");
    params.put("author", "Yangkai.Shen");
    params.put("url", "https://github.com/xkcoding/spring-boot-demo");
    mime.setParams(params);

    Resource resource = ResourceUtil.getResourceObj("static/xkcoding.png");
    // 设置附件
    EmailAttachment attachment = new EmailAttachment(resource.getName(), new File(resource.getUrl().toURI()));
    mime.setAttachments(Collections.singletonList(attachment));

    // 设置静态资源
    EmailStaticResource staticResource = new EmailStaticResource("xkcoding", new File(resource.getUrl().toURI()));
    mime.setStaticResources(Collections.singletonList(staticResource));

    mime.setTos(Collections.singletonList("237497819@qq.com"));
    emailMessageSender.send(mime);
}
```

### 短信发送

- 引入依赖

短信发送使用的是阿里大鱼的 API，因此需要额外引入相关依赖

```xml
<!-- 阿里云核心包 -->
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>${aliyun.core.version}</version>
  <scope>provided</scope>
</dependency>

<!-- 阿里大鱼 -->
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-dysmsapi</artifactId>
  <version>${aliyun.dysmsapi.version}</version>
  <scope>provided</scope>
</dependency>
```

- 基础配置

```yaml
magic:
  message:
    sms:
      enabled: true
      access-key: 填写自己的 key
      secret-key: 填写自己的 secret
      channels:
        填写自己的模板名称: 填写自己的模板CODE
```

- 短信发送

```java
/**
 * 测试短信
 */
@Test
public void testSms() {
    SmsMessage sms = new SmsMessage();
    sms.setMobile("17326075631");

    // 短信模板：尊敬的读者，代码日记发布文章《${article}》，欢迎阅读！
    Map<String, String> map = new HashMap<>();
    map.put("article", "测试短信发送");
    sms.setParams(map);
    sms.setOutId(sms.getMobile());

    sms.setSignName("代码日记");
    sms.setTemplateCode("article_notification");
    smsMessageSender.send(sms);
}
```

### 钉钉机器人发送

- 基础配置

```yaml
magic:
  message:
    dingtalk:
      enabled: true
      webhook: 填写自己的钉钉机器人的 webhook 地址
```

- text 消息

```java
/**
 * 测试 text 消息
 */
@Test
public void testText() {
    TextDingTalkMessage textMsg = new TextDingTalkMessage();
    textMsg.setText(new Text("测试钉钉文本消息"));
    // 测试 @all
    textMsg.setAt(new Attention(Collections.singletonList("17326075631"), true));
    dingTalkMessageSender.send(textMsg);
}
```

- link 消息

```java
/**
 * 测试 link 消息
 */
@Test
public void testLink() {
    LinkDingTalkMessage linkMsg = new LinkDingTalkMessage();
    Link link = new Link();
    link.setTitle("测试钉钉超链接消息：spring-boot-demo");
    link.setText("spring boot demo 是一个用来深度学习并实战 spring boot 的项目，目前总共包含 63 个集成demo，已经完成 52 个。");
    link.setMessageUrl("https://github.com/xkcoding/spring-boot-demo");
    link.setPicUrl("https://camo.githubusercontent.com/14758c0377ca916afc6f8624f6299bcdd68667a8/68747470733a2f2f7374617263686172742e63632f786b636f64696e672f737072696e672d626f6f742d64656d6f2e737667");
    linkMsg.setLink(link);
    dingTalkMessageSender.send(linkMsg);
}
```

- markdown 消息

> 经测试：markdown 消息@功能存在问题，等官方修复~

```java
/**
 * 测试 markdown 消息
 */
@Test
public void testMarkdown() {
    MarkdownDingTalkMessage markdownMsg = new MarkdownDingTalkMessage();
    Markdown markdown = new Markdown();
    markdown.setTitle("测试钉钉markdown消息");
    markdown.setText("# 测试钉钉markdown消息：magic-starter-message\n" + "\n" + "## 简介\n" + "\n" + "`magic-starter-message` 主要是对一些常用的发送消息通知的封装，包括钉钉机器人、各类邮件、阿里大鱼、微信等。\n" + "\n" + "## 模块地址\n" + "\n" + "[magic-starter-message](https://github.com/xkcoding/magic-starter/magic-starter-message)");
    markdownMsg.setMarkdown(markdown);
    // 测试 @17326075631
    markdownMsg.setAt(new Attention(Collections.singletonList("17326075631"), false));
    dingTalkMessageSender.send(markdownMsg);
}
```

- 整体跳转 ActionCard 类型

```java
/**
 * 测试 整体跳转ActionCard 消息
 */
@Test
public void testActionCardWhole() {
    ActionCardWholeDingTalkMessage wholeMsg = new ActionCardWholeDingTalkMessage();
    ActionCardWhole actionCardWhole = new ActionCardWhole();

    actionCardWhole.setTitle("测试整体跳转消息");
    actionCardWhole.setText("# 测试整体跳转消息：GitHub Actions 初体验\n" + "\n" + "## 前言\n" + "\n" + "前段时间同性交友网站 `GitHub` 推出了自家的 `Registry` 服务，支持多种语言的镜像服务，具体参见：https://help.github.com/en/articles/about-github-package-registry\n" + "\n" + "随后，又推出了自家的自动化构建服务：`Github Actions`，具体参见：https://help.github.com/en/articles/about-github-actions\n" + "\n" + "这俩服务我都在第一时间申请使用了，也都已经审核通过了，但是由于 `Registry` 测试有些不稳定，还存在一些问题，所以，并未发布体验文章，不过 关于`GitHub Actions` 我已经成功体验了一把。\n");
    actionCardWhole.setHideAvatar("0");
    actionCardWhole.setBtnOrientation("0");
    actionCardWhole.setSingleTitle("点我学习");
    actionCardWhole.setSingleURL("https://xkcoding.com/2019/08/29/early-experience-about-github-actions.html");

    wholeMsg.setActionCard(actionCardWhole);
    dingTalkMessageSender.send(wholeMsg);
}
```

- 独立跳转 ActionCard 类型

```java
/**
 * 测试 独立跳转ActionCard 消息
 */
@Test
public void testActionCardSingle() {
    ActionCardSingleDingTalkMessage singleMsg = new ActionCardSingleDingTalkMessage();
    ActionCardSingle actionCardSingle = new ActionCardSingle();
    actionCardSingle.setTitle("测试独立跳转消息");
    actionCardSingle.setText("# 测试独立跳转消息：重要提示\n" + "\n" + "> 本群已与多加搜索引擎合作，遇到问题先去合作平台咨询！");
    actionCardSingle.setBtnOrientation("0");
    actionCardSingle.setHideAvatar("0");

    List<Button> buttons = new ArrayList<>();
    buttons.add(new Button("百度咨询一下", "https://www.baidu.com/"));
    buttons.add(new Button("谷歌翻墙一下", "https://www.google.com/"));
    buttons.add(new Button("是在不行，你就 360 一下", "https://www.so.com/?src=360portal&ls="));

    actionCardSingle.setBtns(buttons);
    singleMsg.setActionCard(actionCardSingle);
    dingTalkMessageSender.send(singleMsg);
}
```

- FeedCard 类型

```java
/**
 * 测试 FeedCard 消息
 */
@Test
public void testFeedCard() {
    FeedCardDingTalkMessage feedMsg = new FeedCardDingTalkMessage();
    FeedCard feedCard = new FeedCard();
    List<FeedCardLink> feedCardLinks = new ArrayList<>();
    feedCardLinks.add(new FeedCardLink("设计模式之行为型设计模式 - 策略模式", "https://xkcoding.com/2019/09/03/design-pattern-strategy.html", "https://static.xkcoding.com/blog/2019-08-28-strategy-uml.png"));
    feedCardLinks.add(new FeedCardLink("设计模式之结构型设计模式 - 适配器模式", "https://xkcoding.com/2019/09/13/design-pattern-adapter.html", "https://static.xkcoding.com/blog/2019-09-29-design-pattern-adapter-uml.png"));
    feedCardLinks.add(new FeedCardLink("设计模式之行为型设计模式 - 委派模式", "https://xkcoding.com/2019/08/27/design-pattern-delegate.html", "https://static.xkcoding.com/blog/2019-08-28-delegate-uml.png"));
    feedCardLinks.add(new FeedCardLink("设计模式之结构型设计模式 - 代理模式", "https://xkcoding.com/2019/08/20/design-pattern-proxy.html", "https://static.xkcoding.com/blog/2019-08-21-proxy-dynamicproxy-cglib-uml.png"));
    feedCard.setLinks(feedCardLinks);
    feedMsg.setFeedCard(feedCard);
    dingTalkMessageSender.send(feedMsg);
}
```

## 特点

- 邮件发送支持简单邮件和复杂邮件，复杂邮件支持模板(需引入 `thymeleaf` 依赖)，支持附件及静态资源，支持抄送，支持多收件人
- 短信发送支持多手机号，上限为 1000 个手机号
- 钉钉机器人支持多种类型的消息，text 类型、link 类型、markdown 类型、整体跳转 ActionCard 类型、独立跳转 ActionCard 类型、FeedCard 类型
- 消息发送 API 统一，可以继承对应发送器，覆盖原有逻辑，实现自定义逻辑