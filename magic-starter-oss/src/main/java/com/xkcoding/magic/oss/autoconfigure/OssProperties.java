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
	private CommonProperties aliOss = new CommonProperties();
	/**
	 * 腾讯云 COS
	 */
	private CommonProperties tencentCos = new CommonProperties();
	/**
	 * 七牛云
	 */
	private CommonProperties qiniuCloud = new CommonProperties();
	/**
	 * MinIO
	 */
	private CommonProperties minIo = new CommonProperties();

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
