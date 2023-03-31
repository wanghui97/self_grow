package com.wanghui.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * ClassName: BlogReceptionApplication
 * Package: com.wanghui.blog
 * Description:
 *      个人博客前台启动类
 * @Author 王辉
 * @Create 2023/3/23 11:47
 * @Version 1.0
 */
@SpringBootApplication
@EnableScheduling//开启定时任务
@EnableSwagger2//开启swagger2文档
public class BlogReceptionApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogReceptionApplication.class,args);
    }
}
