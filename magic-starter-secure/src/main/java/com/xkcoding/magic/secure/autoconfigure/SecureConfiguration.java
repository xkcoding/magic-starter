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

import com.xkcoding.magic.secure.model.Rule;
import com.xkcoding.magic.secure.support.SecureExpressionHandler;
import com.xkcoding.magic.secure.support.SecureRuleRegistry;
import com.xkcoding.magic.secure.support.SecureUserArgumentResolver;
import com.xkcoding.magic.secure.util.JwtUtil;
import com.xkcoding.magic.secure.util.SecureUtil;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p>
 * 配置类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/18 18:15
 */
@Configuration
@AutoConfigureBefore({SecureAutoConfiguration.class})
@EnableConfigurationProperties({SecureProperties.class})
public class SecureConfiguration implements WebMvcConfigurer {
	@Bean
	public JwtUtil jwtUtil(SecureProperties properties) {
		return new JwtUtil(properties);
	}

	@Bean
	public SecureUtil secureUtil(JwtUtil jwtUtil) {
		return new SecureUtil(jwtUtil);
	}

	@Bean
	public SecureExpressionHandler secureExpressionHandler(SecureUtil secureUtil) {
		return new SecureExpressionHandler(secureUtil);
	}

	@Bean
	public SecureUserArgumentResolver secureUserArgumentResolver(SecureUtil secureUtil) {
		return new SecureUserArgumentResolver(secureUtil);
	}

	@Bean
	@ConditionalOnMissingBean(SecureRuleRegistry.class)
	public List<Rule> ruleListFromProperties(SecureProperties properties) {
		return properties.getRuleList();
	}

	@Bean
	@ConditionalOnBean(SecureRuleRegistry.class)
	public List<Rule> ruleListFromRegistry(SecureRuleRegistry secureRuleRegistry) {
		List<Rule> ruleList = secureRuleRegistry.getRuleList();
		if (CollectionUtils.isEmpty(ruleList)) {
			throw new IllegalArgumentException("规则列表不能为空");
		}
		return ruleList;
	}

	@Bean
	@ConditionalOnMissingBean(SecureRuleRegistry.class)
	public List<String> whiteListFromProperties(SecureProperties properties) {
		return properties.getWhiteList();
	}

	@Bean
	@ConditionalOnBean(SecureRuleRegistry.class)
	public List<String> whiteListFromRegistry(SecureRuleRegistry secureRuleRegistry) {
		return secureRuleRegistry.getWhiteList();
	}
}
