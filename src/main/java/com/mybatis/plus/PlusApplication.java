package com.mybatis.plus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 *
 * 描述: 项目启动类
 *
 * @author 官萧何
 * @date 2020/8/17 11:37
 * @version V1.0
 */
@SpringBootApplication
@EnableFeignClients(basePackages= {"com.mybatis.plus.web.client"})
@EnableHystrix
@EnableScheduling
public class PlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlusApplication.class, args);
    }

}
