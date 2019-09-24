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

package com.xkcoding.magic.log.aspect;

import com.xkcoding.magic.core.tool.constants.MagicConsts;
import com.xkcoding.magic.log.annotation.OperateLog;
import com.xkcoding.magic.log.support.publisher.LogEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 操作日志切面
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/24 13:57
 */
@Slf4j
@Aspect
@Component
@Order(MagicConsts.AOP_ORDER_LOG)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ConditionalOnProperty(value = "magic.log.enabled", havingValue = "true", matchIfMissing = true)
public class OperateLogAspect {
	@Around("@annotation(operateLog)")
	public Object around(ProceedingJoinPoint point, OperateLog operateLog) throws Throwable {
		// 获取类名
		String className = point.getTarget().getClass().getName();
		// 获取方法
		String methodName = point.getSignature().getName();
		// 开始时间
		long beginTime = System.currentTimeMillis();
		// 执行方法
		Object result = point.proceed();
		// 执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;
		// 日志描述
		String description = operateLog.value();
		// 异步记录日志
		LogEventPublisher.publishOperateLogEvent(methodName, className, description, time);
		return result;
	}
}
