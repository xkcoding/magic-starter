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

package com.xkcoding.magic.secure.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * <p>
 * 默认用户
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/18 17:10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecureUser {
	/**
	 * 用户id
	 */
	private Long id;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 权限标识
	 */
	private Set<String> permissions;
}
