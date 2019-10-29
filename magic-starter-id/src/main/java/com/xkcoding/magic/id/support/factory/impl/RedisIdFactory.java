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

import com.xkcoding.magic.id.support.business.BusinessName;
import com.xkcoding.magic.id.support.factory.IdFactory;
import com.xkcoding.magic.id.support.id.Id;
import com.xkcoding.magic.id.support.id.impl.DefaultRangeId;
import com.xkcoding.magic.id.support.prefix.Prefix;
import com.xkcoding.magic.id.support.prefix.impl.DefaultPrefix;
import com.xkcoding.magic.id.support.range.impl.redis.RedisRangeManager;

/**
 * <p>
 * 基于Redis取步长，主键生成器工厂类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/29 17:10
 */
public class RedisIdFactory implements IdFactory {
	/**
	 * 连接redis的IP[必选]
	 */
	private String ip;
	/**
	 * 连接redis的port[必选]
	 */
	private int port;
	/**
	 * 业务名称[必选]
	 */
	private BusinessName businessName;
	/**
	 * 主键前缀[可选：默认为空]
	 */
	private Prefix prefix = new DefaultPrefix();
	/**
	 * 密码[可选]
	 */
	private String password;
	/**
	 * 获取range步长[可选，默认：1000]
	 */
	private int step = 1000;

	/**
	 * 序列号分配起始值[可选：默认：0]
	 */
	private long stepStart = 0;

	public static RedisIdFactory create() {
		return new RedisIdFactory();
	}

	public RedisIdFactory ip(String ip) {
		this.ip = ip;
		return this;
	}

	public RedisIdFactory port(int port) {
		this.port = port;
		return this;
	}

	public RedisIdFactory password(String password) {
		this.password = password;
		return this;
	}

	public RedisIdFactory step(int step) {
		this.step = step;
		return this;
	}

	public RedisIdFactory businessName(BusinessName businessName) {
		this.businessName = businessName;
		return this;
	}

	public RedisIdFactory prefix(Prefix prefix) {
		this.prefix = prefix;
		return this;
	}

	public RedisIdFactory stepStart(long stepStart) {
		this.stepStart = stepStart;
		return this;
	}

	/**
	 * 返回一个主键生成器对象
	 *
	 * @return 主键生成器
	 */
	@Override
	public Id getInstance() {
		//利用Redis获取区间管理器
		RedisRangeManager redisRangeManager = new RedisRangeManager();
		redisRangeManager.setIp(this.ip);
		redisRangeManager.setPort(this.port);
		redisRangeManager.setPassword(this.password);
		redisRangeManager.setStep(this.step);
		redisRangeManager.setStepStart(stepStart);
		redisRangeManager.init();
		//构建序列号生成器
		DefaultRangeId defaultRangeId = new DefaultRangeId();
		defaultRangeId.setBusinessName(this.businessName);
		defaultRangeId.setPrefix(prefix);
		defaultRangeId.setRangeManager(redisRangeManager);
		return defaultRangeId;
	}
}
