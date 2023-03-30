package com.wanghui.blog.config;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.util.ResponseResult;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Description:
 *
 * @Author 王辉
 * @Create 2023/3/29 16:59
 * @Version 1.0
 */
@Component
public class OssConfig {

    private static String accessKey;
    private static String secretKey;
    private static String bucket;

    public String getAccessKey() {
        return accessKey;
    }
    @Value("${oss.accessKey}")
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
    @Value("${oss.secretKey}")
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }
    @Value("${oss.bucket}")
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public static ResponseResult uploadOss(MultipartFile imgFile, String filePath){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;
        try {
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);//文件路径
                System.out.println(putRet.hash);
                //获取七牛云图片的外链地址，自己域名+文件路径
                String path = "http://rsa4wjwhk.hn-bkt.clouddn.com/"+key;
                return ResponseResult.okResult(path);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return ResponseResult.errorResult(AppHttpCodeEnum.FILE_UPLOAD_FAILURE);
    }
}
