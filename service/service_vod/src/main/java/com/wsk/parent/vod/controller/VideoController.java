package com.wsk.parent.vod.controller;


import com.wsk.ggkt.model.vod.Video;
import com.wsk.parent.vod.service.VideoService;
import com.wsk.serviceutil.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@RestController
@RequestMapping("/admin/vod/video")
public class VideoController {
    @Autowired
    private VideoService videoService;

    //获取video信息
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        Video video = videoService.getById(id);
        return Result.ok(video);
    }

    //新增小节
    @PostMapping("/save")
    public Result save(@RequestBody Video video) {
        videoService.save(video);
        return Result.ok(null);
    }

    //修改小节
    @PutMapping("/update")
    public Result updateById(@RequestBody Video video) {
        videoService.updateById(video);
        return Result.ok(null);
    }

    //通过小节ID删除小节，同时删除腾讯云上面的视频
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        videoService.removeVideoById(id);
        return Result.ok(null);
    }
}

