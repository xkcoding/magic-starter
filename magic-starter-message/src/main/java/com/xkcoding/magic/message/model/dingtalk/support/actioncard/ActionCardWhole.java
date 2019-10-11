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

package com.xkcoding.magic.message.model.dingtalk.support.actioncard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 整体跳转 ActionCard 类型
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/11 14:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionCardWhole {
	/**
	 * 首屏会话透出的展示内容
	 */
	private String title;
	/**
	 * markdown格式的消息
	 */
	private String text;
	/**
	 * 单个按钮的方案。(设置此项和singleURL后btns无效)
	 */
	private String singleTitle;
	/**
	 * 点击singleTitle按钮触发的URL
	 */
	private String singleURL;
	/**
	 * 0-按钮竖直排列，1-按钮横向排列
	 */
	private String btnOrientation;
	/**
	 * 0-正常发消息者头像，1-隐藏发消息者头像
	 */
	private String hideAvatar;
}
