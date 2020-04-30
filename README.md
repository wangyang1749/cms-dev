
### 一、内容（文章）管理系统介绍
本项目使用SpringBoot作为后端；使用thymeleaf作为模板引擎，实现全部页面静态化；
使用vue开发管理员界面。

### 二、技术概况
#### 2.1 页面公共部分的处理
```java
    private static  String pattern = "<!--#include file=\"(.*?)\"-->";
    private static String varPattern = "<!--\\{\\{(.*?)}}-->";
```
采用正则表达式匹配`    <!--#include file="/components/header.html"-->`，找到文件之后，读取替换。

这个插入语句是nginx的，所有将静态目录设置在nginx可以不通过服务器，直接访问静态页面

#### 2.2 分页静态化的实现
```java
    @GetMapping("/{categoryPath}/{categoryViewName}/page-{page}.html")
    public String articleListBy(HttpServletRequest request, @PathVariable("categoryPath") String categoryPath, @PathVariable("categoryViewName") String categoryViewName, @PathVariable("page") Integer page){
        File file = new File(CmsConst.WORK_DIR+"/html/"+categoryPath+"/"+categoryViewName+"/"+page+".html");
        String result = null;
        if(file.exists()){
            result = FileUtils.convert(file,request);
        }else {
            Category category = categoryService.findByViewName(categoryViewName);
            if (category!=null){
                String resultHtml = htmlService.convertArticleListBy(category,page);
               result =  FileUtils.convertByString(resultHtml,request);
            }
        }
        if (request!=null){
            return result;
        }
        System.out.println(categoryViewName);
        System.out.println(page);
        return  "Page is not found!";
    }
```
当第一次访问分页页面时，会生成其html页面。当第二次访问时，判断文件存在就直接文件返回。

在对文章增删改时会删除生成的分类列表缓存。

#### 2.3 权限管理



### 三、项目部署
#### 3.1 前提条件
1. 本程序需要`activemq`的`61616`端口在服务器运行，文章的静态化采用的是`activemq完成的` 
2. 需要将模板文件<https://github.com/wangyang1749/cms-template.git>拷贝在用户目录的cms下
3. 根据配置文件，配置数据库

#### 3.2 其他模块
1. 使用vue实现的后界面，源码参考如下
<https://github.com/wangyang1749/cms-admin.git> <br> 

2. 原生android调用api，项目地址如下（正在开发中）
<https://github.com/wangyang1749/cms-android.git>
+ 前端静态化使用的模板文件



### 四、系统扩展
1. 关于PDF的导出
+ 由于采用`nodejs`的`puppeteer`模块, 因此服务器必须安装`nodejs`
2. latex公式插入

### 五、更新记录
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

### 六、项目展示
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
