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

package com.xkcoding.magic.id.support.range.impl.redis;

import cn.hutool.core.util.StrUtil;
import com.xkcoding.magic.id.exception.IdException;
import com.xkcoding.magic.id.support.range.RangeManager;
import com.xkcoding.magic.id.support.range.model.IdRange;
import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.Jedis;

/**
 * Redis区间管理器
 *
 * @author xuan on 2018/5/8.
 */
@Getter
@Setter
public class RedisRangeManager implements RangeManager {

	/**
	 * 前缀防止key重复
	 */
	private final static String KEY_PREFIX = "x_sequence_";

	/**
	 * redis客户端
	 */
	private Jedis jedis;

	/**
	 * IP
	 */
	private String ip;
	/**
	 * PORT
	 */
	private Integer port;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 区间步长
	 */
	private int step = 1000;

	/**
	 * 区间起始位置，真实从stepStart+1开始
	 */
	private long stepStart = 0;

	/**
	 * 标记业务key是否存在，如果false，在取nextRange时，会取check一把
	 * 这个boolean只为提高性能，不用每次都取redis check
	 */
	private volatile boolean keyAlreadyExist;

	@Override
	public IdRange nextRange(String name) throws IdException {
		if (!keyAlreadyExist) {
			Boolean isExists = jedis.exists(getRealKey(name));
			if (!isExists) {
				//第一次不存在，进行初始化,setnx不存在就set，存在就忽略
				jedis.setnx(getRealKey(name), String.valueOf(stepStart));
			}
			keyAlreadyExist = true;
		}

		Long max = jedis.incrBy(getRealKey(name), step);
		long min = max - step + 1;
		return new IdRange(min, max);
	}

	@Override
	public void init() {
		checkParam();
		jedis = new Jedis(ip, port);
		if (StrUtil.isNotBlank(password)) {
			jedis.auth(password);
		}
	}

	private void checkParam() {
		if (isEmpty(ip)) {
			throw new IdException("[RedisRangeManager-checkParam] ip is empty.");
		}
		if (null == port) {
			throw new IdException("[RedisRangeManager-checkParam] port is null.");
		}
	}

	private String getRealKey(String name) {
		return KEY_PREFIX + name;
	}

	private boolean isEmpty(String str) {
		return null == str || str.trim().length() == 0;
	}

}
