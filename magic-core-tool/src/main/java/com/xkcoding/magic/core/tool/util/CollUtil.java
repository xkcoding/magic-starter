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

package com.xkcoding.magic.core.tool.util;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.lang.Nullable;

/**
 * <p>
 * 对 hutool CollUtil 的扩展
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2020/1/2 10:49
 */
public class CollUtil extends cn.hutool.core.collection.CollUtil {
	/**
	 * 数组中是否包含元素
	 *
	 * @param <T>   数组元素类型
	 * @param array 数组
	 * @param value 被检查的元素
	 * @return 是否包含
	 */
	public static <T> boolean contains(@Nullable T[] array, T value) {
		return ArrayUtil.isNotEmpty(array) && ArrayUtil.contains(array, value);
	}
}
