package com.wanghui.blog.handler.security;

import com.alibaba.fastjson.JSON;
import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.util.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 *  认证失败处理类，需要在springSecurity配置文件中配置
 * @Author 王辉
 * @Create 2023/3/28 20:24
 * @Version 1.0
 */
//认证失败
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        e.printStackTrace();
        ResponseResult result = null;
        if (e instanceof BadCredentialsException){
             result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),e.getMessage());
        }else if (e instanceof InsufficientAuthenticationException){
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }else {
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),"认证或授权失败");
        }
       WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
