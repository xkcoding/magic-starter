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

package com.xkcoding.magic.oss.support.tencent;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectResult;
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
import java.util.List;

/**
 * <p>
 * 腾讯云 COS 操作，参考文档：https://cloud.tencent.com/document/product/436/7751
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/12/31 17:23
 */
public class TencentCosTemplate extends AbstractOssTemplate {
	private final COSClient cosClient;
	private final OssProperties ossProperties;

	public TencentCosTemplate(COSClient cosClient, OssProperties ossProperties, OssRule ossRule) {
		super(ossProperties, ossRule, OssType.TENCENT_COS);
		this.cosClient = cosClient;
		this.ossProperties = ossProperties;
	}

	/**
	 * 根据配置文件获取存储桶名称，腾讯 COS 存储桶后必须添加 appId
	 *
	 * @return 存储桶名称
	 */
	@Override
	protected String getBucketName() {
		return super.getBucketName()
			.concat(StrUtil.DASHED)
			.concat(ossProperties.getTencentCos()
				.getAppId());
	}

	/**
	 * 根据规则生成存储桶名称，腾讯 COS 存储桶后必须添加 appId
	 *
	 * @param bucketName 存储桶名称
	 * @return 存储桶名称
	 */
	@Override
	protected String getBucketName(String bucketName) {
		return super.getBucketName(bucketName)
			.concat(StrUtil.DASHED)
			.concat(ossProperties.getTencentCos()
				.getAppId());
	}

	/**
	 * 创建 存储桶
	 *
	 * @param bucketName 存储桶名称
	 */
	@Override
	public void createBucket(String bucketName) {
		if (!bucketExists(bucketName)) {
			cosClient.createBucket(getBucketName(bucketName));
		}
	}

	/**
	 * 删除 存储桶
	 *
	 * @param bucketName 存储桶名称
	 */
	@Override
	public void deleteBucket(String bucketName) {
		if (bucketExists(bucketName)) {
			cosClient.deleteBucket(getBucketName(bucketName));
		}
	}

	/**
	 * 存储桶是否存在
	 *
	 * @param bucketName 存储桶名称
	 * @return boolean
	 */
	@Override
	public boolean bucketExists(String bucketName) {
		return cosClient.doesBucketExist(getBucketName(bucketName));
	}

	/**
	 * 拷贝文件
	 *
	 * @param sourceBucketName 源存储桶名称
	 * @param fileName         存储桶文件名称
	 * @param targetBucketName 目标存储桶名称
	 */
	@Override
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
	public void copyFile(String sourceBucketName, String fileName, String targetBucketName, String targetFileName) {
		cosClient.copyObject(getBucketName(sourceBucketName), fileName, getBucketName(targetBucketName), targetFileName);
	}

	/**
	 * 获取文件元信息
	 *
	 * @param fileName 存储桶文件名称
	 * @return 文件元信息
	 */
	@Override
	public OssFileMetaInfo getFileMetaInfo(String fileName) {
		return getFileMetaInfo(ossProperties.getTencentCos()
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
	public OssFileMetaInfo getFileMetaInfo(String bucketName, String fileName) {
		final ObjectMetadata metadata = cosClient.getObjectMetadata(getBucketName(bucketName), fileName);
		OssFileMetaInfo metaInfo = new OssFileMetaInfo();
		metaInfo.setName(fileName);
		metaInfo.setLink(getFileLink(metaInfo.getName()));
		metaInfo.setHash(metadata.getContentMD5());
		metaInfo.setLength(metadata.getContentLength());
		metaInfo.setUploadTime(metadata.getLastModified());
		metaInfo.setContentType(metadata.getContentType());
		return metaInfo;
	}

	/**
	 * 获取 COS 域名地址
	 *
	 * @return 域名地址
	 */
	private String getCosEndpoint() {
		return getCosEndpoint(ossProperties.getTencentCos()
			.getBucketName());
	}

	/**
	 * 获取 COS 域名地址
	 *
	 * @param bucketName 存储桶名称
	 * @return 域名地址
	 */
	private String getCosEndpoint(String bucketName) {
		String prefix = ossProperties.getTencentCos()
			.getEndpoint()
			.contains("https://") ? "https://" : "http://";
		return prefix + getBucketName(bucketName) + StrUtil.DOT + ossProperties.getTencentCos()
			.getEndpoint()
			.replaceFirst(prefix, StrUtil.EMPTY);
	}

	/**
	 * 添加协议
	 *
	 * @return 包含协议头
	 */
	public String getCosEndpointProtocolHost() {
		String prefix = ossProperties.getTencentCos()
			.getEndpoint()
			.contains("https://") ? "https://" : "http://";
		return prefix + getCosEndpoint();
	}

	/**
	 * 添加协议
	 *
	 * @param bucketName 存储桶名称
	 * @return 包含协议头
	 */
	public String getCosEndpointProtocolHost(String bucketName) {
		String prefix = ossProperties.getTencentCos()
			.getEndpoint()
			.contains("https://") ? "https://" : "http://";
		return prefix + getCosEndpoint(bucketName);
	}

	/**
	 * 获取文件相对路径
	 *
	 * @param fileName 存储桶对象名称
	 * @return 文件相对路径
	 */
	@Override
	public String getFilePath(String fileName) {
		return getCosEndpoint().concat(StrUtil.SLASH)
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
		return getCosEndpoint(bucketName).concat(StrUtil.SLASH)
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
		return getCosEndpointProtocolHost().concat(StrUtil.SLASH)
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
		return getCosEndpointProtocolHost(bucketName).concat(StrUtil.SLASH)
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
		return uploadFile(ossProperties.getAliOss()
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
		return uploadFile(ossProperties.getAliOss()
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

	private OssFile upload(String bucketName, String fileName, InputStream stream, boolean cover) {
		// 创建存储桶
		createBucket(bucketName);
		// 获取 oss 存储文件名
		String key = getFileName(fileName);

		// 是否覆盖上传
		if (cover) {
			cosClient.putObject(getBucketName(bucketName), key, stream, null);
		} else {
			PutObjectResult response = cosClient.putObject(getBucketName(bucketName), key, stream, null);
			int retry = 0;
			int retryCount = 5;
			// 重试 5 次
			while (StrUtil.isEmpty(response.getETag()) && retry < retryCount) {
				response = cosClient.putObject(getBucketName(bucketName), key, stream, null);
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
	public void deleteFile(String fileName) {
		cosClient.deleteObject(getBucketName(), fileName);
	}

	/**
	 * 删除文件
	 *
	 * @param bucketName 存储桶名称
	 * @param fileName   存储桶对象名称
	 */
	@Override
	public void deleteFile(String bucketName, String fileName) {
		cosClient.deleteObject(getBucketName(bucketName), fileName);
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
}
