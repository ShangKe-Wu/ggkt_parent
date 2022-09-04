package com.wsk.parent.vod.controller;


import com.wsk.ggkt.model.vod.Chapter;
import com.wsk.ggkt.vo.vod.ChapterVo;
import com.wsk.parent.vod.service.ChapterService;
import com.wsk.serviceutil.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@RestController
@RequestMapping("/admin/vod/chapter")
public class ChapterController {
    @Autowired
    ChapterService chapterService;
    //获取章节、小节列表
    @GetMapping("/getNestedTreeList/{courseId}")
    public Result getList(@PathVariable Long courseId){
        List<ChapterVo> chapterVoList = chapterService.getNestedTreeList(courseId);
        return Result.ok(chapterVoList);
    }

    //添加章节
    @PostMapping("/save")
    public Result save(@RequestBody Chapter chapter){
        chapterService.save(chapter);
        return Result.ok();
    }

    //修改-根据ID查询信息（前端数据回显）
    @GetMapping("/get/{id}")
    public Result getChapterById(@PathVariable Long id){
        Chapter chapter = chapterService.getById(id);
        return Result.ok(chapter);
    }

    //修改-修改数据库中的信息
    @PostMapping("/update")
    public Result update(@RequestBody Chapter chapter){
        chapterService.updateById(chapter);
        return Result.ok();
    }

    //删除章节
    //TODO 可以顺便把章节下面的小节也删掉
    @DeleteMapping("/remove/{id}")
    public Result delete(@PathVariable Long id){
        chapterService.removeById(id);
        return Result.ok();
    }
}

