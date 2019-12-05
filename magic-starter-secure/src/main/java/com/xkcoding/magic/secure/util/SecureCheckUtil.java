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

package com.xkcoding.magic.secure.util;

import com.xkcoding.magic.core.tool.util.ClassUtil;
import com.xkcoding.magic.core.tool.util.SpringUtil;
import com.xkcoding.magic.secure.constants.SecureConstants;
import com.xkcoding.magic.secure.support.SecureExpressionHandler;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.MethodParameter;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * <p>
 * 鉴权检查工具
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/18 16:26
 */
public class SecureCheckUtil {
	/**
	 * 表达式处理
	 */
	private static final ExpressionParser PARSER = new SpelExpressionParser();

	/**
	 * 检查 SPEL 表达式
	 *
	 * @param spel    表达式
	 * @param context 上下文
	 * @return {@code true} / {@code false}
	 */
	public static boolean checkExpression(String spel, StandardEvaluationContext context) {
		Expression expression = PARSER.parseExpression(spel);
		Boolean result = expression.getValue(context, Boolean.class);
		return result != null ? result : false;
	}

	/**
	 * 包含方法上的参数的上下文
	 *
	 * @param secureExpressionHandler 上下文处理器
	 * @param method                  方法
	 * @param args                    参数
	 * @return 上下文
	 */
	public static StandardEvaluationContext getEvaluationContext(SecureExpressionHandler secureExpressionHandler, Method method, Object[] args) {
		// 初始化Spel表达式上下文，并设置 处理函数
		StandardEvaluationContext context = new StandardEvaluationContext(secureExpressionHandler);
		// 设置表达式支持spring bean
		context.setBeanResolver(new BeanFactoryResolver(SpringUtil.getContext()));
		for (int i = 0; i < args.length; i++) {
			// 读取方法参数
			MethodParameter methodParam = ClassUtil.getMethodParameter(method, i);
			// 设置方法 参数名和值 为sp el变量
			context.setVariable(methodParam.getParameterName(), args[i]);
		}
		return context;
	}

	/**
	 * 设置 request/response 参数的上下文
	 *
	 * @param secureExpressionHandler 上下文处理器
	 * @param request                 请求
	 * @param response                响应
	 * @return 上下文
	 */
	public static StandardEvaluationContext getEvaluationContext(SecureExpressionHandler secureExpressionHandler, HttpServletRequest request, HttpServletResponse response) {
		// 初始化Sp el表达式上下文，并设置 处理函数
		StandardEvaluationContext context = new StandardEvaluationContext(secureExpressionHandler);
		// 设置表达式支持spring bean
		context.setBeanResolver(new BeanFactoryResolver(SpringUtil.getContext()));
		context.setVariable(SecureConstants.REQUEST, request);
		context.setVariable(SecureConstants.RESPONSE, response);
		return context;
	}
}
