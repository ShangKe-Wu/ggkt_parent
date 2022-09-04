package com.wsk.parent.vod.controller;


import com.wsk.parent.vod.service.VideoVisitorService;
import com.wsk.serviceutil.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 视频来访者记录表 前端控制器
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@RestController
@RequestMapping("/admin/vod/video-visitor")
public class VideoVisitorController {
    @Autowired
    VideoVisitorService videoVisitorService;

    //获得统计数据：某个课程在某个时间段内的观看数据
    // 使用Echart，官方网站：https://echarts.apache.org/zh/index.html
    @GetMapping("/findCount/{courseId}/{startDate}/{endDate}")
    public Result showChart(@PathVariable Long courseId,
                            @PathVariable String startDate,
                            @PathVariable String endDate){
        Map<String,Object> map = videoVisitorService.findCount(courseId,startDate,endDate);
        return Result.ok(map);
    }
}

