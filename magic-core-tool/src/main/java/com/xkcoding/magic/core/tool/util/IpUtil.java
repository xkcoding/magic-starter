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

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Validator;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.xkcoding.magic.core.tool.autoconfigure.ToolProperties;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

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
		if (StrUtil.equals(ip, IpUtil.LOCAL_INNER_LOOP_IP)) {
			ip = IpUtil.LOCAL_IP;
		}
		String address = "";
		boolean ipAddress = Util.isIpAddress(ip);
		if (!ipAddress) {
			return address;
		}
		try {
			DbConfig config = new DbConfig();

			ToolProperties toolProperties = SpringUtil.getBean(ToolProperties.class);

			InputStream streamSafe = ResourceUtil.getStreamSafe(toolProperties.getIpRegion().getDbFile());

			if (streamSafe == null) {
				log.error("【获取地理位置】未找到 IP 数据库文件，请前往 https://github.com/xkcoding/magic-starter/tree/master/magic-core-tool/src/main/resources/ip/ip2region.db 下载！");
				return address;
			}

			DbSearcher searcher = new DbSearcher(config, IoUtil.readBytes(streamSafe));
			DataBlock dataBlock = searcher.memorySearch(ip);

			// dataBlock格式：城市Id|国家|区域|省份|城市|ISP
			// region格式：国家|区域|省份|城市|ISP
			// region例如：中国|0|浙江省|杭州市|电信
			String region = dataBlock.getRegion();

			// 按 | 切分
			List<String> regionList = Splitter.on("|").splitToList(region);
			// 过滤为 0 的数据
			List<String> regionListFilter = regionList.stream().filter(s -> !StrUtil.equals(StrUtil.ZERO, s)).distinct().collect(Collectors.toList());
			// 再用 | 拼接回来
			address = Joiner.on("|").join(regionListFilter);
		} catch (IOException | DbMakerConfigException e) {
			log.error("【获取地理位置】发生异常: ", e);
		}
		return address;
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
