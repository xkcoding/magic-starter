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

package com.xkcoding.magic.core.tool.autoconfigure;

import com.xkcoding.magic.core.tool.enums.Ip2RegionSearchType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 配置属性
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/24 17:58
 */
@Data
@ConfigurationProperties(prefix = "magic.tool")
public class ToolProperties {
	/**
	 * ip2region 配置.
	 */
	private Ip2RegionProperties ipRegion;

	@Data
	public static class Ip2RegionProperties {
		/**
		 * ip 数据文件，默认读取 classpath 下的 ip/ip2region.db 文件.
		 * 该文件可以从 https://github.com/xkcoding/magic-starter/tree/master/magic-core-tool/src/main/resources/ip/ip2region.db 下载
		 */
		private String dbFile = "ip/ip2region.db";
		/**
		 * 查询方式
		 */
		private Ip2RegionSearchType searchType = Ip2RegionSearchType.MEMORY;
	}
}
