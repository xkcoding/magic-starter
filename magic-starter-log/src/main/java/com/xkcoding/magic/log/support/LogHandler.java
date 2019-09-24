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

package com.xkcoding.magic.log.support;

import com.xkcoding.magic.log.model.CustomLogModel;
import com.xkcoding.magic.log.model.ErrorLogModel;
import com.xkcoding.magic.log.model.OperateLogModel;

/**
 * <p>
 * 日志处理接口
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/24 16:11
 */
public interface LogHandler {
	/**
	 * 处理操作日志
	 *
	 * @param logModel 操作日志实体
	 */
	default void handleOperateLog(OperateLogModel logModel) {
	}

	/**
	 * 处理自定义日志
	 *
	 * @param logModel 自定义日志实体
	 */
	default void handleCustomLog(CustomLogModel logModel) {
	}

	/**
	 * 处理错误日志
	 *
	 * @param logModel 错误日志实体
	 */
	default void handleErrorLog(ErrorLogModel logModel) {
	}
}
