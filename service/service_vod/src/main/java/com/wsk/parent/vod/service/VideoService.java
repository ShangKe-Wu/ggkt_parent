package com.wsk.parent.vod.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wsk.ggkt.model.vod.Video;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
public interface VideoService extends IService<Video> {

    //通过课程ID删除小节
    void removeByCourseId(Long id);

    //通过小节ID删除小节，同时删除腾讯云的视频
    void removeVideoById(Long id);
}
