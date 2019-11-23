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

package com.xkcoding.magic.message.support.sms;

import cn.hutool.json.JSONUtil;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.xkcoding.magic.core.tool.util.AssertUtil;
import com.xkcoding.magic.core.tool.util.StrUtil;
import com.xkcoding.magic.message.autoconfigure.MessageProperties;
import com.xkcoding.magic.message.constants.MessageConstants;
import com.xkcoding.magic.message.model.sms.SmsMessage;
import com.xkcoding.magic.message.support.AbstractMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 短信发送器
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/11 14:27
 */
@Slf4j
@RequiredArgsConstructor
public class SmsMessageSender extends AbstractMessageSender<SmsMessage> {
	private final MessageProperties messageProperties;

	/**
	 * 数据校验
	 *
	 * @param message 消息实体
	 */
	@Override
	protected void validate(SmsMessage message) {
		AssertUtil.isBlank(message.getMobile(), "手机号不能为空");
		int size = StrUtil.splitTrim(message.getMobile(), StrUtil.COMMA).size();
		AssertUtil.isNotBetween(size, 0, 1000, "手机号数量必须在 0-1000 个之内");
	}

	/**
	 * 业务处理
	 *
	 * @param message 消息实体
	 * @return boolean
	 */
	@Override
	protected boolean process(SmsMessage message) {
		//可自助调整超时时间
		System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
		System.setProperty("sun.net.client.defaultReadTimeout", "10000");

		//初始化acsClient,暂不支持region化
		IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", messageProperties.getSms().getAccessKey(), messageProperties.getSms().getSecretKey());
		DefaultProfile.addEndpoint("cn-hangzhou", MessageConstants.SMS_PRODUCT, MessageConstants.SMS_ENDPOINT);
		IAcsClient acsClient = new DefaultAcsClient(profile);

		//组装请求对象-具体描述见控制台-文档部分内容
		SendSmsRequest request = new SendSmsRequest();
		//必填:待发送手机号
		request.setPhoneNumbers(message.getMobile());

		//必填:短信签名-可在短信控制台中找到
		request.setSignName(message.getSignName());

		//必填:短信模板-可在短信控制台中找到
		request.setTemplateCode(messageProperties.getSms().getChannels().get(message.getTemplateCode()));

		//可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"
		request.setTemplateParam(JSONUtil.toJsonStr(message.getParams()));
		request.setOutId(message.getOutId());

		//此处可能会抛出异常，注意catch
		boolean result = false;
		try {
			SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
			if (StrUtil.equals(MessageConstants.SMS_SUCCESS_CODE, sendSmsResponse.getCode())) {
				log.debug("短信发送完毕，手机号：{}，返回状态：{}", message.getMobile(), sendSmsResponse.getCode());
				result = true;
			} else {
				log.error("短信发送异常，手机号：{}，返回状态：{}", message.getMobile(), sendSmsResponse.getCode());
			}
		} catch (ClientException e) {
			log.error("短信发送异常！", e);
		}
		return result;
	}

	/**
	 * 失败处理
	 *
	 * @param message 消息实体
	 */
	@Override
	protected void fail(SmsMessage message) {
		log.error("短信发送失败，当前短信签名：{}，当前短信模板：{}，当前手机号：{}", message.getSignName(), message.getTemplateCode(), message.getMobile());
	}
}
