package com.yhh;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.yhh.mapper")
@EnableCaching
public class YhhApplication {

	public static void main(String[] args) {
		SpringApplication.run(YhhApplication.class, args);
	}

}
