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

package com.xkcoding.magic.oss.autoconfigure.minio;

import com.xkcoding.magic.oss.autoconfigure.OssAutoConfiguration;
import com.xkcoding.magic.oss.autoconfigure.OssProperties;
import com.xkcoding.magic.oss.support.minio.MinIoTemplate;
import com.xkcoding.magic.oss.support.rule.OssRule;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 七牛云存储自动装配类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2020/1/2 15:25
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureAfter(OssAutoConfiguration.class)
@ConditionalOnProperty(value = "magic.oss.min-io.enabled", havingValue = "true")
public class MinIoAutoConfiguration {
	private final OssProperties ossProperties;

	@Bean
	@SneakyThrows
	@ConditionalOnMissingBean
	public MinioClient minioClient() {
		return new MinioClient(ossProperties.getMinIo()
			.getEndpoint(), ossProperties.getMinIo()
			.getAccessKey(), ossProperties.getMinIo()
			.getSecretKey());
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean({MinioClient.class, OssRule.class})
	public MinIoTemplate minIoTemplate(MinioClient minioClient, OssRule ossRule) {
		return new MinIoTemplate(minioClient, ossProperties, ossRule);
	}
}
