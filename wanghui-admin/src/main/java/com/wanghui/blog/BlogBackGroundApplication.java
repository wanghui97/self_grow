package com.wanghui.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Description:
 *  个人博客系统后台系统启动类
 * @Author 王辉
 * @Create 2023/4/3 16:05
 * @Version 1.0
 */
@SpringBootApplication
@EnableScheduling//开启定时任务
@EnableSwagger2//开启swagger2文档
@EnableGlobalMethodSecurity(prePostEnabled = true)//security权限控制
//@MapperScan("com.wanghui.blog.mapper")  //该配置可以直接替换mapper接口上需要加的@mapper注解
public class BlogBackGroundApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogBackGroundApplication.class,args);
    }
}
