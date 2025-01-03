package com.blog.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//cors
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration configuration = new CorsConfiguration();

        //1.setAllowedOrigins - 도메인 허용
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));

        //2.setAllowedMethods - 허용 http
        configuration.setAllowedMethods(Arrays.asList("GET","POST","OPTIONS","PATCH", "DELETE"));

        //3.setAllowedHeaders - 허용 Headers
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));

        //4.UIL 기본설정
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        //5.return
        return source;
    }
}
