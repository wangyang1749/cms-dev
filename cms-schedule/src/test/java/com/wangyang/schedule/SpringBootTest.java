package com.wangyang.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.wangyang.data.repository"})
@EntityScan(basePackages = {"com.wangyang.pojo.entity"})
@ComponentScan(basePackages = {"com.wangyang.schedule","com.wangyang.service"})
public class SpringBootTest {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTest.class, args);
    }
}