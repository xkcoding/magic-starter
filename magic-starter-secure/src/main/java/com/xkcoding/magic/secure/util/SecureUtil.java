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

package com.xkcoding.magic.secure.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.xkcoding.magic.secure.constants.SecureConstants;
import com.xkcoding.magic.secure.exception.InvalidTokenException;
import com.xkcoding.magic.secure.exception.NotFoundTokenException;
import com.xkcoding.magic.secure.exception.SecureException;
import com.xkcoding.magic.secure.model.SecureUser;
import com.xkcoding.magic.secure.support.UserContextHolder;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 鉴权工具类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/18 17:08
 */
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SecureUtil {
	private static final int SEVEN = 7;
	private final JwtUtil jwtUtil;

	/**
	 * 返回当前用户信息
	 *
	 * @return 当前用户信息
	 */
	public SecureUser getCurrentUser() {
		SecureUser secureUser = UserContextHolder.get();
		if (secureUser == null) {
			// 从 token 里获取
			HttpServletRequest request = getRequest();
			String token = getTokenFromRequest(request);
			// 校验 token 是否合法
			Boolean isValid = jwtUtil.validateToken(token);
			if (!isValid) {
				return null;
			}

			secureUser = getUserFromToken(token);
			UserContextHolder.set(secureUser);
		}
		return secureUser;
	}

	/**
	 * 解析token，获得用户信息
	 *
	 * @param token token
	 * @return 用户信息
	 */
	@SuppressWarnings("unchecked")
	private SecureUser getUserFromToken(String token) {
		Claims claims = jwtUtil.getClaimsFromToken(token);
		Object userId = claims.get(JwtUtil.USER_ID);
		Object username = claims.get(JwtUtil.USERNAME);
		Object permissions = claims.get(JwtUtil.PERMISSIONS);

		return SecureUser.builder().id(Convert.toLong(userId)).username((String) username).permissions(CollUtil.newHashSet((List<String>) permissions)).build();
	}

	/**
	 * 从request中获取token
	 *
	 * @param request 请求
	 * @return token
	 */
	private String getTokenFromRequest(HttpServletRequest request) {
		String header = request.getHeader(SecureConstants.AUTHORIZATION_HEADER);
		if (StringUtils.isEmpty(header)) {
			throw new NotFoundTokenException("User is not login!");
		}
		if (header.length() <= SEVEN) {
			throw new InvalidTokenException("Token invalid，length <= 7!");
		}

		if (!header.startsWith(SecureConstants.BEARER)) {
			throw new InvalidTokenException("Token invalid, must be started with 'Bearer '!");
		}

		return header.substring(SEVEN);
	}

	/**
	 * 获取request
	 *
	 * @return request
	 */
	private HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if ((requestAttributes == null)) {
			throw new SecureException("There is not web environment!");
		}
		return ((ServletRequestAttributes) requestAttributes).getRequest();
	}

}
