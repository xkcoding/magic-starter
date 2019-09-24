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
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.logging.LogLevel;

/**
 * <p>
 * 错误日志实体类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/24 15:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorLogModel extends AbstractLogModel {
	public ErrorLogModel() {
		super(LogType.ERROR_LOG, LogLevel.ERROR);
	}

	/**
	 * 堆栈信息
	 */
	private String stackTrace;
	/**
	 * 异常名
	 */
	private String exceptionName;
	/**
	 * 异常消息
	 */
	private String exceptionMessage;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 代码行数
	 */
	private Integer lineNumber;
}
