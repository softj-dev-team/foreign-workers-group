package com.softj.pwg.config;

import com.softj.pwg.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
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
                .excludePathPatterns("/")
                .excludePathPatterns("/login")
                .excludePathPatterns("/api/login")
                .excludePathPatterns("/api/admin/**")
                .excludePathPatterns("/api/setNation")
                .excludePathPatterns("/api/getPage")
                .excludePathPatterns("/api/getCommentPage")
                .excludePathPatterns("/api/comFileDownload/**")
                .excludePathPatterns("/search")
                .excludePathPatterns("/board/list")
                .excludePathPatterns("/board/view")
                .excludePathPatterns("/assets/**")
                .excludePathPatterns("/admin")
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/error")
                .addPathPatterns("/**");

        registry.addInterceptor(new NationInterceptor())
                .excludePathPatterns("/board/view")
                .excludePathPatterns("/error")
                .addPathPatterns("/board/**")
                .addPathPatterns("/search/**")
                .addPathPatterns("/mypage/**");

        registry.addInterceptor(new AdminInterceptor())
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/api/admin/login")
                .excludePathPatterns("/error")
                .addPathPatterns("/admin/**");

        registry.addInterceptor(IpBlockInterceptor())
                .excludePathPatterns("/error")
                .excludePathPatterns("/assets/**")
                .addPathPatterns("/**");
    }

    @Bean
    public IpBlockInterceptor IpBlockInterceptor(){
        return new IpBlockInterceptor();
    }
}
