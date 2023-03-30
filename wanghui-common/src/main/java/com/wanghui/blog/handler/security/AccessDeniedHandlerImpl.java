package com.wanghui.blog.handler.security;

import com.alibaba.fastjson.JSON;
import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.util.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 *  授权失败处理类，需要在springSecurity配置文件中配置
 * @Author 王辉
 * @Create 2023/3/28 20:24
 * @Version 1.0
 */
//授权失败
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        e.printStackTrace();
        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
