package com.wsk.parent.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wsk.ggkt.model.live.LiveCourseDescription;
import com.wsk.ggkt.model.vod.CourseDescription;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author wsk
 * @since 2022-08-22
 */
public interface LiveCourseDescriptionService extends IService<LiveCourseDescription> {
    //通过courseID获取描述信息
    LiveCourseDescription getByCourseId(Long id);
}
