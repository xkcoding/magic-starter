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

import cn.hutool.core.date.DateUtil;
import com.xkcoding.magic.core.tool.util.StrUtil;
import com.xkcoding.magic.id.support.business.BusinessName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 日期业务名称
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/29 16:26
 */
@NoArgsConstructor
@AllArgsConstructor
public class DateBusinessName implements BusinessName {
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
		if (StrUtil.isBlank(businessName)) {
			this.businessName = StrUtil.EMPTY;
		}
		return this.businessName + DateUtil.today();
	}
}
