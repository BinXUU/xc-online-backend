package com.xuecheng.ossfilesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author BinXU
 * @version 1.0.0
 * @ClassName ossFileSystemApplication.java
 * @Description TODO
 */

@SpringBootApplication//扫描所在包及子包的bean，注入到ioc中
@EntityScan("com.xuecheng.framework.domain.filesystem")//扫描实体类
@ComponentScan(basePackages={"com.xuecheng.api"})//扫描接口
@ComponentScan(basePackages={"com.xuecheng.framework"})//扫描framework中通用类
@ComponentScan(basePackages={"com.xuecheng.ossfilesystem"})//扫描本项目下的所有类
public class ossFileSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ossFileSystemApplication.class,args);
    }

}
