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

package com.xkcoding.magic.id.support.id.impl;

import com.xkcoding.magic.id.exception.IdException;
import com.xkcoding.magic.id.support.business.BusinessName;
import com.xkcoding.magic.id.support.id.RangeId;
import com.xkcoding.magic.id.support.prefix.Prefix;
import com.xkcoding.magic.id.support.range.RangeManager;
import com.xkcoding.magic.id.support.range.model.IdRange;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 区间范围主键默认生成器，根据业务名称自增
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/29 16:13
 */
public class DefaultRangeId implements RangeId {
	/**
	 * 获取区间是加一把独占锁防止资源冲突
	 */
	private final Lock lock = new ReentrantLock();

	/**
	 * 序列号区间管理器
	 */
	private RangeManager rangeManager;

	/**
	 * 当前序列号区间
	 */
	private volatile IdRange currentRange;

	/**
	 * 需要获取区间的业务名称
	 */
	private BusinessName businessName;

	/**
	 * 返回主键携带的前缀
	 */
	private Prefix prefix;

	@Override
	public long nextId() throws IdException {
		String name = businessName.get();

		//当前区间不存在，重新获取一个区间
		if (null == currentRange) {
			lock.lock();
			try {
				if (null == currentRange) {
					currentRange = rangeManager.nextRange(name);
				}
			} finally {
				lock.unlock();
			}
		}

		//当value值为-1时，表明区间的序列号已经分配完，需要重新获取区间
		long value = currentRange.getAndIncrement();
		if (value == -1) {
			lock.lock();
			try {
				for (; ; ) {
					if (currentRange.isOver()) {
						currentRange = rangeManager.nextRange(name);
					}

					value = currentRange.getAndIncrement();
					if (value == -1) {
						continue;
					}

					break;
				}
			} finally {
				lock.unlock();
			}
		}

		if (value < 0) {
			throw new IdException("Sequence value overflow, value = " + value);
		}

		return value;
	}

	/**
	 * 生成下一个主键(带格式)
	 *
	 * @return 主键(带格式)
	 * @throws IdException 主键生成异常
	 */
	@Override
	public String nextIdStr() throws IdException {
		return String.format("%s%05d", this.prefix.get(), nextId());
	}

	@Override
	public void setRangeManager(RangeManager rangeManager) {
		this.rangeManager = rangeManager;
	}

	/**
	 * 设置主键获取名称
	 *
	 * @param prefix 前缀
	 */
	@Override
	public void setPrefix(Prefix prefix) {
		this.prefix = prefix;
	}

	@Override
	public void setBusinessName(BusinessName businessName) {
		this.businessName = businessName;
	}
}
