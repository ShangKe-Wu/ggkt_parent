package com.wsk.parent.vod.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wsk.ggkt.model.vod.Course;
import com.wsk.ggkt.vo.vod.CourseFormVo;
import com.wsk.ggkt.vo.vod.CoursePublishVo;
import com.wsk.ggkt.vo.vod.CourseQueryVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
public interface CourseService extends IService<Course> {

    //(条件搜索)获取分页列表
    Map<String, Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo);

    //添加课程信息
    Long saveCourseInfo(CourseFormVo courseFormVo);

    //通过前端更改课程信息需要以下两个方法，先前端回显数据，再进行更新
    //根据ID获取课程信息，返回courseFormVo
    CourseFormVo getCourseFromVoById(Long id);
    //根据ID修改课程信息
    void updateCourseById(CourseFormVo courseFormVo);

    //获取发布课程信息
    CoursePublishVo getCoursePublishVo(Long id);

    //课程最终发布,发布课程信息
    boolean publishCourseById(Long id);

    //课程删除（同时删除课程下的章节和小节）
    void removeCourseById(Long id);

    //查询所有课程,还需要封装老师名称、课程分类名称
    List<Course> findList();

    //根据课程ID查询课程信息
    Map<String, Object> getInfoById(Long courseId);
}
