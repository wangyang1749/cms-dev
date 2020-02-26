package com.wangyang.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CmsApplication {

	public static void main(String[] args) {
		// Customize the spring config location
		System.setProperty("spring.config.additional-location", "file:${user.home}/cms/cms.properties");
		SpringApplication.run(CmsApplication.class, args);
	}

}
