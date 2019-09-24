/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */
package com.xkcoding.magic.log.aspect;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xkcoding.magic.core.tool.constants.MagicConsts;
import com.xkcoding.magic.core.tool.util.ClassUtil;
import com.xkcoding.magic.core.tool.util.StrUtil;
import com.xkcoding.magic.core.tool.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Spring boot 控制器 请求日志，方便代码调试
 *
 * @author L.cm
 */
@Slf4j
@Aspect
@Component
@Order(MagicConsts.AOP_ORDER_REQUEST_LOG)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(value = "magic.log.request.enabled", havingValue = "true", matchIfMissing = true)
public class RequestLogAspect {

	private final ObjectMapper objectMapper;

	/**
	 * AOP 环切 控制器 R 返回值
	 *
	 * @param point JoinPoint
	 * @return Object
	 * @throws Throwable 异常
	 */
	@Around("execution(!static com.xkcoding.magic.core.tool.api.R *(..)) && " + "(@within(org.springframework.stereotype.Controller) || " + "@within(org.springframework.web.bind.annotation.RestController))")
	public Object aroundRequest(ProceedingJoinPoint point) throws Throwable {
		MethodSignature ms = (MethodSignature) point.getSignature();
		Method method = ms.getMethod();
		Object[] args = point.getArgs();
		// 请求参数处理
		final Map<String, Object> paraMap = new HashMap<>(16);
		for (int i = 0; i < args.length; i++) {
			// 读取方法参数
			MethodParameter methodParam = ClassUtil.getMethodParameter(method, i);
			// PathVariable 参数跳过
			PathVariable pathVariable = methodParam.getParameterAnnotation(PathVariable.class);
			if (pathVariable != null) {
				continue;
			}
			RequestBody requestBody = methodParam.getParameterAnnotation(RequestBody.class);
			String parameterName = methodParam.getParameterName();
			Object value = args[i];
			// 如果是body的json则是对象
			if (requestBody != null) {
				if (value == null) {
					paraMap.put(parameterName, null);
				} else if (ClassUtil.isPrimitiveOrWrapper(value.getClass())) {
					paraMap.put(parameterName, value);
				} else {
					paraMap.putAll(BeanUtil.beanToMap(value));
				}
				continue;
			}
			// 处理 参数
			if (value instanceof HttpServletRequest) {
				paraMap.putAll(((HttpServletRequest) value).getParameterMap());
				continue;
			} else if (value instanceof WebRequest) {
				paraMap.putAll(((WebRequest) value).getParameterMap());
				continue;
			} else if (value instanceof HttpServletResponse) {
				continue;
			} else if (value instanceof MultipartFile) {
				MultipartFile multipartFile = (MultipartFile) value;
				String name = multipartFile.getName();
				String fileName = multipartFile.getOriginalFilename();
				paraMap.put(name, fileName);
				continue;
			}
			// 参数名
			RequestParam requestParam = methodParam.getParameterAnnotation(RequestParam.class);
			String paraName = parameterName;
			if (requestParam != null && StrUtil.isNotBlank(requestParam.value())) {
				paraName = requestParam.value();
			}
			if (value == null) {
				paraMap.put(paraName, null);
			} else if (ClassUtil.isPrimitiveOrWrapper(value.getClass())) {
				paraMap.put(paraName, value);
			} else if (value instanceof InputStream) {
				paraMap.put(paraName, "InputStream");
			} else if (value instanceof InputStreamSource) {
				paraMap.put(paraName, "InputStreamSource");
			} else if (canJsonSerialize(value)) {
				// 判断模型能被 json 序列化，则添加
				paraMap.put(paraName, value);
			} else {
				paraMap.put(paraName, "【注意】不能序列化为json");
			}
		}
		HttpServletRequest request = WebUtil.getRequest();
		String requestUri = Objects.requireNonNull(request).getRequestURI();
		String requestMethod = request.getMethod();

		// 构建成一条长 日志，避免并发下日志错乱
		StringBuilder beforeReqLog = new StringBuilder(300);
		// 日志参数
		List<Object> beforeReqArgs = new ArrayList<>();
		beforeReqLog.append("\n\n================  Request Start  ================\n");
		// 打印路由
		beforeReqLog.append("===> {}: {}");
		beforeReqArgs.add(requestMethod);
		beforeReqArgs.add(requestUri);
		// 请求参数
		if (paraMap.isEmpty()) {
			beforeReqLog.append("\n");
		} else {
			beforeReqLog.append(" Parameters: {}\n");
			beforeReqArgs.add(JSONUtil.toJsonStr(paraMap));
		}
		// 打印请求头
		Enumeration<String> headers = request.getHeaderNames();
		while (headers.hasMoreElements()) {
			String headerName = headers.nextElement();
			String headerValue = request.getHeader(headerName);
			beforeReqLog.append("===Headers===  {} : {}\n");
			beforeReqArgs.add(headerName);
			beforeReqArgs.add(headerValue);
		}
		beforeReqLog.append("================  Request End   ================\n");
		// 打印执行时间
		long startNs = System.nanoTime();
		log.info(beforeReqLog.toString(), beforeReqArgs.toArray());
		// aop 执行后的日志
		StringBuilder afterReqLog = new StringBuilder(200);
		// 日志参数
		List<Object> afterReqArgs = new ArrayList<>();
		afterReqLog.append("\n\n================  Response Start  ================\n");
		try {
			Object result = point.proceed();
			// 打印返回结构体
			afterReqLog.append("===Result===  {}\n");
			afterReqArgs.add(JSONUtil.toJsonStr(result));
			return result;
		} finally {
			long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
			afterReqLog.append("<=== {}: {} ({} ms)\n");
			afterReqArgs.add(requestMethod);
			afterReqArgs.add(requestUri);
			afterReqArgs.add(tookMs);
			afterReqLog.append("================  Response End   ================\n");
			log.info(afterReqLog.toString(), afterReqArgs.toArray());
		}
	}

	private boolean canJsonSerialize(Object value) {
		try {
			objectMapper.writeValueAsBytes(value);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

}
