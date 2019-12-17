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

import com.xkcoding.magic.core.tool.constants.MimeType;

/**
 * <p>
 * 对 hutool StrUtil 的扩展
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/24 14:12
 */
public class StrUtil extends cn.hutool.core.util.StrUtil {
	public static final String UNKNOWN = "unknown";
	public static final String UTF_8 = "UTF-8";
	public static final String GBK = "GBK";
	public static final String ISO_8859_1 = "ISO-8859-1";
	public static final String Y = "y";
	public static final String YES = "yes";
	public static final String ONE = "1";
	public static final String ZERO = "0";

	/**
	 * 创建StringBuilder对象
	 *
	 * @param sb   初始StringBuilder
	 * @param strs 初始字符串列表
	 * @return StringBuilder对象
	 */
	public static StringBuilder appendBuilder(StringBuilder sb, CharSequence... strs) {
		for (CharSequence str : strs) {
			sb.append(str);
		}
		return sb;
	}

	/**
	 * 获取文件后缀名
	 *
	 * @param fileName 文件名
	 * @return 文件后缀名
	 */
	public static String fileExt(String fileName) {
		if (isBlank(fileName) || fileName.indexOf(StrUtil.C_DOT) == -1) {
			return null;
		}
		return fileName.substring(fileName.lastIndexOf(StrUtil.C_DOT) + 1);
	}

	/**
	 * 获取媒体类型
	 *
	 * @param fileName 文件名
	 * @return 媒体类型
	 */
	public static String mimeType(String fileName) {
		String ext = fileExt(fileName);
		if (null == ext) {
			return null;
		}
		return MimeType.get(ext);
	}

	/**
	 * 用空格补齐指定长度字符串，左侧
	 *
	 * @param str    字符串
	 * @param length 总长度
	 * @return 修改后字符串
	 */
	public static String padLeft(String str, int length) {
		return String.format("%1$" + length + "s", str);
	}

	/**
	 * 用空格补齐指定长度字符串，右侧
	 *
	 * @param str    字符串
	 * @param length 总长度
	 * @return 修改后字符串
	 */
	public static String padRight(String str, int length) {
		return String.format("%1$-" + length + "s", str);
	}

	/**
	 * 字符串转下划线
	 *
	 * @param str 字符串
	 * @return 下划线格式
	 */
	public static String toUnderlineName(String str) {
		if (str == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			boolean nextUpperCase = true;

			if (i < (str.length() - 1)) {
				nextUpperCase = Character.isUpperCase(str.charAt(i + 1));
			}

			if (Character.isUpperCase(c)) {
				if (!upperCase || !nextUpperCase) {
					if (i > 0) {
						sb.append(StrUtil.C_UNDERLINE);
					}
				}
				upperCase = true;
			} else {
				upperCase = false;
			}

			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

	/**
	 * 字符串转驼峰（首字母小写）
	 *
	 * @param str 字符串
	 * @return 驼峰格式（首字母小写）
	 */
	public static String toCamelCase(String str) {
		if (str == null) {
			return null;
		}
		str = str.toLowerCase();
		StringBuilder sb = new StringBuilder(str.length());
		boolean upperCase = false;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			if (c == StrUtil.C_UNDERLINE) {
				upperCase = true;
			} else if (upperCase) {
				sb.append(Character.toUpperCase(c));
				upperCase = false;
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * 字符串转驼峰（首字母大写）
	 *
	 * @param str 字符串
	 * @return 驼峰格式（首字母大写）
	 */
	public static String toCapitalizeCamelCase(String str) {
		if (str == null) {
			return null;
		}
		str = toCamelCase(str);
		return str.substring(0, 1)
			.toUpperCase() + str.substring(1);
	}
}
