package com.wangyang.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@ComponentScan(basePackages = {"com.wangyang.common","com.wangyang.data","com.wangyang.cms","com.wangyang.authorize","com.wangyang.syscall.controller"})
@EnableJpaRepositories(basePackages = {"com.wangyang.data.repository"})
@EntityScan(basePackages = {"com.wangyang.model.pojo.entity"})
@EnableCaching
public class CmsApplication {

	public static void main(String[] args) {
		// Customize the spring config location
		System.setProperty("spring.config.additional-location", "file:${user.home}/cms/cms.properties");
		SpringApplication.run(CmsApplication.class, args);
	}

}
