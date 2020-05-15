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

package com.xkcoding.magic.oss.autoconfigure.awss3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.xkcoding.magic.oss.autoconfigure.OssAutoConfiguration;
import com.xkcoding.magic.oss.autoconfigure.OssProperties;
import com.xkcoding.magic.oss.support.awss3.AwsS3Template;
import com.xkcoding.magic.oss.support.rule.OssRule;
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
 * 亚马逊 S3 存储自动装配类
 * </p>
 *
 * @author harrylee
 * @date Created in 2020/5/15 15:25
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureAfter(OssAutoConfiguration.class)
@ConditionalOnProperty(value = "magic.oss.aws-s3.enabled", havingValue = "true")
public class AwsS3AutoConfiguration {
	private final OssProperties ossProperties;

	@Bean
	@SneakyThrows
	@ConditionalOnMissingBean
	public AmazonS3 awsS3Client() {
		// 设置 AccessKey SecretKey
		AWSStaticCredentialsProvider awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(
			new BasicAWSCredentials(ossProperties.getAwsS3()
				.getAccessKey(), ossProperties.getAwsS3()
				.getSecretKey()));

		// 设置 Client
		ClientConfiguration clientConfiguration = new ClientConfiguration()
			.withProtocol(ossProperties.getAwsS3()
				.getHttps() ? Protocol.HTTPS : Protocol.HTTP)
			.withRequestTimeout(2000);

		// 设置 Endpoint
		AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
			ossProperties.getAwsS3().getEndpoint(),
			ossProperties.getAwsS3().getRegion());

		return AmazonS3ClientBuilder.standard()
			.withCredentials(awsStaticCredentialsProvider)
			.withClientConfiguration(clientConfiguration)
			.withPathStyleAccessEnabled(true)
			.withEndpointConfiguration(endpointConfiguration)
			.build();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean({AmazonS3.class, OssRule.class})
	public AwsS3Template minIoTemplate(AmazonS3 amazonS3, OssRule ossRule) {
		return new AwsS3Template(amazonS3, ossProperties, ossRule);
	}
}
