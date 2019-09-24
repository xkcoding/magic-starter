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

import com.xkcoding.magic.secure.autoconfigure.SecureProperties;
import com.xkcoding.magic.secure.exception.ExpiredTokenException;
import com.xkcoding.magic.secure.exception.InvalidTokenException;
import com.xkcoding.magic.secure.model.SecureUser;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Jwt 工具类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/18 18:04
 */
@Slf4j
@RequiredArgsConstructor
public class JwtUtil {
	public static final String USER_ID = "id";
	public static final String USERNAME = "username";
	public static final String PERMISSIONS = "permissions";
	private final SecureProperties secureProperties;

	/**
	 * 从token中获取claim
	 *
	 * @param token token
	 * @return claim
	 */
	public Claims getClaimsFromToken(String token) {
		try {
			return Jwts.parser().setSigningKey(this.secureProperties.getJwt().getSecret().getBytes()).parseClaimsJws(token).getBody();

		} catch (ExpiredJwtException e) {
			throw new ExpiredTokenException("Token is expired!", e);
		} catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			throw new InvalidTokenException("Token invalid!", e);
		}
	}

	/**
	 * 获取token的过期时间
	 *
	 * @param token token
	 * @return 过期时间
	 */
	public Date getExpirationDateFromToken(String token) {
		return getClaimsFromToken(token).getExpiration();
	}

	/**
	 * 判断token是否过期
	 *
	 * @param token token
	 * @return 已过期返回true，未过期返回false
	 */
	private Boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	/**
	 * 为指定用户生成token
	 *
	 * @param secureUser 用户信息
	 * @return token
	 */
	public String generateToken(SecureUser secureUser) {
		Map<String, Object> claims = new HashMap<>(3);
		claims.put(USER_ID, secureUser.getId());
		claims.put(USERNAME, secureUser.getUsername());
		claims.put(PERMISSIONS, secureUser.getPermissions());
		Date createdTime = new Date();
		Date expirationTime = getExpirationTime();

		byte[] keyBytes = secureProperties.getJwt().getSecret().getBytes();
		SignatureAlgorithm algorithm = secureProperties.getJwt().getAlgorithm();

		return Jwts.builder().setClaims(claims).setIssuedAt(createdTime).setExpiration(expirationTime).signWith(algorithm, keyBytes).compact();
	}

	/**
	 * 计算token的过期时间
	 *
	 * @return 过期时间
	 */
	private Date getExpirationTime() {
		return new Date(System.currentTimeMillis() + secureProperties.getJwt().getTimeout().toMillis());
	}

	/**
	 * 判断token是否非法
	 *
	 * @param token token
	 * @return 未过期返回true，否则返回false
	 */
	public Boolean validateToken(String token) {
		return !isTokenExpired(token);
	}
}
