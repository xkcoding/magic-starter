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

package com.xkcoding.magic.core.tool.support.ip;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.xkcoding.magic.core.tool.autoconfigure.ToolProperties;
import com.xkcoding.magic.core.tool.util.IpUtil;
import com.xkcoding.magic.core.tool.util.SpringUtil;
import com.xkcoding.magic.core.tool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * Ip 地址转换
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/11/6 10:42
 */
@Slf4j
public class Ip2Region {
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
}
