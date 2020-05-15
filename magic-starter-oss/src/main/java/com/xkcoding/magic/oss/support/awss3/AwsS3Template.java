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

package com.xkcoding.magic.oss.support.awss3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.xkcoding.magic.core.tool.util.StrUtil;
import com.xkcoding.magic.oss.AbstractOssTemplate;
import com.xkcoding.magic.oss.autoconfigure.OssProperties;
import com.xkcoding.magic.oss.enums.OssType;
import com.xkcoding.magic.oss.model.OssFile;
import com.xkcoding.magic.oss.model.OssFileMetaInfo;
import com.xkcoding.magic.oss.support.minio.enums.PolicyType;
import com.xkcoding.magic.oss.support.rule.OssRule;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

import static com.xkcoding.magic.oss.support.minio.MinIoTemplate.getPolicyType;

/**
 * <p>
 * AwsS3 操作，参考文档：https://docs.aws.amazon.com/zh_cn/sdk-for-java/v1/developer-guide/examples-s3.html
 * </p>
 *
 * @author harrylee
 * @date Created in 2020/05/15 15:38
 */
public class AwsS3Template extends AbstractOssTemplate {
	private final AmazonS3 amazonS3;
	private final OssProperties ossProperties;

	public AwsS3Template(AmazonS3 amazonS3, OssProperties ossProperties, OssRule ossRule) {
		super(ossProperties, ossRule, OssType.AWSS3);
		this.amazonS3 = amazonS3;
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
			amazonS3.createBucket(getBucketName(bucketName));
			// 这里使用与 minio 一样的存储桶策略
			amazonS3.setBucketPolicy(getBucketName(bucketName),
				getPolicyType(getBucketName(bucketName), PolicyType.READ));
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
			amazonS3.deleteBucket(getBucketName(bucketName));
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
		return amazonS3.doesBucketExistV2(bucketName);
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
		amazonS3.copyObject(getBucketName(sourceBucketName), fileName, getBucketName(targetBucketName), targetFileName);
	}

	/**
	 * 获取文件元信息
	 *
	 * @param fileName 存储桶文件名称
	 * @return 文件元信息
	 */
	@Override
	public OssFileMetaInfo getFileMetaInfo(String fileName) {
		return getFileMetaInfo(getBucketName(), fileName);
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
		final ObjectMetadata objectMetadata = amazonS3.getObjectMetadata(new GetObjectMetadataRequest(getBucketName(bucketName), fileName));
		OssFileMetaInfo metaInfo = new OssFileMetaInfo();
		metaInfo.setName(fileName);
		metaInfo.setLink(getFileLink(metaInfo.getName()));
		metaInfo.setHash(objectMetadata.getContentMD5());
		metaInfo.setLength(objectMetadata.getContentLength());
		metaInfo.setUploadTime(objectMetadata.getLastModified());
		metaInfo.setContentType(objectMetadata.getContentType());
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
		return ossProperties.getAwsS3()
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
		return uploadFile(getBucketName(), fileName, file);
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
		return uploadFile(getBucketName(bucketName), fileName, file.getInputStream());
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
		return uploadFile(getBucketName(), fileName, stream);
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

		ObjectMetadata om = new ObjectMetadata();
		// 获取 oss 存储文件名
		String key = getFileName(fileName);

		om.setContentLength(stream.available());
		PutObjectRequest cannedAcl = new PutObjectRequest(bucketName, key, stream, om)
			.withCannedAcl(CannedAccessControlList.PublicRead);
		amazonS3.putObject(cannedAcl);

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
		deleteFile(getBucketName(), fileName);
	}

	/**
	 * 批量删除文件
	 *
	 * @param bucketName 存储桶名称
	 * @param fileName   存储桶对象名称
	 */
	@Override
	@SneakyThrows
	public void deleteFile(String bucketName, String fileName) {
		amazonS3.deleteObject(getBucketName(bucketName), fileName);
	}

	/**
	 * 批量删除文件
	 *
	 * @param fileNames 存储桶对象名称集合
	 */
	@Override
	public void deleteFiles(List<String> fileNames) {
		deleteFiles(getBucketName(), fileNames);
	}

	/**
	 * 批量删除文件
	 *
	 * @param bucketName 存储桶名称
	 * @param fileNames  存储桶对象名称集合
	 */
	@Override
	@SneakyThrows
	public void deleteFiles(String bucketName, List<String> fileNames) {
		DeleteObjectsRequest dor = new DeleteObjectsRequest(getBucketName(bucketName))
			.withKeys(fileNames.toArray(new String[]{}));
		amazonS3.deleteObjects(dor);
	}
}
