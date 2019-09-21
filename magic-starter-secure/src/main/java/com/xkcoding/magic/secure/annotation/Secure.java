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

package com.xkcoding.magic.secure.annotation;

import com.xkcoding.magic.secure.support.DefaultSecureExpressionHandler;

import java.lang.annotation.*;

/**
 * <p>
 * 自定义方法、类上鉴权注解，内置的权限表达式 {@link DefaultSecureExpressionHandler} 也可以自定义 Spring Bean 使用 {@code @} 引用
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/18 15:20
 * @see DefaultSecureExpressionHandler
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Secure {
	/**
	 * 权限表达式，内置的权限表达式 {@link DefaultSecureExpressionHandler} 也可以自定义 Spring Bean 使用 {@code @} 引用
	 *
	 * @return 权限表达式
	 * @see DefaultSecureExpressionHandler
	 */
	String value();
}
