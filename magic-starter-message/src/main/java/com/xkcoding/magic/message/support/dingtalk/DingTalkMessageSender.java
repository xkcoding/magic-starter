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

package com.xkcoding.magic.message.support.dingtalk;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.xkcoding.magic.core.tool.util.AssertUtil;
import com.xkcoding.magic.message.autoconfigure.MessageProperties;
import com.xkcoding.magic.message.constants.MessageConstants;
import com.xkcoding.magic.message.model.dingtalk.AbstractDingTalkMessage;
import com.xkcoding.magic.message.support.AbstractMessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 钉钉发送器
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/10/11 14:51
 */
@Slf4j
@RequiredArgsConstructor
public class DingTalkMessageSender extends AbstractMessageSender<AbstractDingTalkMessage> {
	private final MessageProperties messageProperties;

	/**
	 * 数据校验
	 *
	 * @param message 消息实体
	 */
	@Override
	public void validate(AbstractDingTalkMessage message) {
		String webhook = messageProperties.getDingtalk().getWebhook();
		AssertUtil.isBlank(webhook, "钉钉配置错误，webhook为空");
	}

	/**
	 * 业务处理
	 *
	 * @param message 消息实体
	 * @return boolean
	 */
	@Override
	public boolean process(AbstractDingTalkMessage message) {
		String webhook = messageProperties.getDingtalk().getWebhook();
		try {
			String result = HttpUtil.post(webhook, JSONUtil.toJsonPrettyStr(message), MessageConstants.DINGTALK_DEFAULT_TIMEOUT);
			log.debug("钉钉提醒成功,报文响应: {}", result);
			return true;
		} catch (Exception e) {
			log.error("钉钉消息发送异常！", e);
			return false;
		}
	}

	/**
	 * 失败处理
	 *
	 * @param message 消息实体
	 */
	@Override
	public void fail(AbstractDingTalkMessage message) {
		// do nothing
	}
}
