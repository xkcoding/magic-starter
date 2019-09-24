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
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 配置属性
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/18 18:23
 */
@Data
@ConfigurationProperties(prefix = "magic.secure")
public class SecureProperties {
	/**
	 * 是否启用 magic secure，默认 true.
	 */
	private boolean enabled = true;
	/**
	 * jwt 配置类.
	 */
	private Jwt jwt = new Jwt();

	/**
	 * 规则列表.
	 */
	private List<Rule> ruleList = new ArrayList<>();

	/**
	 * 白名单.
	 */
	private List<String> whiteList = new ArrayList<>();

	@Data
	public static class Jwt {
		/**
		 * secret
		 */
		private String secret = "secure";

		/**
		 * token的有效时间，默认7天
		 *
		 * @see Duration
		 */
		private Duration timeout = Duration.ofDays(7);

		/**
		 * 加密的算法，默认sha512
		 */
		private SignatureAlgorithm algorithm = SignatureAlgorithm.HS512;
	}

}
