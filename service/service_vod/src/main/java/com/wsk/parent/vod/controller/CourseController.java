package com.wsk.parent.vod.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsk.ggkt.model.vod.Course;
import com.wsk.ggkt.vo.vod.CourseFormVo;
import com.wsk.ggkt.vo.vod.CoursePublishVo;
import com.wsk.ggkt.vo.vod.CourseQueryVo;
import com.wsk.parent.vod.mapper.CourseMapper;
import com.wsk.parent.vod.service.CourseService;
import com.wsk.serviceutil.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@RestController
@RequestMapping("/admin/vod/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    //(条件搜索)获取分页列表
    @GetMapping("/{page}/{limit}")
    public Result index(@PathVariable Long page , @PathVariable Long limit, CourseQueryVo courseQueryVo){
        Page<Course> pageParam = new Page<>(page,limit);
        Map<String,Object> map = new HashMap<>();
        if(courseQueryVo!=null){
             map = courseService.findPage(pageParam,courseQueryVo);
        }
        else {
            map = courseService.findPage(pageParam,null);
        }
        return Result.ok(map);
    }

    //添加课程信息(发布课程页面--课程基本信息)
    @PostMapping("/save")
    public Result save(@RequestBody CourseFormVo courseFormVo){
        Long courseId = courseService.saveCourseInfo(courseFormVo);
        return Result.ok(courseId);
    }

    //获取课程信息（发布课程页面--课程基本信息）
    @GetMapping("/get/{id}")
    public Result getInfo(@PathVariable Long id){
        CourseFormVo courseFromVoById = courseService.getCourseFromVoById(id);
        return Result.ok(courseFromVoById);
    }

    //更新课程信息（发布课程页面--课程基本信息）
    @PostMapping("/update")
    public Result update(@RequestBody CourseFormVo courseFormVo){
        courseService.updateCourseById(courseFormVo);
        return Result.ok(courseFormVo.getId());
    }

    //获取发布课程信息(发布课程的最后页面)
    @GetMapping("/getCoursePublishVo/{id}")
    public Result getCoursePublishVo(@PathVariable Long id){
        CoursePublishVo coursePublishVo = courseService.getCoursePublishVo(id);
        return Result.ok(coursePublishVo);
    }

    //课程最终发布,发布课程信息
    @PutMapping("/publishCourse/{id}")
    public Result publishCourseById(@PathVariable Long id){
        boolean result = courseService.publishCourseById(id);
        return Result.ok();
    }

    //课程删除（同时删除课程下的章节和小节）
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id){
        courseService.removeCourseById(id);
        return Result.ok();
    }

    //查询所有课程,还需要封装老师名称、课程分类名称
    @GetMapping("/findAll")
    public Result findAll(){
        List<Course> list = courseService.findList();
        return Result.ok(list);
    }
}

