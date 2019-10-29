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

package com.xkcoding.magic.id.support.business.impl;

import com.xkcoding.magic.id.support.business.BusinessName;
import lombok.AllArgsConstructor;

/**
 * <p>
 * 默认业务名称
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/29 16:29
 */
@AllArgsConstructor
public class DefaultBusinessName implements BusinessName {
	/**
	 * 业务名称
	 */
	private String businessName;

	/**
	 * 业务名称
	 *
	 * @return 业务名称
	 */
	@Override
	public String get() {
		return this.businessName;
	}
}
