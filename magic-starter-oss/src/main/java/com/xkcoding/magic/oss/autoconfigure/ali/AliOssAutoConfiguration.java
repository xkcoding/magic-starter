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

package com.xkcoding.magic.oss.autoconfigure.ali;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.Protocol;
import com.xkcoding.magic.oss.autoconfigure.OssAutoConfiguration;
import com.xkcoding.magic.oss.autoconfigure.OssProperties;
import com.xkcoding.magic.oss.support.ali.AliOssTemplate;
import com.xkcoding.magic.oss.support.rule.OssRule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 阿里云 OSS 对象存储自动装配类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2020/1/1 09:05
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureAfter(OssAutoConfiguration.class)
@ConditionalOnProperty(value = "magic.oss.ali-oss.enabled", havingValue = "true")
public class AliOssAutoConfiguration {
	private final OssProperties ossProperties;

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
	public OSSClient ossClient() {
		// 创建ClientConfiguration。ClientConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
		ClientBuilderConfiguration config = new ClientBuilderConfiguration();
		// 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
		config.setMaxConnections(1024);
		// 设置Socket层传输数据的超时时间，默认为50000毫秒。
		config.setSocketTimeout(50000);
		// 设置建立连接的超时时间，默认为50000毫秒。
		config.setConnectionTimeout(50000);
		// 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
		config.setConnectionRequestTimeout(1000);
		// 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
		config.setIdleConnectionTime(60000);
		// 设置失败请求重试次数，默认为3次。
		config.setMaxErrorRetry(5);
		// 配置协议
		config.setProtocol(ossProperties.getAliOss()
			.getHttps() ? Protocol.HTTPS : Protocol.HTTP);

		return (OSSClient) new OSSClientBuilder().build(ossProperties.getAliOss()
			.getEndpoint(), ossProperties.getAliOss()
			.getAccessKey(), ossProperties.getAliOss()
			.getSecretKey(), config);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean({OSSClient.class, OssRule.class})
	public AliOssTemplate aliossTemplate(OSSClient ossClient, OssRule ossRule) {
		return new AliOssTemplate(ossClient, ossProperties, ossRule);
	}
}
