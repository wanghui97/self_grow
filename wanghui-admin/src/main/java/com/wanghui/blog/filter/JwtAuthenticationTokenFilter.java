package com.wanghui.blog.filter;

import com.alibaba.fastjson.JSON;
import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.entity.LoginUser;
import com.wanghui.blog.util.*;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Description:
 *  权限登录认证过滤器类，需要在springSecurity配置文件中配置
 * @Author 王辉
 * @Create 2023/3/28 20:24
 * @Version 1.0
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = httpServletRequest.getHeader("token");
        //判断token是否携带token，如果没有携带token，表示未登录，直接放行
        if(!StringUtils.hasText(token)){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        //如果token不为空，解析token，claims有异常表示token非法或者
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //claims有异常表示token非法或者token被篡改，响应异常信息
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse, JSON.toJSONString(result));
            return;
        }
        //如果解析后的token不为空，获取token中的用户id
        //assert用于判断某一条件是否达成，如果达成不进行任何操作，如没有达成会抛出异常
        assert !Objects.isNull(claims);
        String userId = claims.getSubject();
        //通过userid去redis中获取之前保存的登录的用户对象
        LoginUser loginUser = redisCache.getCacheObject(CodeLibraryUtil.BLOG_BACKGROUND_CACHE + userId);
        //判断对象是否为空，如果为空表示登录过期提示重新登录
        if(Objects.isNull(loginUser)){
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(httpServletResponse,JSON.toJSONString(responseResult));
            return;
        }
        //如果查询到了登录用户，则调用 UsernamePasswordAuthenticationToken 类获取当前用户权限信息
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        //并将查询到的用户信息​存入 SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        //过滤器放行
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
