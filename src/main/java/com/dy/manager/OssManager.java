package com.dy.manager;

import com.aliyun.oss.OSS;
import com.dy.config.CosClientConfig;
import com.dy.config.OssClientConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * Oss 对象存储操作
 *

 */
@Component
public class OssManager {

    @Resource
    private OssClientConfig ossClientConfig;

    @Resource
    private OSS ossClient;

//    /**
//     * 上传对象
//     *
//     * @param key 唯一键
//     * @param file 本地文件路径
//     * @return
//     */
//    public PutObjectResult putObject(String key, File file) {
//        PutObjectRequest putObjectRequest = new PutObjectRequest(ossClientConfig.getBucketName(), key,
//                file);
//        return ossClient.putObject(putObjectRequest);
//    }

    /**
     * 上传对象
     *
     * @param key 唯一键
     * @param file 文件
     * @return
     */
    public void putObject(String key, File file) {
        ossClient.putObject(ossClientConfig.getBucketName(), key, file);
    }
}
