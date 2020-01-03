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

package com.xkcoding.magic.oss.autoconfigure.tencent;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import com.xkcoding.magic.oss.autoconfigure.OssAutoConfiguration;
import com.xkcoding.magic.oss.autoconfigure.OssProperties;
import com.xkcoding.magic.oss.support.rule.OssRule;
import com.xkcoding.magic.oss.support.tencent.TencentCosTemplate;
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
 * 腾讯云 COS 对象存储自动装配类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2020/1/3 11:05
 */
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureAfter(OssAutoConfiguration.class)
@ConditionalOnProperty(value = "magic.oss.tencent-cos.enabled", havingValue = "true")
public class TencentCosAutoConfiguration {
	private final OssProperties ossProperties;

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean
	public COSClient cosClient() {
		// 1 初始化用户身份信息（secretId, secretKey）
		COSCredentials credentials = new BasicCOSCredentials(ossProperties.getTencentCos()
			.getAccessKey(), ossProperties.getTencentCos()
			.getSecretKey());

		// 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
		Region region = new Region(ossProperties.getTencentCos()
			.getRegion());

		// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
		ClientConfig clientConfig = new ClientConfig(region);
		// 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
		clientConfig.setMaxConnectionsCount(1024);
		// 设置Socket层传输数据的超时时间，默认为50000毫秒。
		clientConfig.setSocketTimeout(50000);
		// 设置建立连接的超时时间，默认为50000毫秒。
		clientConfig.setConnectionTimeout(50000);
		// 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
		clientConfig.setConnectionRequestTimeout(1000);
		return new COSClient(credentials, clientConfig);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean({COSClient.class, OssRule.class})
	public TencentCosTemplate tencentCosTemplate(COSClient cosClient, OssRule ossRule) {
		return new TencentCosTemplate(cosClient, ossProperties, ossRule);
	}
}
