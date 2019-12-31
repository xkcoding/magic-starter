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

package com.xkcoding.magic.oss.support.rule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.xkcoding.magic.core.tool.util.StrUtil;

/**
 * <p>
 * 对象存储规则
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/12/31 11:05
 */
public interface OssRule {
	/**
	 * 生成存储桶名称规则，默认传入的存储桶名称
	 *
	 * @param bucketName 存储桶名称
	 * @return 存储桶名称
	 */
	default String bucketName(String bucketName) {
		return bucketName;
	}

	/**
	 * 生成文件名规则，默认 "upload/2019-12-31/5e9ec298963a4eef8c59d379d02e8a70.png"
	 *
	 * @param originalFilename 文件名
	 * @return 文件名
	 */
	default String fileName(String originalFilename) {
		return "upload" + StrUtil.SLASH + DateUtil.today() + StrUtil.SLASH + IdUtil.fastSimpleUUID() + StrUtil.DOT + StrUtil.fileExt(originalFilename);
	}
}
