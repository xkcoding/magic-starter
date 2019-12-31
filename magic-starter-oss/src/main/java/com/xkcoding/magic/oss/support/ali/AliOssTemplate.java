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

import com.xkcoding.magic.oss.OssTemplate;
import com.xkcoding.magic.oss.model.OssFile;
import com.xkcoding.magic.oss.model.OssFileMetaInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 阿里云 OSS 操作，参考文档：https://help.aliyun.com/document_detail/31947.html?spm=a2c4g.11186623.3.2.fbba69cbWx4YuF
 * </p>
 *
 * @author yangkai.shen
 * @date Created in 2019/12/31 17:19
 */
public class AliOssTemplate implements OssTemplate {
	/**
	 * 创建 存储桶
	 *
	 * @param bucketName 存储桶名称
	 */
	@Override
	public void createBucket(String bucketName) {

	}

	/**
	 * 删除 存储桶
	 *
	 * @param bucketName 存储桶名称
	 */
	@Override
	public void deleteBucket(String bucketName) {

	}

	/**
	 * 存储桶是否存在
	 *
	 * @param bucketName 存储桶名称
	 * @return boolean
	 */
	@Override
	public boolean bucketExists(String bucketName) {
		return false;
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

	}

	/**
	 * 获取文件元信息
	 *
	 * @param fileName 存储桶文件名称
	 * @return 文件元信息
	 */
	@Override
	public OssFileMetaInfo getFileMetaInfo(String fileName) {
		return null;
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
		return null;
	}

	/**
	 * 获取文件相对路径
	 *
	 * @param fileName 存储桶对象名称
	 * @return 文件相对路径
	 */
	@Override
	public String getFilePath(String fileName) {
		return null;
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
		return null;
	}

	/**
	 * 获取文件地址
	 *
	 * @param fileName 存储桶对象名称
	 * @return 文件地址
	 */
	@Override
	public String getFileLink(String fileName) {
		return null;
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
		return null;
	}

	/**
	 * 上传文件
	 *
	 * @param file 上传文件类
	 * @return 文件信息
	 */
	@Override
	public OssFile uploadFile(MultipartFile file) {
		return null;
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
		return null;
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
	public OssFile uploadFile(String bucketName, String fileName, MultipartFile file) {
		return null;
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
		return null;
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
		return null;
	}

	/**
	 * 删除文件
	 *
	 * @param fileName 存储桶对象名称
	 */
	@Override
	public void deleteFile(String fileName) {

	}

	/**
	 * 删除文件
	 *
	 * @param bucketName 存储桶名称
	 * @param fileName   存储桶对象名称
	 */
	@Override
	public void deleteFile(String bucketName, String fileName) {

	}

	/**
	 * 批量删除文件
	 *
	 * @param fileNames 存储桶对象名称集合
	 */
	@Override
	public void deleteFiles(List<String> fileNames) {

	}

	/**
	 * 批量删除文件
	 *
	 * @param bucketName 存储桶名称
	 * @param fileNames  存储桶对象名称集合
	 */
	@Override
	public void deleteFiles(String bucketName, List<String> fileNames) {

	}
}
