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

package com.xkcoding.magic.oss.support.qiniu;

import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.xkcoding.magic.core.tool.util.CollUtil;
import com.xkcoding.magic.core.tool.util.StrUtil;
import com.xkcoding.magic.oss.AbstractOssTemplate;
import com.xkcoding.magic.oss.autoconfigure.OssProperties;
import com.xkcoding.magic.oss.enums.OssType;
import com.xkcoding.magic.oss.model.OssFile;
import com.xkcoding.magic.oss.model.OssFileMetaInfo;
import com.xkcoding.magic.oss.support.rule.OssRule;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 七牛云操作，参考文档：https://developer.qiniu.com/kodo
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/12/31 17:22
 */
public class QiNiuCloudTemplate extends AbstractOssTemplate {
	private final Auth auth;
	private final UploadManager uploadManager;
	private final BucketManager bucketManager;
	private final OssProperties ossProperties;

	public QiNiuCloudTemplate(Auth auth, UploadManager uploadManager, BucketManager bucketManager, OssProperties ossProperties, OssRule ossRule) {
		super(ossProperties, ossRule, OssType.QINIU_CLOUD);
		this.auth = auth;
		this.uploadManager = uploadManager;
		this.bucketManager = bucketManager;
		this.ossProperties = ossProperties;
	}

	/**
	 * 创建 存储桶
	 *
	 * @param bucketName 存储桶名称
	 */
	@Override
	@SneakyThrows
	public void createBucket(String bucketName) {
		if (!bucketExists(bucketName)) {
			bucketManager.createBucket(getBucketName(bucketName), Zone.autoZone()
				.getRegion());
		}
	}

	/**
	 * 删除 存储桶
	 *
	 * @param bucketName 存储桶名称
	 */
	@Override
	public void deleteBucket(String bucketName) {
		// TODO: 七牛云 Java SDK 暂未提供该接口
	}

	/**
	 * 存储桶是否存在
	 *
	 * @param bucketName 存储桶名称
	 * @return boolean
	 */
	@Override
	@SneakyThrows
	public boolean bucketExists(String bucketName) {
		final String[] buckets = bucketManager.buckets();
		return CollUtil.contains(buckets, getBucketName(bucketName));
	}

	/**
	 * 拷贝文件
	 *
	 * @param sourceBucketName 源存储桶名称
	 * @param fileName         存储桶文件名称
	 * @param targetBucketName 目标存储桶名称
	 */
	@Override
	@SneakyThrows
	public void copyFile(String sourceBucketName, String fileName, String targetBucketName) {
		copyFile(sourceBucketName, fileName, targetBucketName, fileName);
	}

	/**
	 * 拷贝文件，重命名
	 *
	 * @param sourceBucketName 源存储桶名称
	 * @param fileName         存储桶文件名称
	 * @param targetBucketName 目标存储桶名称
	 * @param targetFileName   目标存储桶文件名称
	 */
	@Override
	@SneakyThrows
	public void copyFile(String sourceBucketName, String fileName, String targetBucketName, String targetFileName) {
		bucketManager.copy(getBucketName(sourceBucketName), fileName, getBucketName(targetBucketName), targetFileName);
	}

	/**
	 * 获取文件元信息
	 *
	 * @param fileName 存储桶文件名称
	 * @return 文件元信息
	 */
	@Override
	public OssFileMetaInfo getFileMetaInfo(String fileName) {
		return getFileMetaInfo(ossProperties.getQiniuCloud()
			.getBucketName(), fileName);
	}

	/**
	 * 获取文件元信息
	 *
	 * @param bucketName 存储桶名称
	 * @param fileName   存储桶文件名称
	 * @return 文件元信息
	 */
	@Override
	@SneakyThrows
	public OssFileMetaInfo getFileMetaInfo(String bucketName, String fileName) {
		final FileInfo fileInfo = bucketManager.stat(getBucketName(bucketName), fileName);
		OssFileMetaInfo metaInfo = new OssFileMetaInfo();
		metaInfo.setName(StrUtil.isNotBlank(fileInfo.key) ? fileInfo.key : fileName);
		metaInfo.setLink(getFileLink(metaInfo.getName()));
		metaInfo.setHash(fileInfo.hash);
		metaInfo.setLength(fileInfo.fsize);
		// 单位是 100 纳秒 所以除以 1000 * 10
		metaInfo.setUploadTime(new Date(fileInfo.putTime / (1000 * 10)));
		metaInfo.setContentType(fileInfo.mimeType);
		return metaInfo;
	}

	/**
	 * 获取文件相对路径
	 *
	 * @param fileName 存储桶对象名称
	 * @return 文件相对路径
	 */
	@Override
	public String getFilePath(String fileName) {
		return getBucketName().concat(StrUtil.SLASH)
			.concat(fileName);
	}

	/**
	 * 获取文件相对路径
	 *
	 * @param bucketName 存储桶名称
	 * @param fileName   存储桶对象名称
	 * @return 文件相对路径
	 */
	@Override
	public String getFilePath(String bucketName, String fileName) {
		return getBucketName(bucketName).concat(StrUtil.SLASH)
			.concat(fileName);
	}

	/**
	 * 获取文件地址
	 *
	 * @param fileName 存储桶对象名称
	 * @return 文件地址
	 */
	@Override
	public String getFileLink(String fileName) {
		return ossProperties.getQiniuCloud()
			.getEndpoint()
			.concat(StrUtil.SLASH)
			.concat(fileName);
	}

	/**
	 * 获取文件地址
	 *
	 * @param bucketName 存储桶名称
	 * @param fileName   存储桶对象名称
	 * @return 文件地址
	 */
	@Override
	public String getFileLink(String bucketName, String fileName) {
		return ossProperties.getQiniuCloud()
			.getEndpoint()
			.concat(StrUtil.SLASH)
			.concat(fileName);
	}

	/**
	 * 上传文件
	 *
	 * @param file 上传文件类
	 * @return 文件信息
	 */
	@Override
	public OssFile uploadFile(MultipartFile file) {
		return uploadFile(file.getOriginalFilename(), file);
	}

	/**
	 * 上传文件
	 *
	 * @param fileName 上传文件名
	 * @param file     上传文件类
	 * @return 文件信息
	 */
	@Override
	public OssFile uploadFile(String fileName, MultipartFile file) {
		return uploadFile(ossProperties.getQiniuCloud()
			.getBucketName(), fileName, file);
	}

	/**
	 * 上传文件
	 *
	 * @param bucketName 存储桶名称
	 * @param fileName   上传文件名
	 * @param file       上传文件类
	 * @return 文件信息
	 */
	@Override
	@SneakyThrows
	public OssFile uploadFile(String bucketName, String fileName, MultipartFile file) {
		return uploadFile(bucketName, fileName, file.getInputStream());
	}

	/**
	 * 上传文件
	 *
	 * @param fileName 存储桶对象名称
	 * @param stream   文件流
	 * @return 文件信息
	 */
	@Override
	public OssFile uploadFile(String fileName, InputStream stream) {
		return uploadFile(ossProperties.getQiniuCloud()
			.getBucketName(), fileName, stream);
	}

	/**
	 * 上传文件
	 *
	 * @param bucketName 存储桶名称
	 * @param fileName   存储桶对象名称
	 * @param stream     文件流
	 * @return 文件信息
	 */
	@Override
	public OssFile uploadFile(String bucketName, String fileName, InputStream stream) {
		return upload(bucketName, fileName, stream, false);
	}

	@SneakyThrows
	private OssFile upload(String bucketName, String fileName, InputStream stream, boolean cover) {
		// 创建存储桶
		createBucket(bucketName);
		// 获取 oss 存储文件名
		String key = getFileName(fileName);

		// 是否覆盖上传
		if (cover) {
			uploadManager.put(stream, key, getUploadToken(bucketName, key), null, null);
		} else {
			Response response = uploadManager.put(stream, key, getUploadToken(bucketName), null, null);
			int retry = 0;
			int retryCount = 5;
			while (response.needRetry() && retry < retryCount) {
				response = uploadManager.put(stream, key, getUploadToken(bucketName), null, null);
				retry++;
			}
		}

		OssFile ossFile = new OssFile();
		ossFile.setName(key);
		ossFile.setOriginalName(fileName);
		ossFile.setLink(getFileLink(bucketName, key));
		return ossFile;
	}

	/**
	 * 删除文件
	 *
	 * @param fileName 存储桶对象名称
	 */
	@Override
	@SneakyThrows
	public void deleteFile(String fileName) {
		bucketManager.delete(getBucketName(), fileName);
	}

	/**
	 * 删除文件
	 *
	 * @param bucketName 存储桶名称
	 * @param fileName   存储桶对象名称
	 */
	@Override
	@SneakyThrows
	public void deleteFile(String bucketName, String fileName) {
		bucketManager.delete(getBucketName(bucketName), fileName);
	}

	/**
	 * 批量删除文件
	 *
	 * @param fileNames 存储桶对象名称集合
	 */
	@Override
	public void deleteFiles(List<String> fileNames) {
		fileNames.forEach(this::deleteFile);
	}

	/**
	 * 批量删除文件
	 *
	 * @param bucketName 存储桶名称
	 * @param fileNames  存储桶对象名称集合
	 */
	@Override
	public void deleteFiles(String bucketName, List<String> fileNames) {
		fileNames.forEach(fileName -> deleteFile(bucketName, fileName));
	}

	/**
	 * 获取上传凭证，普通上传
	 *
	 * @param bucketName 存储桶名称
	 * @return Token
	 */
	private String getUploadToken(String bucketName) {
		return auth.uploadToken(getBucketName(bucketName));
	}

	/**
	 * 获取上传凭证，覆盖上传
	 *
	 * @param bucketName 存储桶名称
	 * @param key        文件名
	 * @return Token
	 */
	private String getUploadToken(String bucketName, String key) {
		return auth.uploadToken(getBucketName(bucketName), key);
	}
}
