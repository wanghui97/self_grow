package com.wanghui.blog.service.impl;

import com.wanghui.blog.Enum.AppHttpCodeEnum;
import com.wanghui.blog.Exception.SystemException;
import com.wanghui.blog.entity.LoginUser;
import com.wanghui.blog.entity.User;
import com.wanghui.blog.service.BlogReceptionLoginService;
import com.wanghui.blog.util.BeanCopyUtils;
import com.wanghui.blog.util.JwtUtil;
import com.wanghui.blog.util.RedisCache;
import com.wanghui.blog.util.ResponseResult;
import com.wanghui.blog.vo.BlogUserLoginVo;
import com.wanghui.blog.vo.UserInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * ClassName: BlogReceptionLoginServiceImp
 * Package: com.wanghui.blog.service.impl
 * Description:
 *      博客前台登录接口实现
 * @Author 王辉
 * @Create 2023/3/27 10:18
 * @Version 1.0
 */
@Service
public class BlogReceptionLoginServiceImpl implements BlogReceptionLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;//从配置类注入security的AuthenticationManager
    @Autowired
    private RedisCache redisCache;//封装的RedisTemplate

    @Override
    public ResponseResult login(User user){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        //调用认证的方法，参数是一个authentication，我们传入authentication的实现类对象usernamePasswordAuthenticationToken
        //该方法默认最后会调用UserDetailsService,这个类是从内存中查询认证，但我们去数据库中认证，所以要创建一个UserDetailsService的实现类实现自己的逻辑
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        //判断是否认证通过，如果不通过则返回null
        if(Objects.isNull(authenticate)){
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
        //从authenticate对象中获取loginUser对象
        LoginUser loginUser = (LoginUser)authenticate.getPrincipal();
        //获取用户id，根据userId生成token信息
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //将用户信息存到Redis缓存中
        redisCache.setCacheObject("BlogReceptionCache:"+userId,loginUser);
        //封装响应报文：响应报文报文体中包含token信息和用户基本信息
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo blogUserLoginVo = new BlogUserLoginVo(jwt,userInfoVo);
        //返回登录信息
        return ResponseResult.okResult(blogUserLoginVo);
    }

    @Override
    public ResponseResult loginOut() {
        //从SecurityContextHolder中获取用户的认证信息对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //从用户认证信息中获取用户信息
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        //删除redis缓存信息
        redisCache.deleteObject("BlogReceptionCache:"+loginUser.getUser().getId());
        //返回响应信息
        return ResponseResult.okResult();
    }
}
