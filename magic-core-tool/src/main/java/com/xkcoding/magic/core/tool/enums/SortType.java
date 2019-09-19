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

package com.xkcoding.magic.core.tool.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 排序枚举
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/19 22:06
 */
@Getter
@AllArgsConstructor
public enum SortType {
	/**
	 * 升序
	 */
	ASC(1),
	/**
	 * 降序
	 */
	DESC(-1);

	int value;

	/**
	 * 通过参数转换
	 *
	 * @param value 值
	 * @return 枚举对象
	 */
	public static SortType fromInt(int value) {
		switch (value) {
			case -1:
				return DESC;
			case 1:
				return ASC;
			default:
				throw new IllegalArgumentException(value + " is not a valid");
		}
	}

	/**
	 * 通过字符串转化
	 *
	 * @param value 值
	 * @return 枚举对象
	 */
	public static SortType fromStr(String value) {
		if (StrUtil.isNotBlank(value)) {
			return SortType.valueOf(value.toUpperCase());
		}
		throw new IllegalArgumentException(value + " is not a valid");
	}
}
