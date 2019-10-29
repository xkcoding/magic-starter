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

package com.xkcoding.magic.id.support.range.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * 主键区间对象模型
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/29 16:23
 */
public class IdRange {

	/**
	 * 区间的序列号开始值
	 */
	private final long min;
	/**
	 * 区间的序列号结束值
	 */
	private final long max;
	/**
	 * 区间的序列号当前值
	 */
	private final AtomicLong value;
	/**
	 * 区间的序列号是否分配完毕，每次分配完毕就会去重新获取一个新的区间
	 */
	private volatile boolean over = false;

	public IdRange(long min, long max) {
		this.min = min;
		this.max = max;
		this.value = new AtomicLong(min);
	}

	/**
	 * 返回并递增下一个序列号
	 *
	 * @return 下一个序列号，如果返回-1表示序列号分配完毕
	 */
	public long getAndIncrement() {
		long currentValue = value.getAndIncrement();
		if (currentValue > max) {
			over = true;
			return -1;
		}

		return currentValue;
	}

	public long getMin() {
		return min;
	}

	public long getMax() {
		return max;
	}

	public boolean isOver() {
		return over;
	}

	public void setOver(boolean over) {
		this.over = over;
	}

	@Override
	public String toString() {
		return "max: " + max + ", min: " + min + ", value: " + value;
	}

}
