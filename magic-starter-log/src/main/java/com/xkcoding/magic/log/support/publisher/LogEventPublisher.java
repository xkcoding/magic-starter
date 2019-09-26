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

package com.xkcoding.magic.log.support.publisher;

import com.xkcoding.magic.core.tool.util.ExceptionUtil;
import com.xkcoding.magic.core.tool.util.SpringUtil;
import com.xkcoding.magic.core.tool.util.WebUtil;
import com.xkcoding.magic.log.constants.LogConstants;
import com.xkcoding.magic.log.model.CustomLogModel;
import com.xkcoding.magic.log.model.ErrorLogModel;
import com.xkcoding.magic.log.model.OperateLogModel;
import com.xkcoding.magic.log.support.event.CustomLogEvent;
import com.xkcoding.magic.log.support.event.ErrorLogEvent;
import com.xkcoding.magic.log.support.event.OperateLogEvent;
import org.springframework.boot.logging.LogLevel;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 日志事件发布
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/24 15:56
 */
public class LogEventPublisher {
	/**
	 * 发布操作日志事件
	 *
	 * @param className   执行类名
	 * @param methodName  执行方法名
	 * @param description 操作日志描述
	 * @param spendTime   执行时间
	 */
	public static void publishOperateLogEvent(String className, String methodName, String description, Long spendTime) {
		HttpServletRequest request = WebUtil.getRequest();
		OperateLogModel model = new OperateLogModel();
		model.setClassName(className);
		model.setDescription(description);
		model.setMethodName(methodName);
		model.setSpendTime(spendTime);
		Map<String, Object> event = new HashMap<>(16);
		event.put(LogConstants.EVENT_LOG_KEY, model);
		event.put(LogConstants.EVENT_REQUEST_KEY, request);
		SpringUtil.publishEvent(new OperateLogEvent(event));
	}

	/**
	 * 发布自定义日志事件
	 *
	 * @param level       日志级别
	 * @param prefix      日志前缀
	 * @param description 日志描述
	 */
	public static void publishCustomLogEvent(LogLevel level, String prefix, String description) {
		HttpServletRequest request = WebUtil.getRequest();
		CustomLogModel model = new CustomLogModel(level);
		model.setPrefix(prefix);
		model.setDescription(description);
		Map<String, Object> event = new HashMap<>(16);
		event.put(LogConstants.EVENT_LOG_KEY, model);
		event.put(LogConstants.EVENT_REQUEST_KEY, request);
		SpringUtil.publishEvent(new CustomLogEvent(event));
	}

	/**
	 * 发布错误日志事件
	 *
	 * @param error 异常
	 */
	public static void publishErrorLogEvent(Throwable error) {
		HttpServletRequest request = WebUtil.getRequest();
		ErrorLogModel model = new ErrorLogModel();
		if (!ObjectUtils.isEmpty(error)) {
			model.setStackTrace(ExceptionUtil.stacktraceToString(error));
			model.setExceptionName(error.getClass().getName());
			model.setExceptionMessage(error.getMessage());
			StackTraceElement[] elements = error.getStackTrace();
			if (!ObjectUtils.isEmpty(elements)) {
				StackTraceElement element = elements[0];
				model.setMethodName(element.getMethodName());
				model.setClassName(element.getClassName());
				model.setFileName(element.getFileName());
				model.setLineNumber(element.getLineNumber());
			}
		}
		Map<String, Object> event = new HashMap<>(16);
		event.put(LogConstants.EVENT_LOG_KEY, model);
		event.put(LogConstants.EVENT_REQUEST_KEY, request);
		SpringUtil.publishEvent(new ErrorLogEvent(event));
	}
}
