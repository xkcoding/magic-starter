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

package com.xkcoding.magic.oss.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 对象存储类型枚举，备用
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/12/28 16:16
 */
@Getter
@AllArgsConstructor
public enum OssType {
	/**
	 * 阿里云 OSS
	 */
	ALI_OSS,
	/**
	 * 腾讯云 COS
	 */
	TENCENT_COS,
	/**
	 * 七牛云
	 */
	QINIU_CLOUD,
	/**
	 * MinIO
	 */
	MINIO,
	/**
	 * AwsS3
	 */
	AWSS3;
}
