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

package com.xkcoding.magic.id.support.id;

import com.xkcoding.magic.id.support.business.BusinessName;
import com.xkcoding.magic.id.support.prefix.Prefix;
import com.xkcoding.magic.id.support.range.RangeManager;

/**
 * <p>
 * 区间范围主键生成器接口
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/29 15:50
 */
public interface RangeId extends Id {

	/**
	 * 设置主键区间范围管理器
	 *
	 * @param rangeManager 区间范围管理器
	 */
	void setRangeManager(RangeManager rangeManager);

	/**
	 * 设置主键获取名称
	 *
	 * @param prefix 前缀
	 */
	void setPrefix(Prefix prefix);

	/**
	 * 设置业务名称
	 *
	 * @param name 业务名称
	 */
	void setBusinessName(BusinessName name);
}
