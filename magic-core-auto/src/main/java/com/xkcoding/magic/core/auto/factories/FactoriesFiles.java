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
package com.xkcoding.magic.core.auto.factories;

import com.xkcoding.magic.core.auto.common.MultiSetMap;
import lombok.experimental.UtilityClass;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.StringJoiner;

/**
 * spring boot 自动化配置工具类
 *
 * @author L.cm
 */
@UtilityClass
class FactoriesFiles {
	private static final Charset UTF_8 = StandardCharsets.UTF_8;

	/**
	 * 写出 spring.factories 文件
	 *
	 * @param factories factories 信息
	 * @param output    输出流
	 * @throws IOException 异常信息
	 */
	static void writeFactoriesFile(MultiSetMap<String, String> factories, OutputStream output) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, UTF_8));
		Set<String> keySet = factories.keySet();
		for (String key : keySet) {
			Set<String> values = factories.get(key);
			if (values == null || values.isEmpty()) {
				continue;
			}
			writer.write(key);
			writer.write("=\\\n  ");
			StringJoiner joiner = new StringJoiner(",\\\n  ");
			for (String value : values) {
				joiner.add(value);
			}
			writer.write(joiner.toString());
			writer.newLine();
		}
		writer.flush();
		output.close();
	}

	/**
	 * 写出 spring-devtools.properties
	 *
	 * @param projectName 项目名
	 * @param output      输出流
	 * @throws IOException 异常信息
	 */
	static void writeDevToolsFile(String projectName, OutputStream output) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, UTF_8));
		// restart.include.blade-cloud=/blade-cloud[\\w-]+\.jar
		String format = "restart.include.%s=/%s[\\\\w-]+\\.jar";
		writer.write(String.format(format, projectName, projectName));
		writer.flush();
		output.close();
	}
}
