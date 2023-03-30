package com.wanghui.blog.handler.exception;

import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.Exception.SystemException;
import com.wanghui.blog.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ClassName: GlobalExceptionHandler
 * Package: com.wanghui.blog.handler
 * Description:
 *  配置全局统一异常处理
 * @Author 王辉
 * @Create 2023/3/28 20:24
 * @Version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SystemException.class)//如果是运行时异常用该方法处理
    public ResponseResult systemExceptionHandler(SystemException e){
        log.error("出现了异常！{ }" , e);
        return ResponseResult.errorResult(e.getCode(),e.getMsg());
    }

    @ExceptionHandler(Exception.class)//其他异常类型处理
    public ResponseResult ExceptionHandler(Exception e){
        log.error("出现了不可预期的错误！{ }" , e);
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,e.getMessage());
    }
}
