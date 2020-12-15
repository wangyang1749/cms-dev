package com.wangyang.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJms
@ComponentScan(basePackages = {"com.wangyang.common","com.wangyang.service","com.wangyang.web","com.wangyang.authorize","com.wangyang.syscall.controller","com.wangyang.schedule"})
@EnableJpaRepositories(basePackages = {"com.wangyang.service.repository"})
@EntityScan(basePackages = {"com.wangyang.pojo.entity"})
@EnableCaching
@EnableAsync
public class CmsApplication {

	public static void main(String[] args) {
		// Customize the spring config location
		System.setProperty("spring.config.additional-location", "file:${user.home}/cms/cms.properties");
		SpringApplication.run(CmsApplication.class, args);
	}

}
