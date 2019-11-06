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

import cn.hutool.core.lang.Validator;
import com.xkcoding.magic.core.tool.support.ip.Ip2Region;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>
 * IP 工具类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/24 17:29
 */
@Slf4j
public class IpUtil {

	/**
	 * 本机回环地址
	 */
	public static final String LOCAL_INNER_LOOP_IP = "0:0:0:0:0:0:0:1";

	/**
	 * 本机IP地址
	 */
	public static final String LOCAL_IP = "127.0.0.1";

	/**
	 * 根据ip获取城市信息
	 */
	public static String getAddress(String ip) {
		return Ip2Region.getAddress(ip);
	}

	/**
	 * 取外网IP
	 */
	public static String getRemoteIp(HttpServletRequest request) {
		String ip = request.getHeader("x-real-ip");
		if (ip == null) {
			ip = request.getRemoteAddr();
		}

		//过滤反向代理的ip
		String[] stemps = ip.split(StrUtil.COMMA);
		if (stemps.length >= 1) {
			//得到第一个IP，即客户端真实IP
			ip = stemps[0];
		}

		ip = ip.trim();
		if (ip.length() > 23) {
			ip = ip.substring(0, 23);
		}

		return ip;
	}

	/**
	 * 获取用户的真实ip
	 */
	public static String getUserIP(HttpServletRequest request) {

		// 优先取X-Real-IP
		String ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.length() == 0 || StrUtil.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}

		if (ip == null || ip.length() == 0 || StrUtil.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if (LOCAL_INNER_LOOP_IP.equals(ip)) {
				ip = LOCAL_IP;
			}
		}

		if (StrUtil.UNKNOWN.equalsIgnoreCase(ip)) {
			ip = LOCAL_IP;
			return ip;
		}

		int pos = ip.indexOf(StrUtil.C_COMMA);
		if (pos >= 0) {
			ip = ip.substring(0, pos);
		}

		return ip;
	}

	public static String getLastIpSegment(HttpServletRequest request) {
		String ip = getUserIP(request);
		if (ip != null) {
			ip = ip.substring(ip.lastIndexOf(StrUtil.C_DOT) + 1);
		} else {
			ip = StrUtil.ZERO;
		}
		return ip;
	}

	public static boolean isValidIP(HttpServletRequest request) {
		String ip = getUserIP(request);
		return isValidIP(ip);
	}

	/**
	 * 判断我们获取的ip是否是一个符合规则ip
	 */
	public static boolean isValidIP(String ip) {
		if (StrUtil.isBlank(ip)) {
			return false;
		}

		return Validator.isIpv4(ip);
	}

	public static String getLastServerIpSegment() {
		String ip = getServerIP();
		if (ip != null) {
			ip = ip.substring(ip.lastIndexOf(StrUtil.C_DOT) + 1);
		} else {
			ip = StrUtil.ZERO;
		}
		return ip;
	}

	public static String getServerIP() {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			return inetAddress.getHostAddress();
		} catch (UnknownHostException e) {
			log.error("获取服务器 IP 发生异常！", e);
		}
		return LOCAL_IP;
	}
}
