package com.wsk.parent.live.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wsk.ggkt.model.live.LiveCourseAccount;

/**
 * <p>
 * 直播课程账号表（受保护信息） 服务类
 * </p>
 *
 * @author wsk
 * @since 2022-08-22
 */
public interface LiveCourseAccountService extends IService<LiveCourseAccount> {

    //查看直播课程 （主播的）账号信息
    LiveCourseAccount getLiveCourseAccountCourseId(Long id);
}
