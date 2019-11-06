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

package com.xkcoding.magic.id.support.range.impl.database;


import com.xkcoding.magic.id.exception.IdException;
import com.xkcoding.magic.id.support.range.RangeManager;
import com.xkcoding.magic.id.support.range.model.IdRange;
import lombok.Getter;
import lombok.Setter;

import javax.sql.DataSource;

/**
 * <p>
 * 数据库区间范围管理器
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/29 16:36
 */
@Getter
@Setter
public class DatabaseRangeManager implements RangeManager {
	/**
	 * 区间步长
	 */
	private int step = 1000;
	/**
	 * 区间起始位置，真实从stepStart+1开始
	 */
	private long stepStart = 0;
	/**
	 * 获取区间失败重试次数
	 */
	private int retryTimes = 100;
	/**
	 * DB来源
	 */
	private DataSource dataSource;
	/**
	 * 表名，默认range
	 */
	private String tableName = "range";

	@Override
	public IdRange nextRange(String name) throws IdException {
		if (isEmpty(name)) {
			throw new SecurityException("[DatabaseRangeManager-nextRange] name is empty.");
		}

		Long oldValue;
		long newValue;
		int times = getRetryTimes();
		for (int i = 0; i < times; i++) {
			oldValue = BaseDbHelper.selectRange(getDataSource(), getRealTableName(), name, getStepStart());

			if (null == oldValue) {
				//步长区间不存在，重试次数加 1，重新获取步长区间
				if (i == 0) {
					times++;
				}
				continue;
			}

			newValue = oldValue + getStep();

			if (BaseDbHelper.updateRange(getDataSource(), getRealTableName(), newValue, oldValue, name)) {
				return new IdRange(oldValue + 1, newValue);
			}
			//else 失败重试
		}

		throw new IdException("Retried too many times, retryTimes = " + getRetryTimes());
	}

	@Override
	public void init() {
		checkParam();
		BaseDbHelper.createTable(getDataSource(), getRealTableName());
	}

	private boolean isEmpty(String str) {
		return null == str || str.trim().length() == 0;
	}

	private String getRealTableName() {
		return getTableName();
	}

	private void checkParam() {
		if (step <= 0) {
			throw new IdException("[DatabaseRangeManager-checkParam] step must greater than 0.");
		}
		if (stepStart < 0) {
			throw new IdException("[DatabaseRangeManager-setStepStart] stepStart < 0.");
		}
		if (retryTimes <= 0) {
			throw new IdException("[DatabaseRangeManager-setRetryTimes] retryTimes must greater than 0.");
		}
		if (null == dataSource) {
			throw new IdException("[DatabaseRangeManager-setDataSource] dataSource is null.");
		}
		if (isEmpty(tableName)) {
			throw new IdException("[DatabaseRangeManager-setTableName] tableName is empty.");
		}
	}
}
