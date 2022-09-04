package com.wsk.parent.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wsk.ggkt.model.live.LiveCourseConfig;
import com.wsk.parent.live.mapper.LiveCourseConfigMapper;
import com.wsk.parent.live.service.LiveCourseConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 直播课程配置表 服务实现类
 * </p>
 *
 * @author wsk
 * @since 2022-08-22
 */
@Service
public class LiveCourseConfigServiceImpl extends ServiceImpl<LiveCourseConfigMapper, LiveCourseConfig> implements LiveCourseConfigService {

    //通过课程ID查询课程配置
    @Override
    public LiveCourseConfig getByCourseId(Long id) {
        LambdaQueryWrapper<LiveCourseConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LiveCourseConfig::getLiveCourseId,id);
        LiveCourseConfig liveCourseConfig = baseMapper.selectOne(queryWrapper);
        return liveCourseConfig;
    }
}
