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
}
