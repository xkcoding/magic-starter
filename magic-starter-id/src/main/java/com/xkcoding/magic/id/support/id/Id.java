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

package com.xkcoding.magic.id.support.id;

import com.xkcoding.magic.id.exception.IdException;
import com.xkcoding.magic.id.support.business.BusinessName;
import com.xkcoding.magic.id.support.prefix.Prefix;

/**
 * <p>
 * 主键生成接口
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/29 15:47
 */
public interface Id {
	/**
	 * 生成下一个主键
	 *
	 * @return 主键
	 * @throws IdException 主键生成异常
	 */
	long nextId() throws IdException;

	/**
	 * （根据业务名称）生成下一个主键
	 *
	 * @param businessName 业务名称
	 * @return 主键
	 * @throws IdException 主键生成异常
	 */
	long nextId(BusinessName businessName) throws IdException;

	/**
	 * 生成下一个主键(带格式)
	 *
	 * @return 主键(带格式)
	 * @throws IdException 主键生成异常
	 */
	String nextIdStr() throws IdException;

	/**
	 * （根据业务名称）生成下一个主键(带格式)
	 *
	 * @param businessName 业务名称
	 * @param prefix       前缀
	 * @return 主键(带格式)
	 * @throws IdException 主键生成异常
	 */
	String nextIdStr(BusinessName businessName, Prefix prefix) throws IdException;
}
