package com.wsk.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @author:WuShangke
 * @create:2022/8/20-20:35
 */
@Configuration
public class CorsConfig {
    //处理跨域问题
    @Bean
    public CorsWebFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedMethod("*");// 是什么请求方法，比如GET POST PUT DELETE
        config.addAllowedOrigin("*");// 来自哪个域名的请求，*号表示所有
        config.addAllowedHeader("*");// 是什么请求头部
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
