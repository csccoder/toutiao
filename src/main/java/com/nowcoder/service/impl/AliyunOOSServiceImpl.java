package com.nowcoder.service.impl;

import com.aliyun.oss.OSSClient;
import com.nowcoder.service.OOSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 阿里云对象存储服务 实现
 */
@Service
public class AliyunOOSServiceImpl implements OOSService {
    private Logger logger= LoggerFactory.getLogger(getClass());

    @Override
    public String saveImage(MultipartFile file) throws IOException {
        // endpoint以杭州为例，其它region请按实际情况填写
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建
        String accessKeyId = "LTAItW2Pg1WV9PeO";
        String accessKeySecret = "3spRfo8CAcMcvjap8ZT0dBufIZAE0G";
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 上传
        InputStream inputStream = file.getInputStream();
        String fileName=generateUniqueFileName(file);
        ossClient.putObject("toutiaoproject", fileName, inputStream);
        // 关闭client
        ossClient.shutdown();

        return "http://toutiaoproject.oss-cn-beijing.aliyuncs.com/"+fileName;

    }


    public String generateUniqueFileName(MultipartFile file){
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        String fileExt=file.getOriginalFilename().substring(dotPos+1);
        String fileName= UUID.randomUUID().toString().replaceAll("-","")+"."+fileExt;
        return fileName;
    }
}
