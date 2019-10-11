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

package com.xkcoding.magic.message.constants;

/**
 * <p>
 * 消息常量池
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/11 14:10
 */
public interface MessageConstants {
	/**
	 * 邮箱模板引擎
	 */
	String EMAIL_TEMPLATE_ENGINE_BEAN = "emailTemplateEngine";
	/**
	 * 邮箱模板解析器
	 */
	String EMAIL_TEMPLATE_RESOLVER_BEAN = "emailTemplateResolver";
	/**
	 * 短信产品
	 */
	String SMS_PRODUCT = "Dysmsapi";
	/**
	 * 短信域名
	 */
	String SMS_ENDPOINT = "dysmsapi.aliyuncs.com";
	/**
	 * 短信发送器 Bean 名称
	 */
	String BEAN_NAME_SMS = "smsMessageSender";
	/**
	 * 邮件发送器 Bean 名称
	 */
	String BEAN_NAME_EMAIL = "emailMessageSender";
	/**
	 * 钉钉发送器 Bean 名称
	 */
	String BEAN_NAME_DINGTALK = "dingTalkMessageSender";
	/**
	 * 短信成功状态码
	 */
	String SMS_SUCCESS_CODE = "OK";
	/**
	 * 钉钉消息默认超时时间
	 */
	int DINGTALK_DEFAULT_TIMEOUT = 5000;
}
