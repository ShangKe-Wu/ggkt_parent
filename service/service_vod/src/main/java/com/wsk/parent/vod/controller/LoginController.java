package com.wsk.parent.vod.controller;

import com.wsk.serviceutil.result.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:WuShangke
 * @create:2022/8/15-14:58
 */
@RestController
@Api("用户登录接口")
@RequestMapping("/admin/vod/user")
//@CrossOrigin
public class LoginController {
    //不适用密码直接登录
    //登录后台管理系统
    @PostMapping("/login")
    public Result login(){
        Map<String, Object> map = new HashMap<>();
        map.put("token","admin");
        return Result.ok(map);
    }
    //登出
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }
    //获取用户信息
    @GetMapping("/info")
    public Result getInfo(){
        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name","admin");
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        return Result.ok(map);
    }
}
