package com.wangyang.data;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.wangyang.data"})
@EnableJpaRepositories(basePackages = {"com.wangyang.data.repository"})
@EntityScan(basePackages = {"com.wangyang.model.pojo.entity"})
public class TestApplication {

}
