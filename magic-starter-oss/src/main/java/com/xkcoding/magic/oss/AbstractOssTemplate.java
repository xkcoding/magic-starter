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

package com.xkcoding.magic.oss;

import com.xkcoding.magic.core.tool.constants.MagicConsts;
import com.xkcoding.magic.oss.autoconfigure.OssProperties;
import com.xkcoding.magic.oss.enums.OssType;
import com.xkcoding.magic.oss.support.rule.OssRule;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Oss 抽象类
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2020/1/2 10:32
 */
@RequiredArgsConstructor
public abstract class AbstractOssTemplate implements OssTemplate {
	private final OssProperties ossProperties;
	private final OssRule ossRule;
	private final OssType ossType;

	/**
	 * 根据配置文件获取存储桶名称
	 *
	 * @return 存储桶名称
	 */
	protected String getBucketName() {
		String defaultBucketName;
		switch (ossType) {
			case ALI_OSS:
				defaultBucketName = ossProperties.getAliOss()
					.getBucketName();
				break;
			case TENCENT_COS:
				defaultBucketName = ossProperties.getTencentCos()
					.getBucketName();
				break;
			case QINIU_CLOUD:
				defaultBucketName = ossProperties.getQiniuCloud()
					.getBucketName();
				break;
			case MINIO:
				defaultBucketName = ossProperties.getMinIo()
					.getBucketName();
				break;
			default:
				defaultBucketName = MagicConsts.MAGIC;
				break;
		}
		return getBucketName(defaultBucketName);
	}

	/**
	 * 根据规则生成存储桶名称
	 *
	 * @param bucketName 存储桶名称
	 * @return 存储桶名称
	 */
	protected String getBucketName(String bucketName) {
		return ossRule.bucketName(bucketName);
	}

	/**
	 * 根据规则生成文件名称
	 *
	 * @param fileName 文件名称
	 * @return 文件名称
	 */
	protected String getFileName(String fileName) {
		return ossRule.fileName(fileName);
	}
}
