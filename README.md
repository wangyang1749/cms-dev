# Create Cms Project
----------------
# Dependency
+ `spring-boot-starter-web`
+ `spring-boot-starter-thymeleaf`
+ `spring-boot-starter-data-jpa`
+ `spring-boot-starter-test`
+ `mysql-connector-java`
+  `h2`
# Project Basic Structure
```
.
└── com
    └── wangyang
        └── cms
            ├── CmsApplication.java
            ├── config
            ├── controller
            ├── core
            ├── expection
            ├── filter
            ├── listener
            ├── pojo
            │   ├── dto
            │   ├── entity
            │   ├── params
            │   ├── support
            │   └── vo
            ├── repository
            ├── service
            │   └── impl
            └── utils

```
# Project Configuration

+ spring boot 
# Load External Resource
+ Static resource Loading

> If `file:${user.home}/.cms/` did not go to `classpath:/html/`

```properties
spring.resources.static-locations= file:${user.home}/.cms/, classpath:/static/
```

+ Configuration file loading
```
 --spring.config.location=/home/wy/.cms/application.properties 
```

```java
@SpringBootApplication
public class CmsApplication {

	public static void main(String[] args) {
		// Customize the spring config location
		System.setProperty("spring.config.additional-location", "file:${user.home}/.cms/cms.properties");
		SpringApplication.run(CmsApplication.class, args);
	}
	
}
```
### Query sql
+ three table query 
```mysql-sql
select article.*,tags.*,category.*
from article
left join article_tags 
on  article_tags.article_id=article.id
left join tags
on article_tags.tags_id = tags.id
left join article_category
on article_category.article_id=article.id
left join category
on article_category.category_id=category.id
```

# rrr 

```java
File file = new File("Nginx所在虚拟机下的目标文件",  ".html");
try(PrintWriter writer = new PrintWriter(file,"UTF-8")){ //流在小括号里面会被自动的释放
    templateEngine.process("1",context,writer);
}catch (Exception e){
}
```

# Reference
+ 剪切板粘贴上传图片并返回URl显示图片
<https://blog.csdn.net/qq_33280890/article/details/84799006>