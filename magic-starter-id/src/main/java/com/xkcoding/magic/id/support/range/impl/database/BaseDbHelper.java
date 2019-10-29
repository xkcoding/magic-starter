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

import javax.sql.DataSource;
import java.sql.*;

/**
 * <p>
 * 操作DB帮助类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/29 16:32
 */
abstract class BaseDbHelper {

	private static final long DELTA = 100000000L;

	private final static String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS #tableName(" + "id bigint(20) NOT NULL AUTO_INCREMENT," + "value bigint(20) NOT NULL," + "name varchar(32) NOT NULL," + "create_time DATETIME NOT NULL," + "update_time DATETIME NOT NULL," + "PRIMARY KEY (`id`),UNIQUE uk_name (`name`)" + ")";
	private final static String SQL_INSERT_RANGE = "INSERT IGNORE INTO #tableName(name,value,create_time,update_time)" + " VALUE(?,?,?,?)";
	private final static String SQL_UPDATE_RANGE = "UPDATE #tableName SET value=?,update_time=? WHERE name=? AND " + "value=?";
	private final static String SQL_SELECT_RANGE = "SELECT value FROM #tableName WHERE name=?";

	/**
	 * 创建表
	 *
	 * @param dataSource DB来源
	 * @param tableName  表名
	 */
	static void createTable(DataSource dataSource, String tableName) {

		Connection conn = null;
		Statement stmt = null;

		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate(SQL_CREATE_TABLE.replace("#tableName", tableName));
		} catch (SQLException e) {
			throw new IdException(e);
		} finally {
			closeQuietly(stmt);
			closeQuietly(conn);
		}
	}

	/**
	 * 插入数据区间
	 *
	 * @param dataSource DB来源
	 * @param tableName  表名
	 * @param name       区间名称
	 * @param stepStart  初始位置
	 */
	private static void insertRange(DataSource dataSource, String tableName, String name, long stepStart) {

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SQL_INSERT_RANGE.replace("#tableName", tableName));
			stmt.setString(1, name);
			stmt.setLong(2, stepStart);
			stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
			stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new IdException(e);
		} finally {
			closeQuietly(stmt);
			closeQuietly(conn);
		}
	}

	/**
	 * 更新区间，乐观策略
	 *
	 * @param dataSource DB来源
	 * @param tableName  表名
	 * @param newValue   更新新数据
	 * @param oldValue   更新旧数据
	 * @param name       区间名称
	 * @return 成功/失败
	 */
	static boolean updateRange(DataSource dataSource, String tableName, Long newValue, Long oldValue, String name) {

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SQL_UPDATE_RANGE.replace("#tableName", tableName));
			stmt.setLong(1, newValue);
			stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			stmt.setString(3, name);
			stmt.setLong(4, oldValue);
			int affectedRows = stmt.executeUpdate();
			return affectedRows > 0;
		} catch (SQLException e) {
			throw new IdException(e);
		} finally {
			closeQuietly(stmt);
			closeQuietly(conn);
		}
	}

	/**
	 * 查询区间，如果区间不存在，会新增一个区间，并返回null，由上层重新执行
	 *
	 * @param dataSource DB来源
	 * @param tableName  来源
	 * @param name       区间名称
	 * @param stepStart  初始位置
	 * @return 区间值
	 */
	static Long selectRange(DataSource dataSource, String tableName, String name, long stepStart) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long oldValue;

		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SQL_SELECT_RANGE.replace("#tableName", tableName));
			stmt.setString(1, name);

			rs = stmt.executeQuery();
			if (!rs.next()) {
				//没有此类型数据，需要初始化
				insertRange(dataSource, tableName, name, stepStart);
				return null;
			}
			oldValue = rs.getLong(1);

			if (oldValue < 0) {
				String msg = "Id value cannot be less than zero, value = " + oldValue + ", please check table sequence" + tableName;
				throw new IdException(msg);
			}

			if (oldValue > Long.MAX_VALUE - DELTA) {
				String msg = "Id value overflow, value = " + oldValue + ", please check table sequence" + tableName;
				throw new IdException(msg);
			}

			return oldValue;
		} catch (SQLException e) {
			throw new IdException(e);
		} finally {
			closeQuietly(rs);
			closeQuietly(stmt);
			closeQuietly(conn);
		}
	}

	private static void closeQuietly(AutoCloseable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			} catch (Throwable e) {
				//Ignore
			}
		}
	}

}
