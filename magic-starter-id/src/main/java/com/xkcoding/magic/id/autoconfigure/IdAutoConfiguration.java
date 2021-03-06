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

package com.xkcoding.magic.id.autoconfigure;

import com.xkcoding.magic.id.support.factory.impl.SnowflakeIdFactory;
import com.xkcoding.magic.id.support.id.Id;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 分布式主键自动装配类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/29 15:42
 */
@Configuration
@EnableConfigurationProperties({IdSnowflakeProperties.class, IdDatabaseProperties.class, IdRedisProperties.class})
public class IdAutoConfiguration {

	/**
	 * 默认使用雪花算法实现
	 */
	@Bean
	@ConditionalOnMissingBean
	public Id snowflakeId(IdSnowflakeProperties properties) {
		return SnowflakeIdFactory.create().dataCenterId(properties.getDataCenterId()).workerId(properties.getWorkerId()).prefix(properties::getPrefix).getInstance();
	}

}
