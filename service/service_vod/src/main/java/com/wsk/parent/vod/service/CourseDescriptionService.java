package com.wsk.parent.vod.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wsk.ggkt.model.vod.CourseDescription;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
public interface CourseDescriptionService extends IService<CourseDescription> {

    //通过课程ID删除课程描述
    void removeByCourseId(Long id);

    //根据课程ID查询课程描述信息
    CourseDescription selectByCourseId(Long courseId);
}
