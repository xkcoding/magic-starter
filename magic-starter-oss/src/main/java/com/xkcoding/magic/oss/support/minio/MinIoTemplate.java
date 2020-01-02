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

package com.xkcoding.magic.oss.support.minio;

import com.xkcoding.magic.core.tool.util.StrUtil;
import com.xkcoding.magic.oss.AbstractOssTemplate;
import com.xkcoding.magic.oss.autoconfigure.OssProperties;
import com.xkcoding.magic.oss.enums.OssType;
import com.xkcoding.magic.oss.model.OssFile;
import com.xkcoding.magic.oss.model.OssFileMetaInfo;
import com.xkcoding.magic.oss.support.minio.enums.PolicyType;
import com.xkcoding.magic.oss.support.rule.OssRule;
import io.minio.MinioClient;
import io.minio.ObjectStat;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * MinIO 操作，参考文档：https://docs.min.io/docs/java-client-api-reference.html
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/12/31 17:20
 */
public class MinIoTemplate extends AbstractOssTemplate {
	private final MinioClient minioClient;
	private final OssProperties ossProperties;

	public MinIoTemplate(MinioClient minioClient, OssProperties ossProperties, OssRule ossRule) {
		super(ossProperties, ossRule, OssType.MINIO);
		this.minioClient = minioClient;
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
			minioClient.makeBucket(getBucketName(bucketName));
			minioClient.setBucketPolicy(getBucketName(bucketName), getPolicyType(getBucketName(bucketName), PolicyType.READ));
		}
	}

	/**
	 * 删除 存储桶
	 *
	 * @param bucketName 存储桶名称
	 */
	@Override
	@SneakyThrows
	public void deleteBucket(String bucketName) {
		if (bucketExists(bucketName)) {
			minioClient.removeBucket(getBucketName(bucketName));
		}
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
		return minioClient.bucketExists(getBucketName(bucketName));
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
	@SneakyThrows
	public void copyFile(String sourceBucketName, String fileName, String targetBucketName, String targetFileName) {
		minioClient.copyObject(getBucketName(sourceBucketName), fileName, getBucketName(targetBucketName), targetFileName);
	}

	/**
	 * 获取文件元信息
	 *
	 * @param fileName 存储桶文件名称
	 * @return 文件元信息
	 */
	@Override
	public OssFileMetaInfo getFileMetaInfo(String fileName) {
		return getFileMetaInfo(ossProperties.getMinIo()
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
		final ObjectStat objectStat = minioClient.statObject(getBucketName(bucketName), fileName);
		OssFileMetaInfo metaInfo = new OssFileMetaInfo();
		metaInfo.setName(StrUtil.isNotBlank(objectStat.name()) ? objectStat.name() : fileName);
		metaInfo.setLink(getFileLink(metaInfo.getName()));
		metaInfo.setHash(String.valueOf(objectStat.hashCode()));
		metaInfo.setLength(objectStat.length());
		metaInfo.setUploadTime(objectStat.createdTime());
		metaInfo.setContentType(objectStat.contentType());
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
		return ossProperties.getMinIo()
			.getEndpoint()
			.concat(StrUtil.SLASH)
			.concat(getBucketName())
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
		return ossProperties.getMinIo()
			.getEndpoint()
			.concat(StrUtil.SLASH)
			.concat(getBucketName(bucketName))
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
		return uploadFile(ossProperties.getMinIo()
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
		return uploadFile(ossProperties.getMinIo()
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
	@SneakyThrows
	public OssFile uploadFile(String bucketName, String fileName, InputStream stream) {
		// 创建存储桶
		createBucket(bucketName);
		// 获取 oss 存储文件名
		String key = getFileName(fileName);

		minioClient.putObject(getBucketName(bucketName), key, stream, (long) stream.available(), null, null, StrUtil.mimeType(key));

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
		minioClient.removeObject(getBucketName(), fileName);
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
		minioClient.removeObject(getBucketName(bucketName), fileName);
	}

	/**
	 * 批量删除文件
	 *
	 * @param fileNames 存储桶对象名称集合
	 */
	@Override
	public void deleteFiles(List<String> fileNames) {
		minioClient.removeObjects(getBucketName(), fileNames);
	}

	/**
	 * 批量删除文件
	 *
	 * @param bucketName 存储桶名称
	 * @param fileNames  存储桶对象名称集合
	 */
	@Override
	public void deleteFiles(String bucketName, List<String> fileNames) {
		minioClient.removeObjects(getBucketName(bucketName), fileNames);
	}

	/**
	 * 获取存储桶策略
	 *
	 * @param policyType 策略枚举
	 * @return 存储桶策略
	 */
	public String getPolicyType(PolicyType policyType) {
		return getPolicyType(getBucketName(), policyType);
	}

	/**
	 * 获取存储桶策略，参考 {@link MinioClient#setBucketPolicy(java.lang.String, java.lang.String)} 的注释
	 *
	 * @param bucketName 存储桶名称
	 * @param policyType 策略枚举
	 * @return 存储桶策略
	 */
	public static String getPolicyType(String bucketName, PolicyType policyType) {
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		builder.append("    \"Statement\": [\n");
		builder.append("        {\n");
		builder.append("            \"Action\": [\n");

		switch (policyType) {
			case WRITE:
				builder.append("                \"s3:GetBucketLocation\",\n");
				builder.append("                \"s3:ListBucketMultipartUploads\"\n");
				break;
			case READ_WRITE:
				builder.append("                \"s3:GetBucketLocation\",\n");
				builder.append("                \"s3:ListBucket\",\n");
				builder.append("                \"s3:ListBucketMultipartUploads\"\n");
				break;
			default:
				builder.append("                \"s3:GetBucketLocation\"\n");
				break;
		}

		builder.append("            ],\n");
		builder.append("            \"Effect\": \"Allow\",\n");
		builder.append("            \"Principal\": \"*\",\n");
		builder.append("            \"Resource\": \"arn:aws:s3:::");
		builder.append(bucketName);
		builder.append("\"\n");
		builder.append("        },\n");
		if (PolicyType.READ.equals(policyType)) {
			builder.append("        {\n");
			builder.append("            \"Action\": [\n");
			builder.append("                \"s3:ListBucket\"\n");
			builder.append("            ],\n");
			builder.append("            \"Effect\": \"Deny\",\n");
			builder.append("            \"Principal\": \"*\",\n");
			builder.append("            \"Resource\": \"arn:aws:s3:::");
			builder.append(bucketName);
			builder.append("\"\n");
			builder.append("        },\n");
		}
		builder.append("        {\n");
		builder.append("            \"Action\": ");

		switch (policyType) {
			case WRITE:
				builder.append("[\n");
				builder.append("                \"s3:AbortMultipartUpload\",\n");
				builder.append("                \"s3:DeleteObject\",\n");
				builder.append("                \"s3:ListMultipartUploadParts\",\n");
				builder.append("                \"s3:PutObject\"\n");
				builder.append("            ],\n");
				break;
			case READ_WRITE:
				builder.append("[\n");
				builder.append("                \"s3:AbortMultipartUpload\",\n");
				builder.append("                \"s3:DeleteObject\",\n");
				builder.append("                \"s3:GetObject\",\n");
				builder.append("                \"s3:ListMultipartUploadParts\",\n");
				builder.append("                \"s3:PutObject\"\n");
				builder.append("            ],\n");
				break;
			default:
				builder.append("\"s3:GetObject\",\n");
				break;
		}

		builder.append("            \"Effect\": \"Allow\",\n");
		builder.append("            \"Principal\": \"*\",\n");
		builder.append("            \"Resource\": \"arn:aws:s3:::");
		builder.append(bucketName);
		builder.append("/*\"\n");
		builder.append("        }\n");
		builder.append("    ],\n");
		builder.append("    \"Version\": \"2012-10-17\"\n");
		builder.append("}\n");
		return builder.toString();
	}
}
