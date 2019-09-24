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

package com.xkcoding.magic.log.support.logger;

import com.xkcoding.magic.log.support.publisher.LogEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.logging.LogLevel;

/**
 * <p>
 * 自定义日志记录
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/24 15:36
 */
@Slf4j
public class MagicLogger implements InitializingBean {

	/**
	 * WARN 级别日志
	 *
	 * @param prefix      日志前缀
	 * @param description 日志描述
	 */
	public void warn(String prefix, String description) {
		LogEventPublisher.publishCustomLogEvent(LogLevel.WARN, prefix, description);
	}

	/**
	 * DEBUG 级别日志
	 *
	 * @param prefix      日志前缀
	 * @param description 日志描述
	 */
	public void debug(String prefix, String description) {
		LogEventPublisher.publishCustomLogEvent(LogLevel.DEBUG, prefix, description);
	}

	/**
	 * INFO 级别日志
	 *
	 * @param prefix      日志前缀
	 * @param description 日志描述
	 */
	public void info(String prefix, String description) {
		LogEventPublisher.publishCustomLogEvent(LogLevel.INFO, prefix, description);
	}

	/**
	 * ERROR 级别日志
	 *
	 * @param prefix      日志前缀
	 * @param description 日志描述
	 */
	public void error(String prefix, String description) {
		LogEventPublisher.publishCustomLogEvent(LogLevel.ERROR, prefix, description);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info(">>>>>>>>>> MagicLogger 初始化成功!");
	}
}
