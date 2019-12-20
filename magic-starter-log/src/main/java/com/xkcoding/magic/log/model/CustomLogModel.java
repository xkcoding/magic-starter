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

package com.xkcoding.magic.log.model;

import com.xkcoding.magic.log.enums.LogType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.logging.LogLevel;

/**
 * <p>
 * 自定义日志实体类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/24 15:01
 */
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CustomLogModel extends AbstractLogModel {
	public CustomLogModel(LogLevel level) {
		super(LogType.CUSTOM, level);
	}

	/**
	 * 日志前缀
	 */
	private String prefix;
	/**
	 * 日志描述
	 */
	private String description;
}
