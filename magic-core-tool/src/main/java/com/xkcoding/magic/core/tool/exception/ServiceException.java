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

package com.xkcoding.magic.core.tool.exception;

import com.xkcoding.magic.core.tool.api.IResultCode;
import com.xkcoding.magic.core.tool.enums.CommonResultCode;
import lombok.Getter;

/**
 * <p>
 * 通用业务异常
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/19 22:21
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 2359767895161832954L;

	@Getter
	private final IResultCode resultCode;
	@Getter
	private final Object data;

	public ServiceException(String message) {
		super(message);
		this.resultCode = CommonResultCode.INTERNAL_SERVER_ERROR;
		data = null;
	}

	public ServiceException(IResultCode resultCode) {
		super(resultCode.getMessage());
		this.resultCode = resultCode;
		data = null;
	}

	public ServiceException(IResultCode resultCode, Object data) {
		super(resultCode.getMessage());
		this.resultCode = resultCode;
		this.data = data;
	}

	public ServiceException(IResultCode resultCode, Throwable cause) {
		super(cause);
		this.resultCode = resultCode;
		data = null;
	}

}
