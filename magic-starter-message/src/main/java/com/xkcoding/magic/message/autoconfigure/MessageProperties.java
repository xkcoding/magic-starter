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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * <p>
 * 消息配置属性
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/11 14:13
 */
@Data
@ConfigurationProperties(prefix = "magic.message")
public class MessageProperties {
	/**
	 * 短信配置
	 */
	private SmsMessageProperties sms = new SmsMessageProperties();
	/**
	 * 邮箱配置
	 */
	private EmailMessageProperties email = new EmailMessageProperties();
	/**
	 * 钉钉配置
	 */
	private DingTalkMessageProperties dingtalk = new DingTalkMessageProperties();

	/**
	 * 短信配置
	 */
	@Data
	public static class SmsMessageProperties {
		/**
		 * 是否启用
		 */
		private boolean enabled = false;
		/**
		 * 应用ID
		 */
		private String accessKey;
		/**
		 * 应用秘钥
		 */
		private String secretKey;
		/**
		 * 短信模板配置
		 */
		private Map<String, String> channels;
	}

	/**
	 * 邮箱配置
	 */
	@Data
	public static class EmailMessageProperties {
		/**
		 * 是否启用
		 */
		private boolean enabled = false;
		/**
		 * HTML模板文件的目录，默认位置为classpath:/email/
		 */
		private String prefix = "classpath:/email/";
		/**
		 * 邮箱模板文件后缀，默认：.html
		 */
		private String suffix = ".html";
	}

	/**
	 * 钉钉配置
	 */
	@Data
	public static class DingTalkMessageProperties {
		/**
		 * 是否启用
		 */
		private boolean enabled = false;
		/**
		 * webhook
		 */
		private String webhook;
	}
}
