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

package com.xkcoding.magic.core.tool.api;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 统一分页返回结果封装
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019-07-12 16:23
 */
@Data
@Accessors(chain = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
	/**
	 * 当前页码
	 */
	private Integer currentPage;

	/**
	 * 每页条数
	 */
	private Integer pageSize;

	/**
	 * 总页数
	 */
	private Integer totalPage;

	/**
	 * 总记录数
	 */
	private final Long total;

	/**
	 * 当前页数据
	 */
	private final List<T> list;

	/**
	 * 构造器
	 */
	public PageResult() {
		this(0, Lists.newArrayList());
	}

	/**
	 * 构造器
	 *
	 * @param total 总记录数
	 * @param list  当前页数据
	 */
	public PageResult(int total, List<T> list) {
		this.total = (long) total;
		this.list = list;
	}

	/**
	 * 构造空对象
	 *
	 * @param <T> 泛型
	 * @return 空的分页对象
	 */
	public static <T> PageResult<T> ofEmpty() {
		return new PageResult<>(0, Lists.newArrayList());
	}

	/**
	 * 构造分页对象
	 *
	 * @param total 总记录数
	 * @param list  当前页数据
	 * @param <T>泛型
	 * @return 分页对象
	 */
	public static <T> PageResult<T> of(Number total, List<T> list) {
		return new PageResult<>(total.longValue(), list);
	}

}
