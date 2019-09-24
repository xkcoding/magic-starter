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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 配置属性
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/24 14:28
 */
@Data
@ConfigurationProperties(prefix = "magic.log")
public class LogProperties {
	/**
	 * 是否启用 magic log，默认 true.
	 */
	private boolean enabled = true;

	/**
	 * Web请求日志配置熟悉.
	 */
	private RequestLogProperties request;

	/**
	 * Web请求日志配置熟悉
	 */
	@Data
	public static class RequestLogProperties {
		/**
		 * 是否启用 request log，默认 false.
		 */
		private boolean enabled = false;
	}
}
