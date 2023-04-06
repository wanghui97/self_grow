package com.wanghui.blog.handler.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wanghui.blog.util.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;


/**
* 自定义填充处理器 MetaObjectHandler
*   保存和更新时自动更新以下字段
* */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = null;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
            userId = -1L;//表示是自己创建
        }
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createBy",userId , metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if(Objects.isNull(SecurityUtils.getAuthentication())){
            this.setFieldValByName("updateTime", new Date(), metaObject);
        }else {
            this.setFieldValByName("updateTime", new Date(), metaObject);
            this.setFieldValByName(" ", SecurityUtils.getUserId(), metaObject);
        }

    }
}