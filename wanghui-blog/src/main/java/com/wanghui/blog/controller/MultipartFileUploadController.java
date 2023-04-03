package com.wanghui.blog.controller;

import com.wanghui.blog.annotation.SelfDefinedSystemLog;
import com.wanghui.blog.service.UploadService;
import com.wanghui.blog.util.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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

    /**
     * 图片上传
     * @return 所有数据
     */
    @PostMapping("/upload")
    @SelfDefinedSystemLog(BusinessName="头像上传")
    @ApiOperation(value = "文件上传",notes = "头像上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "img", value = "图片路径")
    })
    public ResponseResult uploadImg(MultipartFile img){
        return uploadService.uploadImg(img);
    }

}
