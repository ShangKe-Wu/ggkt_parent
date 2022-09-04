package com.wsk.parent.live.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wsk.ggkt.model.live.LiveCourse;
import com.wsk.ggkt.vo.live.LiveCourseConfigVo;
import com.wsk.ggkt.vo.live.LiveCourseFormVo;
import com.wsk.ggkt.vo.live.LiveCourseVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 直播课程表 服务类
 * </p>
 *
 * @author wsk
 * @since 2022-08-22
 */
public interface LiveCourseService extends IService<LiveCourse> {

    //管理系统分页列表(所有的直播课程列表)
    IPage<LiveCourse> selectPage(Page<LiveCourse> pageParam);

    //新增直播课程
    void saveLiveCourse(LiveCourseFormVo liveCourseFormVo);

    //删除直播课程（除了删除数据库，还要删除欢透云上的）
    void removeLive(Long id);

    //查询直播课程基本信息和描述信息
    LiveCourseFormVo getLiveCourseVo(Long id);

    //更新直播课程
    void updateLiveById(LiveCourseFormVo liveCourseFormVo);

    //获取直播配置信息(通过 live_course_id 查询)
    LiveCourseConfigVo getCourseConfig(Long id);

    //修改直播配置信息(同时修改欢透云和数据库)
    void updateConfig(LiveCourseConfigVo liveCourseConfigVo);

    //获取最近直播课程列表
    List<LiveCourseVo> getLatelyList();

    //获取用户access_token
    JSONObject getAccessToken(Long id, Long userId);

    //根据ID查询课程
    Map<String, Object> getInfoById(Long courseId);
}
