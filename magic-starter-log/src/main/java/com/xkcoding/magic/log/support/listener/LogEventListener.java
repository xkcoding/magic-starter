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

package com.xkcoding.magic.log.support.listener;

import com.xkcoding.magic.log.constants.LogConstants;
import com.xkcoding.magic.log.model.AbstractLogModel;
import com.xkcoding.magic.log.model.CustomLogModel;
import com.xkcoding.magic.log.model.ErrorLogModel;
import com.xkcoding.magic.log.model.OperateLogModel;
import com.xkcoding.magic.log.support.LogHandler;
import com.xkcoding.magic.log.support.OperatorService;
import com.xkcoding.magic.log.support.event.CustomLogEvent;
import com.xkcoding.magic.log.support.event.ErrorLogEvent;
import com.xkcoding.magic.log.support.event.OperateLogEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 日志事件监听器
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/24 16:10
 */
@Slf4j
@RequiredArgsConstructor
public class LogEventListener {
	private final LogHandler logHandler;
	private final OperatorService operatorService;

	/**
	 * 操作日志事件处理
	 *
	 * @param event 事件
	 */
	@Async
	@Order
	@EventListener(OperateLogEvent.class)
	public void handleOperateLogEvent(OperateLogEvent event) {
		OperateLogModel model = wrapperLogModel(event);
		logHandler.handleOperateLog(model);
	}

	/**
	 * 自定义日志事件处理
	 *
	 * @param event 事件
	 */
	@Async
	@Order
	@EventListener(CustomLogEvent.class)
	public void handleCustomLogEvent(CustomLogEvent event) {
		CustomLogModel model = wrapperLogModel(event);
		logHandler.handleCustomLog(model);
	}

	/**
	 * 错误日志事件处理
	 *
	 * @param event 事件
	 */
	@Async
	@Order
	@EventListener(ErrorLogEvent.class)
	public void handleErrorLogEvent(ErrorLogEvent event) {
		ErrorLogModel model = wrapperLogModel(event);
		logHandler.handleErrorLog(model);
	}

	@SuppressWarnings("unchecked")
	private <T extends AbstractLogModel> T wrapperLogModel(ApplicationEvent event) {
		Map<String, Object> source = (Map<String, Object>) event.getSource();
		T model = (T) source.get(LogConstants.EVENT_LOG_KEY);
		HttpServletRequest request = (HttpServletRequest) source.get(LogConstants.EVENT_REQUEST_KEY);
		model = model.wrapperLog(model, request, operatorService.getOperator(request));
		return model;
	}
}
