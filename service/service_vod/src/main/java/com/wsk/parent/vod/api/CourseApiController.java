package com.wsk.parent.vod.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsk.ggkt.model.vod.Course;
import com.wsk.ggkt.vo.vod.CourseQueryVo;
import com.wsk.parent.vod.service.ChapterService;
import com.wsk.parent.vod.service.CourseService;
import com.wsk.serviceutil.result.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author:WuShangke
 * @create:2022/8/21-17:00
 */
@RestController
@RequestMapping(value="/api/vod/course")
public class CourseApiController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ChapterService chapterService;

    //根据关键词查询课程
    @GetMapping("inner/findByKeyword/{keyword}")
    public List<Course> findByKeyword(@PathVariable String keyword){
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", keyword);
        List<Course> list = courseService.list(queryWrapper);
        return list;
    }

    //根据课程分类(subjectParentId)查询课程分页数据
    @GetMapping("/{subjectParentId}/{page}/{limit}")
    public Result findPageCourse(@PathVariable("subjectParentId") Long subjectParentId,
                                 @PathVariable("page") Long page,
                                 @PathVariable("limit") Long limit){
        //设置查询条件
        CourseQueryVo courseQueryVo = new CourseQueryVo();
        courseQueryVo.setSubjectParentId(subjectParentId);
        //创建page对象
        Page<Course> coursePage = new Page<>(page,limit);
        Map<String, Object> map = courseService.findPage(coursePage, courseQueryVo);
        return Result.ok(map);
    }

    //根据课程ID查询课程详情
    @GetMapping("/getInfo/{courseId}")
    public Result getInfoById(@PathVariable Long courseId){
        Map<String, Object> map = courseService.getInfoById(courseId);
        return Result.ok(map);
    }

    //课程id查询课程信息（客户端远程调用）
    @GetMapping("inner/getById/{courseId}")
    public Course getById(
            @ApiParam(value = "课程ID", required = true)
            @PathVariable Long courseId){
        Course course = courseService.getById(courseId);
        return course;
    }
}
