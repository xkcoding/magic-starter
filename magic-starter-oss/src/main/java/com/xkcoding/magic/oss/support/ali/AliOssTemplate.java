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

package com.xkcoding.magic.oss.support.ali;

import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
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
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 阿里云 OSS 操作，参考文档：https://help.aliyun.com/document_detail/31947.html?spm=a2c4g.11186623.3.2.fbba69cbWx4YuF
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/12/31 17:19
 */
public class AliOssTemplate extends AbstractOssTemplate {
	private final OSSClient ossClient;
	private final OssProperties ossProperties;

	public AliOssTemplate(OSSClient ossClient, OssProperties ossProperties, OssRule ossRule) {
		super(ossProperties, ossRule, OssType.ALI_OSS);
		this.ossClient = ossClient;
		this.ossProperties = ossProperties;
	}

	/**
	 * 创建 存储桶
	 *
	 * @param bucketName 存储桶名称
	 */
	@Override
	public void createBucket(String bucketName) {
		if (!bucketExists(bucketName)) {
			ossClient.createBucket(getBucketName(bucketName));
			ossClient.setBucketAcl(getBucketName(bucketName), CannedAccessControlList.PublicRead);
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
			ossClient.deleteBucket(getBucketName(bucketName));
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
		return ossClient.doesBucketExist(getBucketName(bucketName));
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
		ossClient.copyObject(getBucketName(sourceBucketName), fileName, getBucketName(targetBucketName), targetFileName);
	}

	/**
	 * 获取文件元信息
	 *
	 * @param fileName 存储桶文件名称
	 * @return 文件元信息
	 */
	@Override
	public OssFileMetaInfo getFileMetaInfo(String fileName) {
		return getFileMetaInfo(ossProperties.getAliOss()
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
		final ObjectMetadata metadata = ossClient.getObjectMetadata(getBucketName(bucketName), fileName);
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
	 * 获取 OSS 域名地址
	 *
	 * @return 域名地址
	 */
	private String getOssEndpoint() {
		return getOssEndpoint(ossProperties.getAliOss()
			.getBucketName());
	}

	/**
	 * 获取 OSS 域名地址
	 *
	 * @param bucketName 存储桶名称
	 * @return 域名地址
	 */
	private String getOssEndpoint(String bucketName) {
		String prefix = ossProperties.getAliOss()
			.getHttps() ? "https://" : "http://";

		return prefix + getBucketName(bucketName) + StrUtil.DOT + ossProperties.getAliOss()
			.getEndpoint()
			.replaceFirst(prefix, StrUtil.EMPTY);
	}

	/**
	 * 获取文件相对路径
	 *
	 * @param fileName 存储桶对象名称
	 * @return 文件相对路径
	 */
	@Override
	public String getFilePath(String fileName) {
		return getOssEndpoint().concat(StrUtil.SLASH)
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
		return getOssEndpoint(bucketName).concat(StrUtil.SLASH)
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
		return getOssEndpoint().concat(StrUtil.SLASH)
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
		return getOssEndpoint(bucketName).concat(StrUtil.SLASH)
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
			ossClient.putObject(getBucketName(bucketName), key, stream);
		} else {
			PutObjectResult response = ossClient.putObject(getBucketName(bucketName), key, stream);
			int retry = 0;
			int retryCount = 5;
			// 重试 5 次
			while (StrUtil.isEmpty(response.getETag()) && retry < retryCount) {
				response = ossClient.putObject(getBucketName(bucketName), key, stream);
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
		ossClient.deleteObject(getBucketName(), fileName);
	}

	/**
	 * 删除文件
	 *
	 * @param bucketName 存储桶名称
	 * @param fileName   存储桶对象名称
	 */
	@Override
	public void deleteFile(String bucketName, String fileName) {
		ossClient.deleteObject(getBucketName(bucketName), fileName);
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

	public String getUploadToken() {
		return getUploadToken(ossProperties.getAliOss()
			.getBucketName());
	}

	/**
	 * 获取上传凭证，普通上传，默认 1 小时过期
	 *
	 * @param bucketName 存储桶名称
	 * @return 上传凭证
	 */
	public String getUploadToken(String bucketName) {
		// 默认过期时间1小时，单位 秒
		return getUploadToken(bucketName, ossProperties.getAliOss()
			.getArgs()
			.get("expireTime", 60 * 60));
	}

	/**
	 * 获取上传凭证，普通上传
	 * TODO 上传大小限制、基础路径
	 *
	 * @param bucketName 存储桶名称
	 * @param expireTime 过期时间，单位秒
	 * @return 上传凭证
	 */
	public String getUploadToken(String bucketName, long expireTime) {
		String baseDir = "upload";

		long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
		Date expiration = new Date(expireEndTime);

		PolicyConditions policy = new PolicyConditions();
		// 默认大小限制10M
		policy.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, ossProperties.getAliOss()
			.getArgs()
			.get("contentLengthRange", 1024 * 1024 * 10));
		policy.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, baseDir);

		String postPolicy = ossClient.generatePostPolicy(expiration, policy);
		byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
		String encodedPolicy = BinaryUtil.toBase64String(binaryData);
		String postSignature = ossClient.calculatePostSignature(postPolicy);

		Map<String, String> respMap = new LinkedHashMap<>(16);
		respMap.put("accessid", ossProperties.getAliOss()
			.getAccessKey());
		respMap.put("policy", encodedPolicy);
		respMap.put("signature", postSignature);
		respMap.put("dir", baseDir);
		respMap.put("host", getOssEndpoint(bucketName));
		respMap.put("expire", String.valueOf(expireEndTime / 1000));
		return JSONUtil.toJsonStr(respMap);
	}
}
