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
package com.xkcoding.magic.core.tool.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Spring 工具类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/19 17:58
 */
@Slf4j
@Component
public class SpringUtil implements ApplicationContextAware {
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringUtil.context = context;
	}

	/**
	 * 获取 Spring Bean
	 *
	 * @param clazz 类
	 * @param <T>   泛型
	 * @return 对象
	 */
	public static <T> T getBean(Class<T> clazz) {
		if (clazz == null) {
			return null;
		}
		return context.getBean(clazz);
	}

	/**
	 * 获取 Spring Bean
	 *
	 * @param bean 名称
	 * @param <T>  泛型
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String bean) {
		if (bean == null) {
			return null;
		}
		return (T) context.getBean(bean);
	}

	/**
	 * 获取 Spring Bean
	 *
	 * @param beanName 名称
	 * @param clazz    类
	 * @param <T>      泛型
	 * @return 对象
	 */
	public static <T> T getBean(String beanName, Class<T> clazz) {
		if (null == beanName || "".equals(beanName.trim())) {
			return null;
		}
		if (clazz == null) {
			return null;
		}
		return (T) context.getBean(beanName, clazz);
	}

	/**
	 * 获取上下文
	 *
	 * @return 上下文
	 */
	public static ApplicationContext getContext() {
		if (context == null) {
			return null;
		}
		return context;
	}

	/**
	 * 发布事件
	 *
	 * @param event 事件
	 */
	public static void publishEvent(ApplicationEvent event) {
		if (context == null) {
			return;
		}
		try {
			context.publishEvent(event);
		} catch (Exception ex) {
			log.error(ex.getMessage());
		}
	}

}
