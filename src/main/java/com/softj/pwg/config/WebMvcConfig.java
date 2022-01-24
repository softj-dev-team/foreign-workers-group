package com.softj.pwg.config;

import com.softj.pwg.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PwgInterceptor())
                .excludePathPatterns("/login")
                .excludePathPatterns("/api/login")
                .excludePathPatterns("/assets/**")
                .addPathPatterns("/**");
    }
}
