##父项目：wanghui-blog

###项目运行环境
    java 1.8+
    maven 3.0+
    node.js 12.13.1,尽量低点，版本太高运行不起来前端项目，可以下载nvm控制nodejs版本，具体教程自行百度
    
###项目运行方式
####前端项目（必须安装正确的node.js版本）
    博客前台：
        wanghui-Blog\sg-blog-vue文件夹中cmd打开命令行窗口执行以下命令：
        编译：npm install 或者 npm i
        运行：npm run dev
    博客后台
        wanghui-Blog\sg-vue-admin文件夹中cmd打开命令行窗口执行以下命令：
        编译：npm install 或者 npm i
        运行：npm run dev
####后端项目
    博客前台：
        在idea中打开wanghui-blog/src/main/java/com/wanghui/blog/BlogReceptionApplication.java
        右击 run
    博客后台：
        在idea中打开wanghui-admin/src/main/java/com/wanghui/blog/BlogBackGroundApplication.java
        右击 run
###后端子项目：
    wanghui-admin 博客后台 
    wanghui-blog 博客前台 
    wanghui-common 公用代码
###前端项目：
    sg-blog-vue 博客前台
    sg-vue-admin 博客后台

###接口文档：
    博客前台系统：http://localhost:7777/swagger-ui.html#/
    具体可参考笔记：项目实战-前后端分离博客系统.md
##备注说明
    vo：vo包的作用是实体类对象entity的阉割版，为了避免实体类
        中的其他非前端某接口请求需要的多余数据返回给前端，所以
        使用一个阉割版的entity对象
    dto：dto的作用也是实体类对象entity的阉割版，为了实现swagger2中
        自动生成接口文档的需要(接口中有些方法的参数是实体类中的某些字段
        可以将这些字段抽取出来封装成一个dto，加上@Api()注解就可以实现
        接口文档中实体类的字段映射)
        
    当启动类上加了@MapperScan("com.wanghui.blog.mapper")时，dao层可以不加注解标识，
    如果启动类上没加@MapperScan("com.wanghui.blog.mapper")时，dao层必须加@Mapper
    标识，不然启动会报错
    
    
