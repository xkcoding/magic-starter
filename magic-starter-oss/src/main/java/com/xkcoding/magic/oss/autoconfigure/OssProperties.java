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

package com.xkcoding.magic.oss.autoconfigure;

import com.xkcoding.magic.core.tool.constants.MagicConsts;
import com.xkcoding.magic.core.tool.support.lang.Kv;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 对象存储属性配置
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/12/30 16:37
 */
@Data
@ConfigurationProperties(prefix = "magic.oss")
public class OssProperties {
	/**
	 * 阿里云 OSS
	 */
	private AliOssProperties aliOss = new AliOssProperties();
	/**
	 * 腾讯云 COS
	 */
	private TencentCosProperties tencentCos = new TencentCosProperties();
	/**
	 * 七牛云
	 */
	private QiNiuCloudProperties qiniuCloud = new QiNiuCloudProperties();
	/**
	 * MinIO
	 */
	private MinIoProperties minIo = new MinIoProperties();

	/**
	 * 阿里云 OSS 配置
	 */
	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class AliOssProperties extends CommonProperties {
		/**
		 * 是否使用 https
		 */
		private Boolean https = false;
	}

	/**
	 * 腾讯云 OSS 配置
	 */
	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class TencentCosProperties extends CommonProperties {
		/**
		 * App Id
		 */
		private String appId;

		/**
		 * 区域简称，https://cloud.tencent.com/document/product/436/6224
		 */
		private String region;

		/**
		 * 是否使用 https
		 */
		private Boolean https = false;
	}

	/**
	 * 七牛云存储配置
	 */
	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class QiNiuCloudProperties extends CommonProperties {
		/**
		 * 区域简称，https://developer.qiniu.com/kodo/manual/1671/region-endpoint
		 */
		private String region;
	}

	/**
	 * MinIO 配置
	 */
	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class MinIoProperties extends CommonProperties {
	}

	/**
	 * 通用配置
	 */
	@Data
	public static class CommonProperties {
		/**
		 * 是否启用
		 */
		private boolean enabled = false;

		/**
		 * 对象存储服务的URL
		 */
		private String endpoint;

		/**
		 * Access key
		 */
		private String accessKey;

		/**
		 * Secret key
		 */
		private String secretKey;

		/**
		 * 默认的存储桶名称
		 */
		private String bucketName = MagicConsts.MAGIC;

		/**
		 * 自定义属性
		 */
		private Kv args;
	}
}
