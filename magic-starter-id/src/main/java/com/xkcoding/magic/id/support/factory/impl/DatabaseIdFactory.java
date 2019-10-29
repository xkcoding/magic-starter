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
import com.xkcoding.magic.id.support.range.impl.database.DatabaseRangeManager;

import javax.sql.DataSource;

/**
 * <p>
 * 基于Database取步长，主键生成器工厂类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/29 16:51
 */
public class DatabaseIdFactory implements IdFactory {
	/**
	 * 数据库数据源[必选]
	 */
	private DataSource dataSource;
	/**
	 * 业务名称[必选]
	 */
	private BusinessName businessName;
	/**
	 * 主键前缀[可选：默认为空]
	 */
	private Prefix prefix = new DefaultPrefix();

	/**
	 * 存放序列号步长的表[可选：默认：sequence]
	 */
	private String tableName = "sequence";
	/**
	 * 并发是数据使用了乐观策略，这个是失败重试的次数[可选：默认：100]
	 */
	private int retryTimes = 100;
	/**
	 * 获取range步长[可选：默认：1000]
	 */
	private int step = 1000;

	/**
	 * 序列号分配起始值[可选：默认：0]
	 */
	private long stepStart = 0;

	public static DatabaseIdFactory create() {
		return new DatabaseIdFactory();
	}

	public DatabaseIdFactory dataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		return this;
	}

	public DatabaseIdFactory tableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public DatabaseIdFactory retryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
		return this;
	}

	public DatabaseIdFactory step(int step) {
		this.step = step;
		return this;
	}

	public DatabaseIdFactory businessName(BusinessName businessName) {
		this.businessName = businessName;
		return this;
	}

	public DatabaseIdFactory prefix(Prefix prefix) {
		this.prefix = prefix;
		return this;
	}

	public DatabaseIdFactory stepStart(long stepStart) {
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
		//利用DB获取区间管理器
		DatabaseRangeManager databaseRangeManager = new DatabaseRangeManager();
		databaseRangeManager.setDataSource(this.dataSource);
		databaseRangeManager.setTableName(this.tableName);
		databaseRangeManager.setRetryTimes(this.retryTimes);
		databaseRangeManager.setStep(this.step);
		databaseRangeManager.setStepStart(stepStart);
		databaseRangeManager.init();
		//构建序列号生成器
		DefaultRangeId defaultRangeId = new DefaultRangeId();
		defaultRangeId.setBusinessName(this.businessName);
		defaultRangeId.setPrefix(this.prefix);
		defaultRangeId.setRangeManager(databaseRangeManager);
		return defaultRangeId;
	}
}
