package com.wsk.parent.vod.controller;


import com.wsk.ggkt.model.vod.Subject;
import com.wsk.parent.vod.service.SubjectService;
import com.wsk.serviceutil.result.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 课程分类 前端控制器
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@RestController
@RequestMapping("/admin/vod/subject")
public class SubjectController {

    @Autowired
    SubjectService subjectService;

    //查询下一级课程分类，懒加载
    //查询一级标签令ID=0即可
    @ApiOperation("查询某个一级课程的下一级课程分类")
    @GetMapping("/getChildSubject/{id}")
    public Result getChildSubject(@PathVariable(value = "id") Long id){
        List<Subject> list= subjectService.getChildSubject(id);
        return Result.ok(list);
    }

    //课程分类导出
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response){
        subjectService.exportData(response);
    }

    //课程分类导入，需要使用到监听器
    @PostMapping("/importData")
    public Result importDate(MultipartFile file){
        subjectService.importDate(file);
        return Result.ok();
    }


}

