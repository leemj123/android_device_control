package com.lee.android_device_control.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")          // 모든 URL 경로에 대해
                .allowedOriginPatterns("*") // 모든 오리진 허용 (allowedOrigins("*") 대신 사용 권장)
                .allowedMethods("*")        // GET, POST, PUT, DELETE 등 모든 메서드 허용
                .allowedHeaders("*")        // 모든 헤더 허용
                .allowCredentials(true);    // 쿠키나 인증 헤더 허용
    }
}
