package com.wsk.ggkt.client;

import com.wsk.ggkt.model.vod.Course;
import com.wsk.ggkt.model.vod.Teacher;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author:WuShangke
 * @create:2022/8/21-17:16
 */
@FeignClient("service-vod")
public interface CourseFeignClient {
    //关键词查询课程(公众号端)
    @GetMapping("/api/vod/course/inner/findByKeyword/{keyword}")
    List<Course> findByKeyword(@PathVariable String keyword);
    //课程ID查询课程(公众号端)
    @GetMapping("/api/vod/course/inner/getById/{courseId}")
    Course getById(@PathVariable Long courseId);
    //教师ID获取教师信息(后台管理系统)
    @GetMapping("/admin/vod/teacher/inner/getTeacher/{id}")
    Teacher getTeacherLive(@PathVariable Long id);
}
