package com.wsk.parent.vod.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wsk.ggkt.model.vod.Chapter;
import com.wsk.ggkt.model.vod.Video;
import com.wsk.ggkt.vo.vod.ChapterVo;
import com.wsk.ggkt.vo.vod.VideoVo;
import com.wsk.parent.vod.mapper.ChapterMapper;
import com.wsk.parent.vod.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsk.parent.vod.service.VideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {
    @Autowired
    ChapterMapper chapterMapper;
    //小节的服务
    @Autowired
    VideoService videoService;

    //获取章节、小节列表
    @Override
    public List<ChapterVo> getNestedTreeList(Long courseId) {
        List<ChapterVo> chapterVoList = new ArrayList<>();
        //获取章节信息
        LambdaQueryWrapper<Chapter> queryWrapperChapter = new LambdaQueryWrapper<>();
        queryWrapperChapter.eq(Chapter::getCourseId,courseId);
        queryWrapperChapter.orderByAsc(Chapter::getSort, Chapter::getId);
        List<Chapter> chapterList = chapterMapper.selectList(queryWrapperChapter);
        //获取课程所有的小结信息
        LambdaQueryWrapper<Video> queryWrapperVideo = new LambdaQueryWrapper<>();
        queryWrapperVideo.eq(Video::getCourseId,courseId);
        queryWrapperVideo.orderByAsc(Video::getSort, Video::getId);
        List<Video> videoList = videoService.list(queryWrapperVideo);

        //TODO 下面可不可以优化？
        //填充章节列表，然后加入到VoList
        for(Chapter chapter :chapterList){
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);
            //填充小结
            List<VideoVo> videoVos = new ArrayList<>();
            for(Video video : videoList){
                if(video.getChapterId().equals(chapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video,videoVo);
                    videoVos.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVos);
            chapterVoList.add(chapterVo);
        }
        return chapterVoList;
    }

    //通过课程ID删除章节
    @Override
    public void removeByCourseId(Long id) {
        LambdaQueryWrapper<Chapter> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Chapter::getCourseId,id);
        chapterMapper.delete(queryWrapper);
    }
}
