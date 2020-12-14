package com.sxt.shop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sxt.shop.mapper")
public class AdminApp {
	public static void main(String[] args) throws Exception {
		// 启动项目
		SpringApplication.run(AdminApp.class, args);
	}

}
