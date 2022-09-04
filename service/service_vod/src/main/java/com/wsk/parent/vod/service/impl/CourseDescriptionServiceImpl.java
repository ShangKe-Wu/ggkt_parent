package com.wsk.parent.vod.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wsk.ggkt.model.vod.CourseDescription;
import com.wsk.parent.vod.mapper.CourseDescriptionMapper;
import com.wsk.parent.vod.service.CourseDescriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程简介 服务实现类
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@Service
public class CourseDescriptionServiceImpl extends ServiceImpl<CourseDescriptionMapper, CourseDescription> implements CourseDescriptionService {

    @Autowired
    CourseDescriptionMapper courseDescriptionMapper;

    //通过课程ID删除课程描述
    @Override
    public void removeByCourseId(Long id) {
        LambdaQueryWrapper<CourseDescription> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(CourseDescription::getCourseId,id);
        courseDescriptionMapper.delete(queryWrapper);
    }

    //根据课程ID查询课程描述信息
    @Override
    public CourseDescription selectByCourseId(Long courseId) {
        LambdaQueryWrapper<CourseDescription> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CourseDescription::getCourseId,courseId);
        CourseDescription courseDescription = courseDescriptionMapper.selectOne(queryWrapper);
        return courseDescription;
    }
}
