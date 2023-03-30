package com.wanghui.blog.service.impl;

import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.config.OssConfig;
import com.wanghui.blog.service.UploadService;
import com.wanghui.blog.util.PathUtils;
import com.wanghui.blog.util.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description:
 *
 * @Author 王辉
 * @Create 2023/3/29 16:51
 * @Version 1.0
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Override
    public ResponseResult uploadImg(MultipartFile imgFile) {
        //获取原始文件名
        String fileName = imgFile.getOriginalFilename();
        //判断文件类型
        if(!fileName.endsWith(".png")&&!fileName.endsWith(".jpg")){
            return ResponseResult.errorResult(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //文件保存路径，调用PathUtils工具类,实现效果：2023/03/29/uuid.png
        String key = PathUtils.generateFilePath(fileName);
        //将文件上传到七牛云，返回响应报文
        return OssConfig.uploadOss(imgFile,key);
    }
}
