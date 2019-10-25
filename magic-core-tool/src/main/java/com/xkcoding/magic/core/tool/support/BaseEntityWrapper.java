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

package com.xkcoding.magic.core.tool.support;

import com.xkcoding.magic.core.tool.api.PageResult;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * VO对象包装器
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/25 15:06
 */
public abstract class BaseEntityWrapper<E, V> {
	/**
	 * 单个实体类包装VO对象
	 *
	 * @param entity 实体对象
	 * @return VO 对象
	 */
	public abstract V toVO(E entity);

	/**
	 * 实体类集合包装VO集合
	 *
	 * @param list 集合
	 * @return VO 集合对象
	 */
	public List<V> listVO(List<E> list) {
		return list.stream().map(this::toVO).collect(Collectors.toList());
	}

	/**
	 * 分页实体类集合包装VO分页
	 *
	 * @param pageResult 分页数据
	 * @return VO 分页对象
	 */
	public PageResult<V> pageVO(PageResult<E> pageResult) {
		List<V> records = listVO(pageResult.getList());
		return PageResult.of(pageResult.getTotal(), records);
	}
}
