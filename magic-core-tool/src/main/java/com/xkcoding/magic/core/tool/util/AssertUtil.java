

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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xkcoding.magic.core.tool.enums.CommonResultCode;
import com.xkcoding.magic.core.tool.exception.ServiceException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 断言工具类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/11 14:36
 */
public class AssertUtil {
	private static Validator validator;

	static {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	/**
	 * 校验对象
	 *
	 * @param object 待校验对象
	 * @param groups 待校验的组
	 */
	public static void validateEntity(Object object, Class<?>... groups) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
		if (!constraintViolations.isEmpty()) {
			StringBuilder msg = new StringBuilder();
			for (ConstraintViolation<Object> constraint : constraintViolations) {
				msg.append(constraint.getMessage()).append("<br>");
			}
			throw new ServiceException(CommonResultCode.PARAM_VALID_ERROR, msg.toString());
		}
	}

	/**
	 * 数值是否在范围内
	 *
	 * @param number  待校验数值
	 * @param start   起始值
	 * @param end     终止值
	 * @param message 异常消息
	 */
	public static void isNotBetween(int number, int start, int end, String message) {
		if (number > end || number < start) {
			throw new ServiceException(message);
		}
	}

	/**
	 * 字符串是否为空
	 *
	 * @param str     待校验字符串
	 * @param message 异常消息
	 */
	public static void isBlank(String str, String message) {
		if (StrUtil.isBlank(str)) {
			throw new ServiceException(message);
		}
	}

	/**
	 * 对象是否为空
	 *
	 * @param object  待校验对象
	 * @param message 异常消息
	 */
	public static void isNull(Object object, String message) {
		if (ObjectUtil.isNull(object)) {
			throw new ServiceException(message);
		}
	}

	/**
	 * 集合是否为空
	 *
	 * @param collection 待校验对象
	 * @param message    异常消息
	 */
	public static void isEmpty(Collection<?> collection, String message) {
		if (CollUtil.isEmpty(collection)) {
			throw new ServiceException(message);
		}
	}

	/**
	 * map是否为空
	 *
	 * @param map     待校验对象
	 * @param message 异常消息
	 */
	public static void isEmpty(Map<?, ?> map, String message) {
		if (MapUtil.isEmpty(map)) {
			throw new ServiceException(message);
		}
	}

	/**
	 * 集合是否不为空
	 *
	 * @param collection 待校验对象
	 * @param message    异常消息
	 */
	public static void isNotEmpty(Collection<?> collection, String message) {
		if (CollUtil.isNotEmpty(collection)) {
			throw new ServiceException(message);
		}
	}
}
