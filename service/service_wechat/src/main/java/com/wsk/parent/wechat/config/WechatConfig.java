package com.wsk.parent.wechat.config;

import com.wsk.parent.wechat.utils.ConstantPropertiesUtil;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author:WuShangke
 * @create:2022/8/21-15:11
 */
@Configuration
public class WechatConfig {

    @Autowired
    private ConstantPropertiesUtil constantPropertiesUtil;


    @Bean
    public WxMpService wxMpService(){
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){
        WxMpDefaultConfigImpl wxMpDefaultConfig = new WxMpDefaultConfigImpl();
        wxMpDefaultConfig.setAppId(ConstantPropertiesUtil.ACCESS_KEY_ID);
        wxMpDefaultConfig.setSecret(ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        return wxMpDefaultConfig;
    }
}
