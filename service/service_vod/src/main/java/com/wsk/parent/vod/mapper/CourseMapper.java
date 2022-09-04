package com.wsk.parent.vod.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wsk.ggkt.model.vod.Course;
import com.wsk.ggkt.vo.vod.CoursePublishVo;
import com.wsk.ggkt.vo.vod.CourseVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {


    CoursePublishVo getCoursePublishVo(Long id);

    CourseVo selectCourseVoById(Long courseId);

}

