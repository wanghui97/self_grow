package com.wanghui.blog.service;

import com.wanghui.blog.util.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description:
 *
 * @Author 王辉
 * @Create 2023/3/29 16:51
 * @Version 1.0
 */
public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);
}
