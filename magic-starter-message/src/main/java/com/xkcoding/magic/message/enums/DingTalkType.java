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

package com.xkcoding.magic.message.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 钉钉消息类型枚举类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/11 13:46
 */
@Getter
@AllArgsConstructor
public enum DingTalkType {
	/**
	 * text类型
	 */
	TEXT("text"),
	/**
	 * link类型
	 */
	LINK("link"),
	/**
	 * markdown类型
	 */
	MARKDOWN("markdown"),
	/**
	 * 整体跳转ActionCard类型
	 */
	ACTIONCARD_WHOLE("actionCard"),
	/**
	 * 独立跳转ActionCard类型
	 */
	ACTIONCARD_SINGLE("actionCard"),
	/**
	 * FeedCard类型
	 */
	FEEDCARD("feedCard");

	/**
	 * 类型
	 */
	private String type;
	
}
