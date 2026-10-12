package com.lee.android_device_control;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.TimeZone;

@EnableJpaRepositories
@EnableJpaAuditing
@SpringBootApplication
public class AndroidDeviceControlApplication {

    @PostConstruct
    public void init() {
        // 서버의 시간대를 무조건 한국 시간으로 고정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
    public static void main(String[] args) {
        SpringApplication.run(AndroidDeviceControlApplication.class, args);
    }

}
