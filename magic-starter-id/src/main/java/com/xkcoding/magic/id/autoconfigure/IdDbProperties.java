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

package com.xkcoding.magic.id.autoconfigure;

import com.xkcoding.magic.id.autoconfigure.base.BaseIdProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * 数据库步长ID生成器配置
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/29 17:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "magic.id.database")
public class IdDbProperties extends BaseIdProperties {
	/**
	 * 表名称
	 */
	private String tableName = "magic_id";
	/**
	 * 重试次数
	 */
	private int retryTimes = 1;

}
