package com.wsk.parent.vod.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsk.ggkt.model.vod.*;
import com.wsk.ggkt.vo.activity.CouponUseQueryVo;
import com.wsk.ggkt.vo.vod.*;
import com.wsk.parent.vod.mapper.CourseMapper;
import com.wsk.parent.vod.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsk.serviceutil.exception.GgktException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Autowired
    TeacherService teacherService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CourseDescriptionService courseDescriptionService;
    @Autowired
    VideoService videoService;
    @Autowired
    ChapterService chapterService;


    //(条件搜索)获取分页列表
    @Override
    public Map<String, Object> findPage(Page<Course> pageParam, CourseQueryVo courseQueryVo) {
        //封装非空的条件值，然后进行查询
        LambdaQueryWrapper<Course> queryWrapper = new LambdaQueryWrapper();
        if(courseQueryVo!=null) {
            //获取对象中的条件值
            String title = courseQueryVo.getTitle();
            Long subjectId = courseQueryVo.getSubjectId();
            Long subjectParentId = courseQueryVo.getSubjectParentId();
            Long teacherId = courseQueryVo.getTeacherId();

            if (!StringUtils.isEmpty(title)) {
                queryWrapper.like(Course::getTitle, title);
            }
            if (!StringUtils.isEmpty(subjectId)) {
                queryWrapper.like(Course::getSubjectId, subjectId);
            }
            if (!StringUtils.isEmpty(subjectParentId)) {
                queryWrapper.like(Course::getSubjectParentId, subjectParentId);
            }
            if (!StringUtils.isEmpty(teacherId)) {
                queryWrapper.like(Course::getTeacherId, teacherId);
            }
        }

        //实现分页查询
        Page<Course> coursePage = courseMapper.selectPage(pageParam, queryWrapper);
        //封装数据到Map集合中（其实也可以直接更新page里面的数据，然后返回）
        long pages = coursePage.getPages();//总页数
        long current = coursePage.getCurrent();//当前页
        long size = coursePage.getSize();//每页记录数
        long total = coursePage.getTotal();//总记录数

        //查询数据库，封装集合中讲师的姓名，课程分类的名称
        List<Course> records = coursePage.getRecords();//数据的集合
        //TODO 可以优化，不循环查询数据库，自己写SQL语句，course分出3个MAP
        records.stream().forEach(item ->{
            this.getNameById(item);
        });
        //数据封装到Map集合
        Map<String,Object> map = new HashMap<>();
        map.put("totalCount",total);
        map.put("totalPage",pages);
        map.put("records",records);
        return map;
    }

    //添加课程信息
    @Override
    public Long saveCourseInfo(CourseFormVo courseFormVo) {
        //需要更新两张表：course 和 course_description
        //1.  courseFormVo 转换成 course ，先保存到course表中
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo,course);
        courseMapper.insert(course);
        //2. courseFormVo 转换成 course_description ,保存到course_description中
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setCourseId(course.getId());
        courseDescription.setDescription(courseFormVo.getDescription());
        courseDescriptionService.save(courseDescription);

        return course.getId();
    }

    //根据ID获取课程信息，返回courseFormVo
    @Override
    public CourseFormVo getCourseFromVoById(Long id) {
        //先查询数据库信息
        Course course = courseMapper.selectById(id);
        if(course==null){
            throw new GgktException(20001,"课程信息不存在");
        }
        //转换成courseFormVo
        CourseFormVo courseFormVo = new CourseFormVo();
        BeanUtils.copyProperties(course,courseFormVo);
        //查询course_description表,封装描述信息
        CourseDescription description = courseDescriptionService.selectByCourseId(id);
        if(description!=null){
            courseFormVo.setDescription(description.getDescription());
        }
        return courseFormVo;
    }

    //根据ID修改课程信息
    @Override
    public void updateCourseById(CourseFormVo courseFormVo) {
        //更新两张表 course 和 course_description
        //1. course表
        Course course = new Course();
        BeanUtils.copyProperties(courseFormVo,course);
        courseMapper.updateById(course);
        //2. description表 要通过courseId获取
        CourseDescription description = courseDescriptionService.selectByCourseId(course.getId());
        description.setDescription(courseFormVo.getDescription());
        courseDescriptionService.updateById(description);
    }

    //获取发布课程信息
    @Override
    public CoursePublishVo getCoursePublishVo(Long id) {
        return courseMapper.getCoursePublishVo(id);
    }

    //课程最终发布,发布课程信息
    @Override
    public boolean publishCourseById(Long id) {
        Course course = new Course();
        course.setId(id);
        course.setPublishTime(new Date());
        course.setStatus(1);
        int i = courseMapper.updateById(course);
        return i>0;
    }

    //课程删除（同时删除课程下的章节和小节）
    @Override
    public void removeCourseById(Long id) {
        //删除小节
        videoService.removeByCourseId(id);
        //删除章节
        chapterService.removeByCourseId(id);
        //删除课程描述
        courseDescriptionService.removeByCourseId(id);
        //删除课程
        courseMapper.deleteById(id);
    }

    //查询所有课程,还需要封装老师名称、课程分类名称
    @Override
    public List<Course> findList() {
        //先获取所有数据再进行封装
        List<Course> courseList = courseMapper.selectList(null);
        //TODO 优化：不循环更新查询数据库
        for(Course course :courseList){
            getNameById(course);
        }
        return courseList;
    }

    //根据课程ID查询课程信息
    @Override
    public Map<String, Object> getInfoById(Long courseId) {
        //更新数据库中的数据（浏览量+1）
        Course course = courseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount()+1);
        courseMapper.updateById(course);

        //查询课程详情
        CourseVo courseVo = courseMapper.selectCourseVoById(courseId);
        //查询课程的章节小节
        List<ChapterVo> chapterAndVideo = chapterService.getNestedTreeList(courseId);
        //查询课程的教师信息
        Teacher teacher = teacherService.getById(courseVo.getTeacherId());
        //查询课程的描述信息
        CourseDescription courseDescription = courseDescriptionService.selectByCourseId(courseId);
        //封装到map集合
        Map<String, Object> map = new HashMap<>();
        map.put("courseVo", courseVo);
        map.put("chapterVoList", chapterAndVideo);
        map.put("description", null != courseDescription ? courseDescription.getDescription() : "");
        map.put("teacher", teacher);
        map.put("isBuy", false);//是否购买  TODO 后续完善
        return map;
    }


    //根据ID获取讲师名称和课程分类名称
    public Course getNameById(Course course){
        //根据讲师ID查询讲师名称，然后封装
        Long teacherId = course.getTeacherId();
        Teacher teacher = teacherService.getById(teacherId);
        if(teacher!=null){
            course.getParam().put("teacherName",teacher.getName());
        }
        //根据课程分类ID查询课程分类名称，然后封装
        //一级课程分类
        Subject subjectOne = subjectService.getById(course.getSubjectParentId());
        if(subjectOne != null) {
            course.getParam().put("subjectParentTitle",subjectOne.getTitle());
        }
        //二级课程分类
        Subject subjectTwo = subjectService.getById(course.getSubjectId());
        if(subjectTwo != null) {
            course.getParam().put("subjectTitle",subjectTwo.getTitle());
        }
        return course;
    }
}
