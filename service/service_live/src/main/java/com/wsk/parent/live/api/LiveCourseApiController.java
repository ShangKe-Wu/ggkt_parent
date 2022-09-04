package com.wsk.parent.live.api;

import com.alibaba.fastjson.JSONObject;
import com.wsk.parent.live.service.LiveCourseService;
import com.wsk.serviceutil.result.Result;
import com.wsk.serviceutil.utils.AuthContextHolder;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author:WuShangke
 * @create:2022/8/22-18:45
 */
@RestController
@RequestMapping("api/live/liveCourse")
public class LiveCourseApiController {
    @Autowired
    private LiveCourseService liveCourseService;

    //获取用户access_token, 用户要观看直播，必须获取对应的用户access_token，通过access_token 获取观看的直播课程
    //id 直播课程的ID
    @GetMapping("/getPlayAuth/{id}")
    public Result getPlayAuth(@PathVariable Long id) {
        JSONObject object = liveCourseService.getAccessToken(id, AuthContextHolder.getUserId());
        return Result.ok(object);
    }

    //根据ID查询课程
    @GetMapping("/getInfo/{courseId}")
    public Result getInfo(
            @PathVariable Long courseId){
        Map<String, Object> map = liveCourseService.getInfoById(courseId);
        return Result.ok(map);
    }
}
