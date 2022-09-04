package com.wsk.parent.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wsk.ggkt.model.user.UserInfo;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author wsk
 * @since 2022-08-20
 */
public interface UserInfoService extends IService<UserInfo> {

    //通过openId获取用户
    UserInfo getByOpenid(String openId);
}
