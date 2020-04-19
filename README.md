
### Create Cms Project
内容管理项目, 由后台控制实现前台页面静态化

### 项目展示
+ 目前我已将本项目部署在自己的小型服务器里

<http://www.bioinfo.online/>

+ 后台界面
![](https://wangyang-bucket.oss-cn-beijing.aliyuncs.com/image-bed/5406acbf-de2f-418b-866f-3d113fad41a9.png)
![](https://wangyang-bucket.oss-cn-beijing.aliyuncs.com/image-bed/593ceb55-19c9-4b9b-9612-c8f8fc771a66.png)
![](https://wangyang-bucket.oss-cn-beijing.aliyuncs.com/image-bed/c18cfb8d-023a-4881-a82a-d4291f7695ef.png)
+ 静态化的前台界面
![](https://wangyang-bucket.oss-cn-beijing.aliyuncs.com/image-bed/d68e9599-496a-470f-86ac-7926374ea56e.png)
![](https://wangyang-bucket.oss-cn-beijing.aliyuncs.com/image-bed/6a358f19-2db8-4afc-a2bb-d0afff8e676a.png)
![](https://wangyang-bucket.oss-cn-beijing.aliyuncs.com/image-bed/94535ba9-4d75-4792-b6b2-53eac38a1ee9.png)
![](https://wangyang-bucket.oss-cn-beijing.aliyuncs.com/image-bed/9fac9998-c498-4742-b9a8-f42bcc29d82f.png)
### Project Configuration
1. 本项目依赖两个模块
+ 使用vue实现的后界面
<https://github.com/wangyang1749/cms-admin.git> <br> 
+ 前端静态化使用的模板文件
<https://github.com/wangyang1749/cms-template.git> <br>
2. 在前台的静态化上, 本项目需要`Nginx`的支持
+ 由于头部和底部信息是随时会发生变化的, 因此需要`Nginx`
帮助实现动态引入
+ nginx的配置可以参考如下:

```
server {
	listen 80 default_server;
	listen [::]:80 default_server;
	index index.html index.htm index.nginx-debian.html;
	server_name _;
	location / {
		root /home/wy/cms/html;
		ssi on;

		try_files $uri $uri/ =404;
	}
	location /templates {
		root /home/wy/cms;

		try_files $uri $uri/ =404;
	}
}

```

### 说明
1. 本程序需要`activemq`的`61616`端口在服务器运行
+ 文章的静态化采用的是`activemq完成的` 
2. 引入`flexmark`原因
+ markdown中要为图片添加div包裹, 实现图片的居中
+ 执行nodejs实现`Katex`和`mermaid`的服务端渲染
3. 关于PDF的导出
+ 由于采用`nodejs`的`puppeteer`模块, 因此服务器必须安装`nodejs`

### 更新记录
2020.3.23
1. 更新文章和分类的关系变换为一对多（一篇文章只能在一个分类中）
2. 新增栏目功能

2020.3.17
1. 添加一键更新所有Category HTML功能
2. 添加设置文章默认模板的功能

2020.3.9
1. 实现导出文章PDF
2. 删除本程序采用socket对`nodejs`渲染`Katex`和`mermaid`的远程调用, 
改为java调用本地命令的方式实现
3. 将授权模块分离出来

 2020.3.7 
1. 实现markdown添加图片包裹一层div
2. 修复文章更新不能删除旧分类文章列表中的文章标题
3. 增加重新生成所有文章Html功能
