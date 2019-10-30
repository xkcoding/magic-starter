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

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.StrUtil;
import com.xkcoding.magic.id.exception.IdException;
import com.xkcoding.magic.id.support.business.BusinessName;
import com.xkcoding.magic.id.support.id.Id;
import com.xkcoding.magic.id.support.prefix.Prefix;
import com.xkcoding.magic.id.support.prefix.impl.DefaultPrefix;

/**
 * <p>
 * 雪花算法 Id 生成器
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/29 15:55
 */
public class SnowFlakeId implements Id {
	/**
	 * Thu, 04 Nov 2010 01:42:54 GMT
	 */
	private final long EPOCH = 1288834974657L;
	private final long WORKER_ID_BITS = 5L;
	private final long DATA_CENTER_ID_BITS = 5L;

	/**
	 * 序列号12位
	 */
	private final long sequenceBits = 12L;
	/**
	 * 机器节点左移12位
	 */
	private final long workerIdShift = sequenceBits;
	/**
	 * 数据中心节点左移17位
	 */
	private final long dataCenterIdShift = sequenceBits + WORKER_ID_BITS;
	/**
	 * 时间毫秒数左移22位
	 */
	private final long timestampLeftShift = sequenceBits + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

	private long workerId;
	private long dataCenterId;
	/**
	 * 返回主键携带的前缀
	 */
	private Prefix prefix;

	private long sequence = 0L;
	private long lastTimestamp = -1L;
	private boolean useSystemClock;

	/**
	 * 构造
	 *
	 * @param workerId     终端ID
	 * @param dataCenterId 数据中心ID
	 */
	public SnowFlakeId(long workerId, long dataCenterId) {
		this(workerId, dataCenterId, new DefaultPrefix());
	}

	/**
	 * 构造
	 *
	 * @param workerId     终端ID
	 * @param dataCenterId 数据中心ID
	 * @param prefix       前缀
	 */
	public SnowFlakeId(long workerId, long dataCenterId, Prefix prefix) {
		this(workerId, dataCenterId, false, prefix);
	}

	/**
	 * 构造
	 *
	 * @param workerId         终端ID
	 * @param dataCenterId     数据中心ID
	 * @param isUseSystemClock 是否使用{@link SystemClock} 获取当前时间戳
	 * @param prefix           前缀
	 */
	public SnowFlakeId(long workerId, long dataCenterId, boolean isUseSystemClock, Prefix prefix) {
		// 最大支持机器节点数0~31，一共32个
		long maxWorkerId = ~(-1L << WORKER_ID_BITS);
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IdException(StrUtil.format("worker Id can't be greater than {} or less than 0", maxWorkerId));
		}
		// 最大支持数据中心节点数0~31，一共32个
		long maxDataCenterId = ~(-1L << DATA_CENTER_ID_BITS);
		if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
			throw new IdException(StrUtil.format("data center Id can't be greater than {} or less than 0", maxDataCenterId));
		}
		this.workerId = workerId;
		this.dataCenterId = dataCenterId;
		this.useSystemClock = isUseSystemClock;
		this.prefix = prefix;
	}

	/**
	 * 根据Snowflake的ID，获取机器id
	 *
	 * @param id snowflake算法生成的id
	 * @return 所属机器的id
	 */
	public long getWorkerId(long id) {
		return id >> workerIdShift & ~(-1L << WORKER_ID_BITS);
	}

	/**
	 * 根据Snowflake的ID，获取数据中心id
	 *
	 * @param id snowflake算法生成的id
	 * @return 所属数据中心
	 */
	public long getDataCenterId(long id) {
		return id >> dataCenterIdShift & ~(-1L << DATA_CENTER_ID_BITS);
	}

	/**
	 * 根据Snowflake的ID，获取生成时间
	 *
	 * @param id snowflake算法生成的id
	 * @return 生成的时间
	 */
	public long getGenerateDateTime(long id) {
		return (id >> timestampLeftShift & ~(-1L << 41L)) + EPOCH;
	}

	/**
	 * 循环等待下一个时间
	 *
	 * @param lastTimestamp 上次记录的时间
	 * @return 下一个时间
	 */
	private long tilNextMillis(long lastTimestamp) {
		long timestamp = genTime();
		while (timestamp <= lastTimestamp) {
			timestamp = genTime();
		}
		return timestamp;
	}

	/**
	 * 生成时间戳
	 *
	 * @return 时间戳
	 */
	private long genTime() {
		return this.useSystemClock ? SystemClock.now() : System.currentTimeMillis();
	}

	/**
	 * 生成下一个主键
	 *
	 * @return 主键
	 * @throws IdException 主键生成异常
	 */
	@Override
	public long nextId() throws IdException {
		return nextId(null);
	}

	/**
	 * （根据业务名称）生成下一个主键
	 *
	 * @param businessName 业务名称
	 * @return 主键
	 * @throws IdException 主键生成异常
	 */
	@Override
	public synchronized long nextId(BusinessName businessName) throws IdException {
		long timestamp = genTime();
		if (timestamp < lastTimestamp) {
			// 如果服务器时间有问题(时钟后退) 报错。
			throw new IllegalStateException(StrUtil.format("Clock moved backwards. Refusing to generate id for {}ms", lastTimestamp - timestamp));
		}
		if (lastTimestamp == timestamp) {
			// 4095
			long sequenceMask = ~(-1L << sequenceBits);
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;

		return ((timestamp - EPOCH) << timestampLeftShift) | (dataCenterId << dataCenterIdShift) | (workerId << workerIdShift) | sequence;
	}

	/**
	 * 生成下一个主键(带格式)
	 *
	 * @return 主键(带格式)
	 * @throws IdException 主键生成异常
	 */
	@Override
	public String nextIdStr() throws IdException {
		return nextIdStr(null, this.prefix);
	}

	/**
	 * （根据业务名称）生成下一个主键(带格式)
	 *
	 * @param businessName 业务名称
	 * @param prefix       前缀
	 * @return 主键(带格式)
	 * @throws IdException 主键生成异常
	 */
	@Override
	public String nextIdStr(BusinessName businessName, Prefix prefix) throws IdException {
		return prefix.get() + nextId(businessName);
	}
}
