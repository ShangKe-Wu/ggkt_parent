package com.wsk.parent.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wsk.ggkt.model.live.LiveCourseConfig;

/**
 * <p>
 * 直播课程配置表 服务类
 * </p>
 *
 * @author wsk
 * @since 2022-08-22
 */
public interface LiveCourseConfigService extends IService<LiveCourseConfig> {
    //通过课程ID查询课程配置
    LiveCourseConfig getByCourseId(Long id);
}
