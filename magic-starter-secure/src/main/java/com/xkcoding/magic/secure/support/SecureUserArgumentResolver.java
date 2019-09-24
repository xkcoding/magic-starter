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

package com.xkcoding.magic.secure.support;

import com.xkcoding.magic.secure.annotation.CurrentUser;
import com.xkcoding.magic.secure.model.SecureUser;
import com.xkcoding.magic.secure.util.SecureUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * <p>
 * 参数绑定解析
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/19 10:07
 */
@RequiredArgsConstructor
public class SecureUserArgumentResolver implements HandlerMethodArgumentResolver {
	private final SecureUtil secureUtil;

	/**
	 * 参数上必须包含 {@link CurrentUser} 注解，同时满足参数类型是 {@link SecureUser}
	 *
	 * @param parameter 参数
	 * @return 满足条件 true，反之 false
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().isAssignableFrom(SecureUser.class) && parameter.hasParameterAnnotation(CurrentUser.class);
	}

	/**
	 * 解析参数
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		return secureUtil.getCurrentUser();
	}
}
