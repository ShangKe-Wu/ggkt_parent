package com.wsk.parent.live.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsk.ggkt.model.live.LiveCourse;
import com.wsk.ggkt.model.live.LiveCourseAccount;
import com.wsk.ggkt.vo.live.LiveCourseConfigVo;
import com.wsk.ggkt.vo.live.LiveCourseFormVo;
import com.wsk.ggkt.vo.live.LiveCourseVo;
import com.wsk.parent.live.service.LiveCourseAccountService;
import com.wsk.parent.live.service.LiveCourseService;
import com.wsk.serviceutil.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 直播课程表 前端控制器
 * </p>
 *
 * @author wsk
 * @since 2022-08-22
 */
@RestController
@RequestMapping(value="/admin/live/liveCourse")
public class LiveCourseController {
    @Autowired
    private LiveCourseService liveCourseService;
    @Autowired
    private LiveCourseAccountService liveCourseAccountService;

    //管理系统分页列表(所有的直播课程列表)
    @GetMapping("/{page}/{limit}")
    public Result index(@PathVariable Long page ,@PathVariable Long limit){
        Page<LiveCourse> pageParam = new Page(page,limit);
        IPage<LiveCourse> pageModel = liveCourseService.selectPage(pageParam);
        return Result.ok(pageModel);
    }

    //新增直播课程（除了添加到数据库还要添加到欢透云上）
    @PostMapping("/save")
    public Result save(@RequestBody LiveCourseFormVo liveCourseFormVo){
        liveCourseService.saveLiveCourse(liveCourseFormVo);
        return Result.ok();
    }

    //删除直播课程（除了删除数据库，还要删除欢透云上的）
    @DeleteMapping("/remove/{id}")
    public Result delete(@PathVariable Long id){
        liveCourseService.removeLive(id);
        return Result.ok();
    }

    //更新直播课程(同时更新欢透云)
    @PutMapping("/update")
    public Result update(@RequestBody LiveCourseFormVo liveCourseFormVo){
        liveCourseService.updateLiveById(liveCourseFormVo);
        return Result.ok();
    }

    //查询直播课程基本信息
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id){
        LiveCourse liveCourse = liveCourseService.getById(id);
        return Result.ok(liveCourse);
    }
    //查询直播课程基本信息和描述信息
    @GetMapping("/getInfo/{id}")
    public Result getInfo(@PathVariable Long id){
        LiveCourseFormVo liveCourseFormVo = liveCourseService.getLiveCourseVo(id);
        return Result.ok(liveCourseFormVo);
    }

    //查看直播课程 （主播的）账号信息 (通过live_course_id 查询)
    @GetMapping("/getLiveCourseAccount/{id}")
    public Result<LiveCourseAccount> getLiveCourseAccount(@PathVariable Long id) {
        LiveCourseAccount liveCourseAccount = liveCourseAccountService.getLiveCourseAccountCourseId(id);
        return Result.ok(liveCourseAccount);
    }

    //获取直播配置信息(通过 live_course_id 查询)
    @GetMapping("/getCourseConfig/{id}")
    public Result getCourseConfig(@PathVariable Long id) {
        LiveCourseConfigVo liveCourseConfigVo = liveCourseService.getCourseConfig(id);
        return Result.ok(liveCourseConfigVo);
    }

    //修改直播配置信息(同时修改欢透云和数据库)
    @PutMapping("/updateConfig")
    public Result updateConfig(@RequestBody LiveCourseConfigVo liveCourseConfigVo) {
        liveCourseService.updateConfig(liveCourseConfigVo);
        return Result.ok(null);
    }

    //获取最近直播课程列表
    @GetMapping("/findLatelyList")
    public Result findLatelyList() {
        List<LiveCourseVo> list = liveCourseService.getLatelyList();
        return Result.ok(list);
    }

    //TODO 批量删除 /batchRemove  IDS
}

