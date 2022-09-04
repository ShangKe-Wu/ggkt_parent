package com.wsk.parent.vod.api;

import com.wsk.parent.vod.service.VodService;
import com.wsk.serviceutil.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author:WuShangke
 * @create:2022/8/21-21:08
 */
@RestController
@RequestMapping("/api/vod")
public class VodApiController {
    @Autowired
    private VodService vodService;

    //点播视频播放接口
    //TODO  前端整合
    @GetMapping("getPlayAuth/{courseId}/{videoId}")
    public Result getPlayAuth(@PathVariable Long courseId,@PathVariable Long videoId){
        Map<String,Object> map = vodService.getPlayAuth(courseId,videoId);
        return Result.ok(map);
    }
}
