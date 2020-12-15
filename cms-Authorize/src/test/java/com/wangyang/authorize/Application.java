package com.wangyang.authorize;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.wangyang.service","com.wangyang.authorize"})
@EnableJpaRepositories(basePackages = {"com.wangyang.service.repository"})
@EntityScan(basePackages = {"com.wangyang.pojo.entity"})
public class Application {
}
