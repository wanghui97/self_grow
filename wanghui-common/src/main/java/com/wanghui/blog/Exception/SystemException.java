package com.wanghui.blog.Exception;

import com.wanghui.blog.Enum.AppHttpCodeEnum;
/**
 * ClassName: SystemException
 * Package: com.wanghui.blog.Exception
 * Description:
 *      运行时异常处理
 * @Author 王辉
 * @Create 2023/3/28 20:24
 * @Version 1.0
 */
public class SystemException extends RuntimeException{
    private int code;

    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public SystemException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }
}