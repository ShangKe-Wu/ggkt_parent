package com.wsk.parent.vod.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsk.ggkt.model.vod.Teacher;
import com.wsk.ggkt.vo.vod.TeacherQueryVo;
import com.wsk.parent.vod.service.TeacherService;
import com.wsk.serviceutil.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@Api(tags = "讲师接口")
@RestController
@RequestMapping("/admin/vod/teacher")
public class TeacherController {

    @Resource
    TeacherService teacherService;

    //根据ID获取教师信息（远程调用，返回teacher对象）
    @GetMapping("inner/getTeacher/{id}")
    public Teacher getTeacherLive(@PathVariable Long id) {
        Teacher teacher = teacherService.getById(id);
        return teacher;
    }

    //查询所有讲师列表
    @ApiOperation(value = "查询所有讲师列表")
    @GetMapping("/findAll")
    public Result findAllTeacher() {
        //调用service方法
        List<Teacher> list = teacherService.list();
        return Result.ok(list).message("查询数据成功");
    }

    //删除讲师
    @ApiOperation("通过ID删除讲师")
    @DeleteMapping("/remove/{id}")
    public Result removeById(@PathVariable(value = "id") String id){
        boolean result = teacherService.removeById(id);
        if(result){
            return Result.ok();
        }
        return Result.fail();
    }

    //条件查询分页列表
    @ApiOperation("按条件分页查询")
    @PostMapping("/findQueryPage/{page}/{limit}")
    public Result index(@ApiParam(value = "当前页码",name = "page",required = true) @PathVariable("page") Long page,
                        @ApiParam(value = "每页记录数",name = "limit",required = true)@PathVariable("limit") Long limit,
                        @ApiParam(value = "查询条件",name = "teacherQueryVo") @RequestBody(required = false) TeacherQueryVo teacherQueryVo){
        //创建page对象，传入当前页码和每页记录数
        Page<Teacher> pageParam = new Page<>(page,limit);
        //提取查询条件中的参数
        String name = teacherQueryVo.getName();//讲师名称
        Integer level = teacherQueryVo.getLevel();//讲师级别
        String joinDateBegin = teacherQueryVo.getJoinDateBegin();//开始时间
        String joinDateEnd = teacherQueryVo.getJoinDateEnd();//结束时间
        //判断是否为空，为空则不封装条件
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("name",name);//第一个参数代表数据库中的字段名，如果用lambdaQueryWrapper，则是使用对象中的属性名
        }
        if(!StringUtils.isEmpty(level)){
            queryWrapper.eq("level",level);
        }
        if(!StringUtils.isEmpty(joinDateBegin)){
            queryWrapper.ge("join_date",joinDateBegin);
        }
        if(!StringUtils.isEmpty(level)){
            queryWrapper.le("join_date",joinDateEnd);
        }
        //返回查询结果
        IPage<Teacher> page1 = teacherService.page(pageParam, queryWrapper);
        return Result.ok(page1);
    }

    //添加讲师
    @ApiOperation("添加讲师")
    @PostMapping("/saveTeacher")
    public Result save(@RequestBody Teacher teacher){
        boolean save = teacherService.save(teacher);
        if(save){
            return Result.ok();
        }
        return Result.fail();
    }

    //修改讲师信息,先获取原本讲师的信息，回显给前端，然后再前端页面中修改
    //先查询
    @ApiOperation("根据ID查询")
    @GetMapping("/getTeacher/{id}")
    public Result get(@PathVariable("id") Long id){
        Teacher byId = teacherService.getById(id);
        if(byId!=null){
            return Result.ok(byId);
        }
        return Result.fail();
    }

    //后修改
    @ApiOperation("修改讲师信息")
    @PostMapping("/updateTeacher")
    public Result updateById(@RequestBody Teacher teacher){
        boolean b = teacherService.updateById(teacher);
        if(b){
            return Result.ok();
        }
        return Result.fail();
    }

    //批量删除
    @ApiOperation("根据ID列表批量删除")
    @DeleteMapping("/removeBatch")
    public Result deleteBatch(@RequestBody List<Long> Ids){
        boolean result = teacherService.removeByIds(Ids);
        if(result){
            return Result.ok();
        }
        return Result.fail();
    }
}

