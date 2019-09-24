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

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xkcoding.magic.core.tool.util.IpUtil;
import com.xkcoding.magic.core.tool.util.UrlUtil;
import com.xkcoding.magic.core.tool.util.WebUtil;
import com.xkcoding.magic.log.enums.LogType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.format.annotation.DateTimeFormat;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * <p>
 * 日志实体基类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/24 15:00
 */
@Data
@RequiredArgsConstructor
public abstract class AbstractLogModel {
	/**
	 * 日志类型
	 */
	private final LogType type;
	/**
	 * 日志级别
	 */
	private final LogLevel level;
	/**
	 * 操作IP
	 */
	private String remoteIp;
	/**
	 * 操作IP对应解析出来的地址
	 */
	private String ipAddress;
	/**
	 * User-Agent
	 */
	private String userAgent;
	/**
	 * 请求URI
	 */
	private String requestUri;
	/**
	 * 请求方式
	 */
	private String httpMethod;
	/**
	 * 执行类名
	 */
	private String className;
	/**
	 * 执行方法名
	 */
	private String methodName;
	/**
	 * 操作提交的数据
	 */
	private String params;
	/**
	 * 操作人
	 */
	private String operator;
	/**
	 * 操作时间
	 */
	@DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	private LocalDateTime operateTime;

	/**
	 * 包装日志
	 *
	 * @param t        日志
	 * @param request  请求
	 * @param operator 操作人
	 * @param <T>      具体日志
	 * @return 日志对象
	 */
	public <T extends AbstractLogModel> T wrapperLog(T t, HttpServletRequest request, String operator) {
		t.setRemoteIp(WebUtil.getIP(request));
		t.setIpAddress(IpUtil.getAddress(WebUtil.getIP(request)));
		t.setUserAgent(request.getHeader(WebUtil.USER_AGENT_HEADER));
		t.setRequestUri(UrlUtil.getPath(request.getRequestURI()));
		t.setHttpMethod(request.getMethod());
		t.setParams(WebUtil.getRequestParamString(request));
		t.setOperator(operator);
		t.setOperateTime(LocalDateTime.now());
		return t;
	}
}
