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

package com.xkcoding.magic.secure.support;

import com.xkcoding.magic.secure.enums.HttpMethod;
import com.xkcoding.magic.secure.model.Rule;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 鉴权规则注册中心
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/9/19 14:10
 */
@Data
public class SecureRuleRegistry {
	private List<Rule> ruleList = new ArrayList<>();
	private List<String> whiteList = new ArrayList<>();

	/**
	 * 添加鉴权规则
	 *
	 * @param path       路径
	 * @param method     请求方法
	 * @param expression 权限表达式
	 * @return 当前对象
	 */
	public SecureRuleRegistry addRule(String path, HttpMethod method, String expression) {
		ruleList.add(new Rule(path, method, expression));
		return this;
	}

	/**
	 * 添加排除路径
	 *
	 * @param path 路径
	 * @return 当前对象
	 */
	public SecureRuleRegistry exclude(String path) {
		whiteList.add(path);
		return this;
	}
}
