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

import com.xkcoding.magic.message.constants.MessageConstants;
import com.xkcoding.magic.message.support.dingtalk.DingTalkMessageSender;
import com.xkcoding.magic.message.support.email.EmailMessageSender;
import com.xkcoding.magic.message.support.sms.SmsMessageSender;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

/**
 * <p>
 * 消息自动装配类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/11 14:14
 */
@Configuration
@EnableConfigurationProperties({MessageProperties.class})
public class MessageAutoConfiguration {
	@Bean(MessageConstants.BEAN_NAME_SMS)
	@ConditionalOnMissingBean(name = MessageConstants.BEAN_NAME_SMS)
	@ConditionalOnProperty(value = "magic.message.sms.enabled", havingValue = "true")
	public SmsMessageSender smsMessageSender(MessageProperties properties) {
		return new SmsMessageSender(properties);
	}

	@Bean(MessageConstants.BEAN_NAME_EMAIL)
	@ConditionalOnMissingBean(name = MessageConstants.BEAN_NAME_EMAIL)
	@ConditionalOnProperty(value = "magic.message.email.enabled", havingValue = "true")
	public EmailMessageSender emailMessageSender(JavaMailSender mailSender, @Qualifier(MessageConstants.EMAIL_TEMPLATE_ENGINE_BEAN) TemplateEngine templateEngine) {
		return new EmailMessageSender(mailSender, templateEngine);
	}

	@Bean(MessageConstants.BEAN_NAME_DINGTALK)
	@ConditionalOnMissingBean(name = MessageConstants.BEAN_NAME_DINGTALK)
	@ConditionalOnProperty(value = "magic.message.dingtalk.enabled", havingValue = "true")
	public DingTalkMessageSender dingTalkMessageSender(MessageProperties properties) {
		return new DingTalkMessageSender(properties);
	}
}
