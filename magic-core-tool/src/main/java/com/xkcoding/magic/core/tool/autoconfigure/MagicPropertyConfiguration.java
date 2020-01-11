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

package com.xkcoding.magic.core.tool.autoconfigure;

import com.xkcoding.magic.core.tool.props.MagicPropertySourcePostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * blade property config
 *
 * @author L.cm
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MagicPropertyConfiguration {

	@Bean
	public MagicPropertySourcePostProcessor magicPropertySourcePostProcessor() {
		return new MagicPropertySourcePostProcessor();
	}

}
