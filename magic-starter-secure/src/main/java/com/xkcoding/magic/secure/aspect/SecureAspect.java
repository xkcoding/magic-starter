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

package com.xkcoding.magic.secure.aspect;

import cn.hutool.core.util.StrUtil;
import com.xkcoding.magic.core.tool.constants.MagicConsts;
import com.xkcoding.magic.core.tool.util.ClassUtil;
import com.xkcoding.magic.secure.annotation.Secure;
import com.xkcoding.magic.secure.exception.AuthorizationException;
import com.xkcoding.magic.secure.support.SecureExpressionHandler;
import com.xkcoding.magic.secure.util.SecureCheckUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * <p>
 * 鉴权注解切面
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/18 15:33
 */
@Slf4j
@Aspect
@Component
@Order(MagicConsts.AOP_ORDER_SECURE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ConditionalOnProperty(value = "magic.secure.enabled", havingValue = "true", matchIfMissing = true)
public class SecureAspect {
	private final SecureExpressionHandler secureExpressionHandler;

	@Around("@annotation(com.xkcoding.magic.secure.annotation.Secure) || @within(com.xkcoding.magic.secure.annotation.Secure)")
	public Object preAuth(ProceedingJoinPoint joinPoint) throws Throwable {
		if (validateSecure(joinPoint)) {
			return joinPoint.proceed();
		}
		throw new AuthorizationException("Access Denied!");
	}

	/**
	 * 校验权限
	 *
	 * @param joinPoint 切入点
	 * @return 是否有访问权限
	 */
	private boolean validateSecure(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		// 读取权限注解，优先方法上，没有则读取类
		Secure secure = ClassUtil.getAnnotation(method, Secure.class);
		// 权限表达式
		String secureExpression = secure.value();
		if (StrUtil.isNotBlank(secureExpression)) {
			// 方法参数值
			Object[] args = joinPoint.getArgs();
			StandardEvaluationContext context = SecureCheckUtil.getEvaluationContext(secureExpressionHandler, method, args);
			return SecureCheckUtil.checkExpression(secureExpression, context);
		}
		return false;
	}

}
