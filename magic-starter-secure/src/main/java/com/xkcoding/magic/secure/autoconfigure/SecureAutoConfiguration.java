/*
 * Copyright (c) 2019-2029, xkcoding & Yangkai.Shen & 沈扬凯 (237497819@qq.com & xkcoding.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xkcoding.magic.secure.autoconfigure;

import com.xkcoding.magic.secure.interceptor.SecureInterceptor;
import com.xkcoding.magic.secure.model.Rule;
import com.xkcoding.magic.secure.support.SecureExpressionHandler;
import com.xkcoding.magic.secure.support.SecureUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p>
 * 自动装配类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/19 23:59
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SecureAutoConfiguration implements WebMvcConfigurer {
	private final SecureUserArgumentResolver secureUserArgumentResolver;
	private final SecureExpressionHandler secureExpressionHandler;
	private final List<Rule> ruleList;
	private final List<String> whiteList;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(secureUserArgumentResolver);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SecureInterceptor(ruleList, secureExpressionHandler)).excludePathPatterns(whiteList);
	}
}
