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

package com.xkcoding.magic.message.autoconfigure;

import com.xkcoding.magic.core.tool.util.StrUtil;
import com.xkcoding.magic.message.constants.MessageConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * <p>
 * 邮件模板 Thymeleaf 自动装配
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/11 14:11
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnClass({TemplateEngine.class, SpringResourceTemplateResolver.class})
public class EmailMessageTemplateAutoConfiguration {
	private final MessageProperties messageProperties;

	@Bean(MessageConstants.EMAIL_TEMPLATE_ENGINE_BEAN)
	public TemplateEngine templateEngine(@Qualifier(MessageConstants.EMAIL_TEMPLATE_RESOLVER_BEAN) SpringResourceTemplateResolver templateResolver) {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.addDialect(new Java8TimeDialect());
		engine.addTemplateResolver(templateResolver);
		engine.getConfiguration();

		return engine;
	}

	@Bean(MessageConstants.EMAIL_TEMPLATE_RESOLVER_BEAN)
	public SpringResourceTemplateResolver templateResolver(ApplicationContext context) {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(context);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCacheable(false);
		String prefix = messageProperties.getEmail().getPrefix();
		templateResolver.setPrefix(StrUtil.appendIfMissing(prefix, StrUtil.SLASH));
		templateResolver.setSuffix(messageProperties.getEmail().getSuffix());
		return templateResolver;
	}
}
