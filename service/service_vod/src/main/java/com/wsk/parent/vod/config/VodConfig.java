package com.wsk.parent.vod.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author:WuShangke
 * @create:2022/8/14-13:51
 *腾讯云密钥
 * SecretId: --
 *
 * SecretKey: ---
 */
@Configuration
@MapperScan("com.wsk.parent.vod.mapper")
public class VodConfig {
    //分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
