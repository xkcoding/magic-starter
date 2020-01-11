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

package com.xkcoding.magic.core.tool.props;

import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * 自定义资源文件读取，优先级最低
 *
 * @author L.cm
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MagicPropertySource {

	/**
	 * Indicate the resource location(s) of the properties file to be loaded.
	 * for example, {@code "classpath:/com/example/app.yml"}
	 *
	 * @return location(s)
	 */
	String value();

	/**
	 * load app-{activeProfile}.yml
	 *
	 * @return {boolean}
	 */
	boolean loadActiveProfile() default true;

	/**
	 * Get the order value of this resource.
	 *
	 * @return order
	 */
	int order() default Ordered.LOWEST_PRECEDENCE;

}
