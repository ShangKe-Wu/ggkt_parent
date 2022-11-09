package com.wsk.parent.vod.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wsk.ggkt.model.vod.Video;
import com.wsk.parent.vod.mapper.VideoMapper;
import com.wsk.parent.vod.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsk.parent.vod.service.VodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private VodService vodService;

    //通过课程ID删除小节，同时删除腾讯云的视频
    @Override
    public void removeByCourseId(Long id) {
        LambdaQueryWrapper<Video> queryWrapperForVideo = new LambdaQueryWrapper<>();
        queryWrapperForVideo.eq(Video::getCourseId,id);
        List<Video> videos = videoMapper.selectList(queryWrapperForVideo);
        //删除腾讯云视频
        //TODO 优化：修改成不是遍历删除
        for(Video video : videos){
            String videoSourceId = video.getVideoSourceId();
            if(!StringUtils.isEmpty(videoSourceId)){
                vodService.removeVideo(videoSourceId);
            }
        }
        //删除数据库信息
        videoMapper.delete(queryWrapperForVideo);
    }

    //通过小节ID删除小节，同时删除腾讯云的视频
    @Override
    public void removeVideoById(Long id) {
        Video video = videoMapper.selectById(id);
        //删除腾讯云
        if(video!=null){
            String videoSourceId = video.getVideoSourceId();
            if(!StringUtils.isEmpty(videoSourceId)){
                vodService.removeVideo(videoSourceId);
            }
        }
        //删除数据库
        videoMapper.deleteById(id);
    }
}
