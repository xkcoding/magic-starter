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

package com.xkcoding.magic.message.model.sms;

import com.google.common.collect.Maps;
import com.xkcoding.magic.message.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * <p>
 * 短信，参考文档：https://help.aliyun.com/document_detail/101414.html
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/11 13:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsMessage implements Message {
	/**
	 * 手机号，多个手机号码之间以英文逗号（,）分隔，上限为1000个手机号码
	 */
	private String mobile;
	/**
	 * 短信模板的参数
	 */
	private Map<String, String> params = Maps.newHashMap();
	/**
	 * 外部流水扩展字段
	 */
	private String outId;
	/**
	 * 短信签名名称
	 */
	private String signName;
	/**
	 * 短信模板ID
	 */
	private String templateCode;
}
