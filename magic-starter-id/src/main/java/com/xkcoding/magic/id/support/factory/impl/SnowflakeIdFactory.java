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

package com.xkcoding.magic.id.support.factory.impl;

import com.xkcoding.magic.id.support.factory.IdFactory;
import com.xkcoding.magic.id.support.id.Id;
import com.xkcoding.magic.id.support.id.impl.SnowFlakeId;
import com.xkcoding.magic.id.support.prefix.Prefix;
import com.xkcoding.magic.id.support.prefix.impl.DefaultPrefix;

/**
 * <p>
 * 雪花算法生成器，主键生成器工厂类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/29 16:48
 */
public class SnowflakeIdFactory implements IdFactory {
	/**
	 * 数据中心ID，值的范围在[0,31]之间，一般可以设置机房的IDC[必选]
	 */
	private long dataCenterId;
	/**
	 * 工作机器ID，值的范围在[0,31]之间，一般可以设置机器编号[必选]
	 */
	private long workerId;
	/**
	 * 前缀，可选，默认为空
	 */
	private Prefix prefix = new DefaultPrefix();

	public static SnowflakeIdFactory create() {
		return new SnowflakeIdFactory();
	}

	public SnowflakeIdFactory dataCenterId(long dataCenterId) {
		this.dataCenterId = dataCenterId;
		return this;
	}

	public SnowflakeIdFactory workerId(long workerId) {
		this.workerId = workerId;
		return this;
	}

	public SnowflakeIdFactory prefix(Prefix prefix) {
		this.prefix = prefix;
		return this;
	}

	/**
	 * 返回一个主键生成器对象
	 *
	 * @return 主键生成器
	 */
	@Override
	public Id getInstance() {
		return new SnowFlakeId(this.dataCenterId, this.workerId, this.prefix);
	}
}
