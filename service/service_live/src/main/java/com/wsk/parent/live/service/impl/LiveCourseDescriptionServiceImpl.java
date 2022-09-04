package com.wsk.parent.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wsk.ggkt.model.live.LiveCourseDescription;
import com.wsk.ggkt.model.vod.CourseDescription;
import com.wsk.parent.live.mapper.LiveCourseDescriptionMapper;
import com.wsk.parent.live.service.LiveCourseDescriptionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程简介 服务实现类
 * </p>
 *
 * @author wsk
 * @since 2022-08-22
 */
@Service
public class LiveCourseDescriptionServiceImpl extends ServiceImpl<LiveCourseDescriptionMapper, LiveCourseDescription> implements LiveCourseDescriptionService {

    @Override
    public LiveCourseDescription getByCourseId(Long id) {
        LambdaQueryWrapper<LiveCourseDescription> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(LiveCourseDescription::getLiveCourseId,id);
        LiveCourseDescription liveCourseDescription = baseMapper.selectOne(queryWrapper);
        return liveCourseDescription;
    }
}
