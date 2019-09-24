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

package com.xkcoding.magic.log.autoconfigure;

import com.xkcoding.magic.log.support.LogHandler;
import com.xkcoding.magic.log.support.OperatorService;
import com.xkcoding.magic.log.support.listener.LogEventListener;
import com.xkcoding.magic.log.support.logger.MagicLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 自动装配类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/24 14:06
 */
@Configuration
@EnableConfigurationProperties({LogProperties.class})
@ConditionalOnProperty(value = "magic.log.enabled", havingValue = "true", matchIfMissing = true)
public class LogAutoConfiguration {
	@Bean
	@ConditionalOnMissingBean
	public LogHandler logService() {
		return new LogHandler() {
		};
	}

	@Bean
	@ConditionalOnMissingBean
	public OperatorService operatorService() {
		return new OperatorService() {
		};
	}

	@Bean
	public MagicLogger magicLogger() {
		return new MagicLogger();
	}

	@Bean
	public LogEventListener logEventListener(LogHandler logHandler, OperatorService operatorService) {
		return new LogEventListener(logHandler, operatorService);
	}
}
