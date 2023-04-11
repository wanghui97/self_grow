# 前后端分离项目部署步骤：(个人博客系统)springboot+vue

```plain
	博客前台系统前端项目：sg-blog-vue
	博客前台系统后端项目：wanghui-blog
	博客后台系统前端项目：sg-vue-admin
	博客后台系统后端项目：wanghui-admin
	博客系统前后台公共项目：wanghui-common
```
# 前端项目部署步骤：

## 1、部署前准备工作

		sg-blog-vue

```plain
  (build前先改一下src/store文件夹下的index.js文件里的baseURL，将localhost改为服务器地址)
  安装依赖：npm install
  运行项目：npm run dev
  构建项目：nmp run build  （生成dist文件，部署时部署该文件就行）
```
		sg-vue-admin
```plain
  安装依赖：npm install
  运行项目：npm run dev
  构建项目：nmp run build  （生成dist文件，部署时部署该文件就行）
```
**注：构建时遇到报错：在main.js中找不到../mock，在main.js中注释掉就行**
		下载安装好自己的虚拟机，配置好相关环境、固定ip地址、关闭防火墙等

		使用远程ssh工具连接本地服务器：

		在root用户的用户目录下新增文件夹self-grows/self-blog

			mkdir -p self-grows/self-blog

## 2、安装nginx		

### 1.安装nginx依赖：pcre

```plain
  下载pcre：
      wget http://downloads.sourceforge.net/project/pcre/pcre/8.37/pcre-8.37.tar.gz
  解压pcre：
      tar -xzpvf pcre-8.37.tar.gz
  进入 pcre-8.37 执行命令：
      ./configure
  执行命令：
      make && make install
  验证pcre是否成功：
      pcre-config --version			# 出现版本号就是安装成功了
```
### 2.安装 openssl 、zlib 、 gcc 依赖

```plain
  运行命令：
    yum -y install make zlib zlib-devel gcc-c++ libtool openssl openssl-devel
```
### 3.下载安装nginx

```plain
  下载nginx：
      wget http://nginx.org/download/nginx-1.20.1.tar.gz
  解压nginx：
      tar -zxvf nginx-1.20.1.tar.gz
  进入nginx-1.20.1目录，执行：
      ./configure
  编译：
      make && make install
```
### 4.启动nginx

```plain
  进入/usr/local/nginx/sbin，启动nginx
      cd /usr/local/nginx/sbin
      ./nginx
  查看nginx是否启动：
      ps -ef | grep nginx
```
### 5.打开80端口

```plain
  因为nginx初始配置启动的是80端口，系统设置默认都是关闭的，运行命令开启80端口
      firewall-cmd --zone=public --add-port=80/tcp --permanent
  执行完出现success之后，重启防火墙配置
      firewall-cmd --reload
  通过查看系统开放的端口号，来检查配置是否生效
      firewall-cmd --list-all
```
## 3、部署项目

### 1.复制打包文件：就是把vue的打包文件复制到nginx的html目录里面

```plain
	博客前台系统前端项目设置端口为80，放在/wh-blog-reception/dist
	博客前台系统前端项目设置端口为80，放在/wh-blog-background/dist
```
### 2.设置History路由：路由设置History模式之后，浏览器刷新页面会出现404的情况，这里就还需要再配置下nginx

	**首先打开nginx的配置文件(/usr/local/nginx/conf/nginx.conf)，修改配置文件**	

```lua
  #user  nobody;
  worker_processes  1;
  
  #error_log  logs/error.log;
  #error_log  logs/error.log  notice;
  #error_log  logs/error.log  info;
  
  #pid        logs/nginx.pid;
  
  
  events {
  	worker_connections  1024;
  }
  
  
  http {
  	include       mime.types;
  	default_type  application/octet-stream;
  
  	#log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
  	#                  '$status $body_bytes_sent "$http_referer" '
  	#                  '"$http_user_agent" "$http_x_forwarded_for"';
  
  	#access_log  logs/access.log  main;
  
  	sendfile        on;
  	#tcp_nopush     on;
  
  	#keepalive_timeout  0;
  	keepalive_timeout  65;
  
  	#gzip  on;
  	#博客前台系统前端项目设置端口为80
  	server {
  		listen       80; #监听80端口
  		server_name  192.168.253.128;
  
  		#charset koi8-r;
  
  		#access_log  logs/host.access.log  main;
  
  		location / { #80端口下所有以/开头的请求都转发到/usr/local/nginx/html/wh-blog-reception/dist路径下
  			root   html/wh-blog-reception/dist;
  			index  index.html index.htm;
  			try_files $uri $uri/ /index.html;
  		}
  
  		#error_page  404              /404.html;
  
  		# redirect server error pages to the static page /50x.html
  		#
  		error_page   500 502 503 504  /50x.html;
  		location = /50x.html {
  			root   html;
  		}
  
  		# proxy the PHP scripts to Apache listening on 127.0.0.1:80
  		#
  		#location ~ \.php$ {
  		#    proxy_pass   http://127.0.0.1;
  		#}
  
  		# pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
  		#
  		#location ~ \.php$ {
  		#    root           html;
  		#    fastcgi_pass   127.0.0.1:9000;
  		#    fastcgi_index  index.php;
  		#    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
  		#    include        fastcgi_params;
  		#}
  
  		# deny access to .htaccess files, if Apache's document root
  		# concurs with nginx's one
  		#
  		#location ~ /\.ht {
  		#    deny  all;
  		#}
  	}
  	#博客后台系统前端项目设置端口为81
  	server {
  		listen       81; #监听81端口
  		server_name  localhost;
  
  		#charset koi8-r;
  
  		#access_log  logs/host.access.log  main;
  
  		location / { #81端口下所有以/开头的请求都转发到/usr/local/nginx/html/wh-blog-background/dist路径下
  			root   html/wh-blog-background/dist;
  			index  index.html index.htm;
  			try_files $uri $uri/ /index.html;
  		}
  		#参考https://blog.csdn.net/weixin_39255905/article/details/125168464
  		#81端口下所有以/prod-api开头的请求都转发到/usr/local/nginx/html/wh-blog-background/dist路径下
  		location /prod-api/{ 
  			#注意http://192.168.253.128:8989/ 会去掉/prod-api前缀，如：localhost:81/prod-api/user/login 经过Nginx所转发，访问的后端接口路径为localhost:8989/user/login
  			#如果http://192.168.253.128:8989 的话，不会去掉前缀，如：localhost:81/prod-api/user/login 经过Nginx所转发，访问的后端接口路径为localhost:8989/prod-api/user/login
  			proxy_pass http://192.168.253.128:8989/;
  		}
  		#error_page  404              /404.html;
  
  		# redirect server error pages to the static page /50x.html
  		#
  		error_page   500 502 503 504  /50x.html;
  		location = /50x.html {
  			root   html;
  		}
  
  		# proxy the PHP scripts to Apache listening on 127.0.0.1:80
  		#
  		#location ~ \.php$ {
  		#    proxy_pass   http://127.0.0.1;
  		#}
  
  		# pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
  		#
  		#location ~ \.php$ {
  		#    root           html;
  		#    fastcgi_pass   127.0.0.1:9000;
  		#    fastcgi_index  index.php;
  		#    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
  		#    include        fastcgi_params;
  		#}
  
  		# deny access to .htaccess files, if Apache's document root
  		# concurs with nginx's one
  		#
  		#location ~ /\.ht {
  		#    deny  all;
  		#}
  	}
  
  
  	# another virtual host using mix of IP-, name-, and port-based configuration
  	#
  	#server {
  	#    listen       8000;
  	#    listen       somename:8080;
  	#    server_name  somename  alias  another.alias;
  
  	#    location / {
  	#        root   html;
  	#        index  index.html index.htm;
  	#    }
  	#}
  
  
  	# HTTPS server
  	#
  	#server {
  	#    listen       443 ssl;
  	#    server_name  localhost;
  
  	#    ssl_certificate      cert.pem;
  	#    ssl_certificate_key  cert.key;
  
  	#    ssl_session_cache    shared:SSL:1m;
  	#    ssl_session_timeout  5m;
  
  	#    ssl_ciphers  HIGH:!aNULL:!MD5;
  	#    ssl_prefer_server_ciphers  on;
  
  	#    location / {
  	#        root   html;
  	#        index  index.html index.htm;
  	#    }
  	#}
  
  }
```
	**保存之后，终端进入/usr/local/nginx/sbin目录，./nginx -s reload重启nginx**
# 后端项目部署步骤：

（参考：[http://t.csdn.cn/ghb2t](http://t.csdn.cn/ghb2t)、[https://blog.csdn.net/weixin_41064107/article/details/122653857](https://blog.csdn.net/weixin_41064107/article/details/122653857)、[https://www.jianshu.com/p/878a8b62e70f](https://www.jianshu.com/p/878a8b62e70f)）

## 1、将项目打包成jar包

		在父工程的pom文件中加以下配置

```lua
  <build>
  	<plugins>
  		<plugin>
  			<groupId>org.apache.maven.plugins</groupId>
  			<artifactId>maven-compiler-plugin</artifactId>
  			<version>3.10.1</version>
  			<configuration>
  				<source>${java.version}</source>
  				<target>${java.version}</target>
  				<encoding>${project.build.sourceEncoding}</encoding>
  			</configuration>
  		</plugin>
  		<!-- 此插件必须放在父 POM 中  -->
  		<plugin>
  			<groupId>org.apache.maven.plugins</groupId>
  			<artifactId>maven-assembly-plugin</artifactId>
  			<version>3.3.0</version>
  			<executions>
  				<!--
  				执行本插件的方法为，在主目录下执行如下命令：
  				mvn package assembly:single
  
  				对于 IntelliJ IDEA，生成的 JAR 包位于每个模块下的文件夹 target
  				-->
  				<execution>
  					<id>make-assembly</id><!-- 配置执行器 -->
  					<phase>package</phase><!-- 绑定到package生命周期阶段上 -->
  					<goals>
  						<!-- 此处 IntelliJ IDEA 可能会报红，这是正常现象  -->
  						<goal>single</goal><!-- 只运行一次 -->
  					</goals>
  				</execution>
  			</executions>
  			<configuration>
  				<archive>
  					<manifest>
  						<!-- 配置程序运行入口所在的类 -->
  						<mainClass>com.wanghui.blog.BlogReceptionApplication</mainClass>
  					</manifest>
  					<manifest>
  						<!-- 配置程序运行入口所在的类 -->
  						<mainClass>com.wanghui.blog.BlogBackGroundApplication</mainClass>
  					</manifest>
  				</archive>
  				<!-- 设置 JAR 包输出目录 -->
  				<outputDirectory>${project.build.directory}/#maven-assembly-plugin</outputDirectory>
  				<!-- 设置打包后的 JAR 包的目录结构为默认 -->
  				<descriptorRefs>
  					<descriptorRef>jar-with-dependencies</descriptorRef>
  				</descriptorRefs>
  			</configuration>
  		</plugin>
  	</plugins>
  </build>
  再有启动类的子项目pom文件下加以下配置
  <build>
  	<plugins>
  		<plugin>
  			<groupId>org.springframework.boot</groupId>
  			<artifactId>spring-boot-maven-plugin</artifactId>
  			<version>2.2.6.RELEASE</version>
  			<executions>
  				<execution>
  					<goals>
  						<goal>repackage</goal>
  					</goals>
  				</execution>
  			</executions>
  		</plugin>
  	</plugins>
  	<finalName>${project.artifactId}</finalName>
  </build>
```
		**问题1：mvn intall报错Type org.springframework.boot.maven.RepackageMojo not present**
			子项目pom文件中spring-boot-maven-plugin的版本默认为3.0.0,版本不匹配，指定版本2.2.6.RELEASE

```lua
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-maven-plugin</artifactId>
  <version>2.2.6.RELEASE</version>
```
## 2、上传jar包到服务器

## 3、安装jdk环境

### 1.卸载原有的jdk

```lua
  #检查当前机器是否有自带的JDK
    rpm -qa |grep java
    rpm -qa |grep jdk
    rpm -qa |grep gcj
  #如果没有 则跳至安装步骤，有的话进行卸载
    rpm -qa | grep java | xargs rpm -e --nodeps
  #检测卸载是否成功
    java -version
  #出现一下提示则说明没有安装JDK或者已经卸载成功
    -bash: java: command not found
```
### 2.下载安装JDK(下载地址：[http://www.codebaoku.com/jdk/jdk-oracle-jdk1-8.html#jdk8u333)](http://www.codebaoku.com/jdk/jdk-oracle-jdk1-8.html#jdk8u333))

```lua
  下载完成后上传到服务器指定目录(/usr/local/java)
  解压JDK压缩包
    tar -zxvf jdk-8u192-linux-x64.tar.gz 
  配置环境变量
    #编辑配置文件：vim /etc/profile
    #添加JDK配置
    	#jdk配置
    	export JAVA_HOME=/usr/local/java/jdk1.8.0_333
    	export JRE_HOME=$JAVA_HOME/jre
    	export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH
    	export CLASSPATH=$CLASSPATH:.:$JAVA_HOME/lib:$JRE_HOME/lib
    #按esc退出按wq!保存退出
    #刷新配置：source /etc/profile
  检查JDK是否安装成功
    #查看JDK版本：java -version
```
### 3.运行jar包(参考：[https://www.cnblogs.com/xuyaowen/p/8798748.html)](https://www.cnblogs.com/xuyaowen/p/8798748.html))

```lua
  后台运行：nohup java -jar wanghui-admin.jar
  		   nohup java -jar wanghui-blog.jar
  注意：nohup 的功能和& 之间的功能并不相同。其中，nohup 可以使得命令永远运行下去和用户终端没有关系。当我们断开ssh 连接的时候不会影响他的运行。而& 表示后台运行。当ssh 断开连接的时候（用户退出或挂起的时候），命令也自动退出。
```
### 4.安装mysql环境

(https://blog.csdn.net/qq_52433326/article/details/125669370、[https://www.cnblogs.com/myibm/p/10731060.html)](https://www.cnblogs.com/myibm/p/10731060.html))

#### 1.安装前准备

```lua
	卸载已安装的MySQL
		检查是否已安装 MySQL：rpm -qa | grep mysql  
		如有返回信息则执行以下命令，删除已有的：rpm -e --nodeps [返回信息]
	卸载默认安装的数据库
		检查是否已安装 mariadb：rpm -qa | grep mariadb
		如已安装则卸载：rpm -e mariadb-libs-5.5.56-2.el7.x86_64 --nodeps
	清除已建立的文件夹
		查询所有 MySQL 对应的文件夹
			whereis mysql  
			sudo find / -name mysql  
		删除目录及文件：sudo rm -rf [查询结果1] [查询结果2]
	检查 MySQL 用户组和用户是否存在，如果没有，则创建
		检查
			cat /etc/group | grep mysql
			cat /etc/passwd |grep mysql
		创建
			sudo groupadd mysql
			sudo useradd -r -g mysql mysql
		创建mysql安装目录并进入该目录
			sudo mkdir /usr/local/mysql
			cd /usr/local/mysql
	下载并解压 MySQL 安装包
		下载:https://dev.mysql.com/downloads/mysql/ 手动下载  上传到/usr/local/mysql
			下载教程：https://blog.csdn.net/qq_34292044/article/details/125397035
		解压:tar -xvf mysql-8.0.32-1.el7.x86_64.rpm-bundle.tar
```
#### 2.MySQL 的安装及配置(MySQL 的安装需要有准确的顺序：COMMON -> LIB -> CLIENT -> SERVER)

```lua
	安装MySQL
		sudo rpm -ivh mysql-community-common-8.0.32-1.el7.x86_64.rpm --nodeps --force
		sudo rpm -ivh mysql-community-libs-8.0.32-1.el7.x86_64.rpm --nodeps --force
		sudo rpm -ivh mysql-community-client-8.0.32-1.el7.x86_64.rpm --nodeps --force
		sudo rpm -ivh mysql-community-server-8.0.32-1.el7.x86_64.rpm --nodeps --force
	对 MySQL 数据库进行初始化和相关配置:sudo mysqld --initialize;sudo chown mysql:mysql /var/lib/mysql -R;sudo systemctl start mysqld.service;systemctl enable mysqld;
	查看默认密码
		sudo cat /var/log/mysqld.log | grep password
		注：2022-07-07T11:23:26.331281Z 6 [Note] [MY-010454] [Server] A temporary password is generated for root@localhost: 后的内容就是密码
	登录：mysql -uroot -p[密码]

	修改密码：因为 MySQL对密码强度有要求，但是修改要求之前要先修改初始密码，先设置一个密码：
		ALTER USER USER() IDENTIFIED BY 'Root_12root';
		输入以下命令可以查看当前密码要求：SHOW VARIABLES LIKE 'validate_password%';
		输入以下命令调整密码要求（MySQL8），第一个参数为验证强度，第二个参数为密码的最小长度：
			set global validate_password.policy=0;
			set global validate_password.length=4;
		设置密码：ALTER USER USER() IDENTIFIeD BY '123456';
		修改完密码以后我们输入：exit
	密码相关配置
		授权远程访问
		create user 'root'@'%' identified with mysql_native_password by '123456';
		grant all privileges on *.* to 'root'@'%' with grant option;
		flush privileges; 
		(注意：打开my.cnf文件,找到 [mysqld],在其下方添加上一行 skip-grant-tables,然后保存)
		修改加密规则(部分可视化工具，不支持最新版本 mysql 8.0 加密规则，会导致无法链接。)
			ALTER USER 'root'@'localhost' IDENTIFIED BY '123456' PASSWORD EXPIRE NEVER;
			flush privileges; 
		设置密码永不过期：alter user 'root'@'%' identified by '123456' password expire never;
		退出 MySQL：exit;
	启动mysql：systemctl start mysqld/systemctl enable mysqld(自启动)
		启动：service mysqld start
		停止：service mysqld stop
		重启：service mysqld restart
```
**问题1：本地navcat连接服务器上的mysql服务时报错1045.(新建一个用户就可以了)**
```lua
  使用查看用户命令发现root用户还有两个连接方式，其中localhost仅本地访问，所以报错1045
  查看用户：SELECT DISTINCT CONCAT('User: ''',user,'''@''',host,''';') AS query FROM mysql.user;
  		  select user,host,plugin from mysql.user;
      user : 用户名
      host : 连接方式 (%所有客户端都可访问, 192.168.239.% 同网段可访问, localhost 仅本地访问)
      plugin : 密码加密插件 
  查看具体用户的权限：
      show grants for 'root'@'%';
  授权远程访问
      create user 'wanghui'@'%' identified with mysql_native_password by '123456';
      grant all privileges on *.* to 'wanghui'@'%' with grant option;
```
**问题2：由于问题1，查询网上说在my.cnf文件中 [mysqld]下加上skip-grant-tables,然后保存后报错2003.**
```lua
  skip-grant-tables直接过滤了权限表，导致无法远程连接
  不能加skip-grant-tables，重新修改my.cnf配置文件，删除skip-grant-tables
```
### 5.下载安装redis

#### 1.下载地址：

```lua
  https://redis.io/download/
  wget http://219.238.7.66/files/502600000A29C8D5/download.redis.io/releases/redis-3.2.9.tar.gz
```
#### 2.安装：(将安装包上传到/usr/local/redis下)

```lua
  解压：tar -zxvf redis-3.2.9.tar.gz
  切换目录： cd redis-3.2.9，执行命令：make
  make命令执行过程中可能报错，根据控制台输出的错误信息进行解决
  错误一：gcc命令找不到，是由于没有安装gcc导致
  	gcc是GNU compiler collection的缩写，它是Linux下一个编译器集合，是c或c++程序的编译器。
  	使用yum进行安装，命令：yum -y install gcc
  错误二： error: jemalloc/jemalloc.h: No such file or directory
  	内存分配器使用libc，执行 make MALLOC=libc
  	注意：安装完gcc之后，再执行make，先执行 make distclean 清理一下上次make后产生的文件
  有人在make执行之后再执行 make install，该操作则将 src下的许多可执行文件复制到/usr/local/bin 目录下(可选)
```
#### 3.启动

```lua
  切换到 redis-3.2.9/src/ 目录执行命令
  后台启动：
    ./redis-server &
  后台启动并输出日志到nohup.out文件：
    nohup /usr/local/redis3.2.9/src/redis-server &
  	nohup redis-server redis.conf &
```
#### 4.关闭

```lua
  切换到 redis-3.2.9/src/ 目录执行：./redis-cli shutdown
  kill pid 或者 kill -9 pid
  kill杀掉进程这种方式比较粗暴
```
#### 5.redis客户端

```lua
  redis命令行客户端：
    redis-cli（Redis Command Line Interface）是Redis自带的基于命令行的Redis客户端，用于与服务端交互，我们可以使用该客户端来执行redis的各种命令
  直接连接redis (默认ip127.0.0.1，端口6379)：
      ./redis-cli
  指定IP和端口连接redis：
      ./redis-cli -h 127.0.0.1 -p 6379
```
#### 6、redis远程客户端(下载官网：[https://redisdesktop.com/)](https://redisdesktop.com/))

			Redis Desktop Manager

#### 7、redis安全

```lua
  设置密码：设置redis密码，在redis.conf文件配置 requirepass 123456
      注意：因为redis 速度相当快，所以在一台比较好的服务器下，一个外部的用户可以在一秒钟进行150K 次的密码尝试，这意味着你需要指定非常非常强大的密码来防止暴力破解。
      客户端连接则需要密码：AUTH 123456 或者 redis-cli -h 127.0.0.1 -p 6379 -a123456
  绑定ip
    把# bind 127.0.0.1前面的 注释#号去掉，然后把127.0.0.1改成允许访问你redis服务器的ip地址，表示只允许该ip进行访问
  命令禁止或重命名
    在redis.conf文件中进行命令禁止或重命名配置
  	rename-command FLUSHALL oyfekmjvmwxq5a9c8usofuo369x0it2k #重命名FLUSHALL命令
  注意：对于FLUSHALL命令，需要保证appendonly.aof文件没有flushall命令，否则服务器无法启动
  	rename-command FLUSHALL "" # 禁用FLUSHALL命令
  	rename-command FLUSHDB "" #禁止FLUSHDB命令
  	rename-command CONFIG b840fc02d524045429941cc15f59e41cb7be6c52
  # 重命名CONFIG命令：
     rename-command CONFIG "" #禁止CONFIG命令
    修改默认端口：修改redis的端口，这一点很重要，使用默认的端口很危险，redis.conf中修改port 6379 将其修改为自己指定的端口（可随意）
```
### 6.运行之前打包好上传到服务器的jar包

```lua
  运行博客系统前台项目：
      nohup java -jar wanghui-blog.jar &
  运行博客系统后台项目：
      nohup java -jar wanghui-admin.jar &
  查看服务的端口号码：
      netstat -nultp	
```
### 7.总结

```lua
  每次服务器重启后需要重新启动所有服务
  	  先查一下有没有启动：ps -ef |grep java/nginx/redis/mysql
  前端项目:重启nginx就行
    	进入：cd /usr/local/nginx/sbin/
    	执行：./nginx
  启动redis:
    	进入：cd /usr/local/redis/redis-3.2.9/src/
    	执行：nohup redis-server redis.conf &
  启动mysql：
      service mysqld start		
  启动后端项目：
    	进入：cd /root/self-grows/self-blog/wanghui-blog
    	执行：nohup java -jar wanghui-blog.jar &
    	进入：cd /root/self-grows/self-blog/wanghui-admin
    	执行：nohup java -jar wanghui-admin.jar &
```
		
