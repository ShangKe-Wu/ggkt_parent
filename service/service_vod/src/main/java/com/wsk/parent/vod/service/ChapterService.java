package com.wsk.parent.vod.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wsk.ggkt.model.vod.Chapter;
import com.wsk.ggkt.vo.vod.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
public interface ChapterService extends IService<Chapter> {
    //获取章节、小节列表
    List<ChapterVo> getNestedTreeList(Long courseId);

    //通过课程ID删除章节
    void removeByCourseId(Long id);
}
