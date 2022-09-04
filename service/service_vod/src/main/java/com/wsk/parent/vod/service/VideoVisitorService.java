package com.wsk.parent.vod.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wsk.ggkt.model.vod.VideoVisitor;

import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 服务类
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
public interface VideoVisitorService extends IService<VideoVisitor> {

    //统计数据：某个课程在某个时间段内的观看数据
    Map<String, Object> findCount(Long courseId, String startDate, String endDate);
}
