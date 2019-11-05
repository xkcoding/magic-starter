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

package com.xkcoding.magic.core.tool.support.wrapper.page.pagehelper;

import com.github.pagehelper.Page;
import com.xkcoding.magic.core.tool.api.PageResult;

/**
 * <p>
 * PageHelper 分页结果集包装
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/11/6 10:45
 */
public class PageResultWrapper {
	/**
	 * 构造分页对象
	 *
	 * @param pageData 当前页数据
	 * @param <T>泛型
	 * @return 分页对象
	 */
	public static <T> PageResult<T> wrapper(Page<T> pageData) {
		return new PageResult<>(pageData.getPageNum(), pageData.getPageSize(), pageData.getPages(), pageData.getTotal(), pageData.getResult());
	}
}
