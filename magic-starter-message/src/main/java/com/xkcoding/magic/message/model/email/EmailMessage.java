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

package com.xkcoding.magic.message.model.email;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xkcoding.magic.message.enums.EmailType;
import com.xkcoding.magic.message.model.Message;
import com.xkcoding.magic.message.model.email.support.EmailAttachment;
import com.xkcoding.magic.message.model.email.support.EmailStaticResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 邮件
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/11 13:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailMessage implements Message {
	/**
	 * 构建一个简单邮件
	 */
	public static EmailMessage simpleEmail() {
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.setEmailType(EmailType.SIMPLE);
		return emailMessage;
	}

	/**
	 * 构建一个复杂邮件
	 */
	public static EmailMessage mimeEmail() {
		EmailMessage emailMessage = new EmailMessage();
		emailMessage.setEmailType(EmailType.MIME);
		return emailMessage;
	}

	/**
	 * 邮件类型
	 */
	private EmailType emailType;
	/**
	 * 收件人
	 */
	private List<String> tos = Lists.newArrayList();
	/**
	 * 发件人
	 */
	private String from;
	/**
	 * 主题
	 */
	private String subject;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 模板名
	 */
	private String template;
	/**
	 * 模板变量
	 */
	private Map<String, Object> params = Maps.newHashMap();
	/**
	 * 附件列表
	 */
	private List<EmailAttachment> attachments = Lists.newArrayList();
	/**
	 * 静态资源列表
	 */
	private List<EmailStaticResource> staticResources = Lists.newArrayList();
	/**
	 * 抄送
	 */
	private List<String> ccs = Lists.newArrayList();
}
