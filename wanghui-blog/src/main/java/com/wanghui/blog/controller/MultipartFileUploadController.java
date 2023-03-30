package com.wanghui.blog.controller;

import com.wanghui.blog.service.UploadService;
import com.wanghui.blog.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description:
 *  文件上传处理类
 * @Author 王辉
 * @Create 2023/3/29 16:43
 * @Version 1.0
 */
@RestController
public class MultipartFileUploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
    }

}
