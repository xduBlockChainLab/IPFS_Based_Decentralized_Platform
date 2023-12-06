package com.example.paas_decentralized_webplatform;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@CrossOrigin
@MapperScan("com.example.paas_decentralized_webplatform.dao")
public class PaaSDecentralizedWebPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaaSDecentralizedWebPlatformApplication.class, args);
    }

}
