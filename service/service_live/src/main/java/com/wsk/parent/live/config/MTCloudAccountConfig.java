package com.wsk.parent.live.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author:WuShangke
 * @create:2022/8/22-15:32
 */
@Component
@ConfigurationProperties(prefix = "mtcloud")
@Data
public class MTCloudAccountConfig {
    private String openId;
    private String openToken;
}
