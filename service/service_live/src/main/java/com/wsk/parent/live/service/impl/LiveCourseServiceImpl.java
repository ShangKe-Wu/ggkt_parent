package com.wsk.parent.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsk.ggkt.client.CourseFeignClient;
import com.wsk.ggkt.client.UserInfoFeignClient;
import com.wsk.ggkt.model.live.*;
import com.wsk.ggkt.model.user.UserInfo;
import com.wsk.ggkt.model.vod.CourseDescription;
import com.wsk.ggkt.model.vod.Teacher;
import com.wsk.ggkt.vo.live.LiveCourseConfigVo;
import com.wsk.ggkt.vo.live.LiveCourseFormVo;
import com.wsk.ggkt.vo.live.LiveCourseGoodsView;
import com.wsk.ggkt.vo.live.LiveCourseVo;
import com.wsk.parent.live.mapper.LiveCourseMapper;
import com.wsk.parent.live.mtcloud.CommonResult;
import com.wsk.parent.live.mtcloud.MTCloud;
import com.wsk.parent.live.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsk.serviceutil.exception.GgktException;
import com.wsk.serviceutil.utils.DateUtil;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * <p>
 * 直播课程表 服务实现类
 * </p>
 *
 * @author wsk
 * @since 2022-08-22
 */
@Service
public class LiveCourseServiceImpl extends ServiceImpl<LiveCourseMapper, LiveCourse> implements LiveCourseService {
    @Autowired
    private LiveCourseMapper liveCourseMapper;

    @Autowired
    private CourseFeignClient courseFeignClient;

    @Autowired
    private LiveCourseAccountService liveCourseAccountService;

    @Autowired
    private LiveCourseDescriptionService liveCourseDescriptionService;

    @Autowired
    private MTCloud mtCloudClient;

    @Autowired
    private LiveCourseConfigService liveCourseConfigService;

    @Autowired
    private LiveCourseGoodsService liveCourseGoodsService;

    @Autowired
    private UserInfoFeignClient userInfoFeignClient;


    //管理系统分页列表(所有的直播课程列表)
    @Override
    public IPage<LiveCourse> selectPage(Page<LiveCourse> pageParam) {
        IPage<LiveCourse> liveCoursePage = liveCourseMapper.selectPage(pageParam, null);
        List<LiveCourse> records = liveCoursePage.getRecords();
        //查询教师信息，并封装
        for (LiveCourse liveCourse : records){
            //TODO 优化成不是循环查询
            Teacher teacherLive = courseFeignClient.getTeacherLive(liveCourse.getTeacherId());
            liveCourse.getParam().put("teacherName",teacherLive.getName());
            liveCourse.getParam().put("teacherLevel",teacherLive.getLevel());
        }
        return liveCoursePage;
    }

    //新增直播课程，除了添加到数据库外，还要添加到欢透云上，参数参考欢透云的官方文档
    //TODO 优化：加上事务管理，之前很多方法都没有加事务
    @Override
    public void saveLiveCourse(LiveCourseFormVo liveCourseFormVo) {
        //先把VO转换成 liveCourse对象
        LiveCourse liveCourse = new LiveCourse();
        BeanUtils.copyProperties(liveCourseFormVo,liveCourse);
        //获取老师信息
        Teacher teacher = courseFeignClient.getTeacherLive(liveCourseFormVo.getTeacherId());
        //添加到欢透云
        //创建map集合，封装直播课程其他参数
        HashMap<Object, Object> options = new HashMap<>();
        options.put("scenes", 2);//直播类型。1: 教育直播，2: 生活直播。默认 1，说明：根据平台开通的直播类型填写
        options.put("password", liveCourseFormVo.getPassword());
        //下面是欢透云API所需要的参数
        //course_name 课程名称
//       account 发起直播课程的主播账号
//       start_time 课程开始时间,格式: 2015-01-10 12:00:00
//       end_time 课程结束时间,格式: 2015-01-10 13:00:00
//         nickname  昵称
//         accountIntro 主播介绍
//         options 其他参数
        try {
            String res = mtCloudClient.courseAdd(liveCourse.getCourseName(),
                    teacher.getId().toString(),
                    new DateTime(liveCourse.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    new DateTime(liveCourse.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    teacher.getName(),
                    teacher.getIntro(),
                    options);
            //根据res字符串，判断是否添加成功
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            //添加成功
            if(Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS){
                //添加到数据库
                //3张表
                //1 live_course
                JSONObject data = commonResult.getData();
                Long course_id = data.getLong("course_id");
                liveCourse.setCourseId(course_id);
                liveCourseMapper.insert(liveCourse);
                //2 live_course_description
                LiveCourseDescription liveCourseDescription = new LiveCourseDescription();
                liveCourseDescription.setDescription(liveCourseFormVo.getDescription());
                liveCourseDescription.setLiveCourseId(liveCourse.getId());
                liveCourseDescriptionService.save(liveCourseDescription);
                //3 live_course_account ，里面的数据需要从欢透云返回的结果找出
                LiveCourseAccount liveCourseAccount = new LiveCourseAccount();
                liveCourseAccount.setLiveCourseId(liveCourse.getId());
                liveCourseAccount.setZhuboAccount(data.getString("bid"));
                liveCourseAccount.setZhuboPassword(liveCourseFormVo.getPassword());
                liveCourseAccount.setAdminKey(data.getString("admin_key"));
                liveCourseAccount.setUserKey(data.getString("user_key"));
                liveCourseAccount.setZhuboKey(data.getString("zhubo_key"));
                liveCourseAccountService.save(liveCourseAccount);
            }
            //添加失败
            else{
                throw new GgktException(20001,"直播创建失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除直播课程（除了删除数据库，还要删除欢透云上的）
    @Override
    public void removeLive(Long id) {
        try {
            //根据ID查询课程信息
            LiveCourse liveCourse = liveCourseMapper.selectById(id);
            if(liveCourse!=null){
                //获取courseID
                Long courseId = liveCourse.getCourseId();
                //删除欢透云上面的信息
                mtCloudClient.courseDelete(courseId.toString());
                //删除数据库表的数据
                //TODO 优化 还可以删除另外两张表的信息
                liveCourseMapper.deleteById(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GgktException(20001,"删除直播课程失败");
        }
    }

    //查询直播课程基本信息和描述信息
    @Override
    public LiveCourseFormVo getLiveCourseVo(Long id) {
        //获取基本信息
        LiveCourse liveCourse = liveCourseMapper.selectById(id);
        //获取描述信息
        LiveCourseDescription liveCourseDescription = liveCourseDescriptionService.getByCourseId(id);
        //封装成VO
        LiveCourseFormVo liveCourseFormVo = new LiveCourseFormVo();
        BeanUtils.copyProperties(liveCourse,liveCourseFormVo);
        liveCourseFormVo.setDescription(liveCourseDescription.getDescription());
        return liveCourseFormVo;
    }

    //更新直播课程(同时更新欢透云)
    @Override
    public void updateLiveById(LiveCourseFormVo liveCourseFormVo) {
        //先获取数据库中的信息
        LiveCourse liveCourse = liveCourseMapper.selectById(liveCourseFormVo.getId());
        //覆盖原本的信息
        BeanUtils.copyProperties(liveCourseFormVo,liveCourse);
        //获取老师信息
        Teacher teacher = courseFeignClient.getTeacherLive(liveCourseFormVo.getTeacherId());
        //更新欢透云
        //     *   course_id 课程ID
        //     *   account 发起直播课程的主播账号
        //     *   course_name 课程名称
        //     *   start_time 课程开始时间,格式:2015-01-01 12:00:00
        //     *   end_time 课程结束时间,格式:2015-01-01 13:00:00
        //     *   nickname 	主播的昵称
        //     *   accountIntro 	主播的简介
        //     *  options 		可选参数
        HashMap<Object, Object> options = new HashMap<>();
        try {
            String res = mtCloudClient.courseUpdate(liveCourse.getCourseId().toString(),
                    teacher.getId().toString(),
                    liveCourse.getCourseName(),
                    new DateTime(liveCourse.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    new DateTime(liveCourse.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    teacher.getName(),
                    teacher.getIntro(),
                    options);
            //返回结果转换，判断是否成功
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS){
                JSONObject object = commonResult.getData();
                //更新数据库
                //1 更新直播课程基本信息
                liveCourse.setCourseId(object.getLong("course_id"));
                baseMapper.updateById(liveCourse);
                //2 课程描述
                LiveCourseDescription liveCourseDescription =
                        liveCourseDescriptionService.getByCourseId(liveCourse.getId());
                liveCourseDescription.setDescription(liveCourseFormVo.getDescription());
                liveCourseDescriptionService.updateById(liveCourseDescription);
                //3 TODO 主播账户是否需要修改？
            }
            else{
                throw new GgktException(20001,"更新失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public LiveCourseConfigVo getCourseConfig(Long id) {
        //通过课程ID查询课程配置
        LiveCourseConfig liveCourseConfig = liveCourseConfigService.getByCourseId(id);
        //转换成VO
        LiveCourseConfigVo liveCourseConfigVo = new LiveCourseConfigVo();
        if(liveCourseConfig!=null){
            //查询直播课程商品列表
            List<LiveCourseGoods> liveCourseGoodsList = liveCourseGoodsService.getGoodsListCourseId(id);
            //复制属性
            BeanUtils.copyProperties(liveCourseConfig,liveCourseConfigVo);
            liveCourseConfigVo.setLiveCourseGoodsList(liveCourseGoodsList);
        }
        return liveCourseConfigVo;
    }

    //修改直播配置信息(同时修改欢透云和数据库)
    @Override
    public void updateConfig(LiveCourseConfigVo liveCourseConfigVo) {
        //更新基本信息表
        //获取数据库原始信息
        Long id = liveCourseConfigVo.getId();
        LiveCourse liveCourse = liveCourseMapper.selectById(id);
        if(liveCourse==null){
            throw new GgktException(20001,"数据不存在");
        }
        //覆盖原本属性
        BeanUtils.copyProperties(liveCourseConfigVo,liveCourse);
        //更新数据库
        liveCourseMapper.updateById(liveCourse);

        //更新商品表（先移除原本所有的，再添加进去） TODO 优化成只操作一次数据库 还有就是应该写在liveCourseGoodsService中 这样写感觉不太规范
        LambdaQueryWrapper<LiveCourseGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveCourseGoods::getLiveCourseId,liveCourseConfigVo.getLiveCourseId());
        liveCourseGoodsService.remove(wrapper);
        //添加商品列表
        if(!CollectionUtils.isEmpty(liveCourseConfigVo.getLiveCourseGoodsList())) {
            liveCourseGoodsService.saveBatch(liveCourseConfigVo.getLiveCourseGoodsList());
        }

        //最后修改欢透云
        this.updateLifeConfig(liveCourseConfigVo);
    }

    //获取最近直播课程列表
    @Override
    public List<LiveCourseVo> getLatelyList() {
        List<LiveCourseVo> liveCourseVoList = baseMapper.getLatelyList();
        //循环封装VO中的属性
        for (LiveCourseVo liveCourseVo:liveCourseVoList) {
            //封装开始和结束时间
            liveCourseVo.setStartTimeString(new DateTime(liveCourseVo.getStartTime()).toString("yyyy年MM月dd HH:mm"));
            liveCourseVo.setEndTimeString(new DateTime(liveCourseVo.getEndTime()).toString("HH:mm"));

            //封装讲师
            Long teacherId = liveCourseVo.getTeacherId();
            Teacher teacher = courseFeignClient.getTeacherLive(teacherId);
            liveCourseVo.setTeacher(teacher);

            //封装直播状态
            liveCourseVo.setLiveStatus(this.getLiveStatus(liveCourseVo));
        }
        return liveCourseVoList;
    }

    //获取用户access_token
    @Override
    public JSONObject getAccessToken(Long id, Long userId) {
        //根据课程id获取直播课程信息
        LiveCourse liveCourse = baseMapper.selectById(id);
        //根据用户id获取用户信息
        UserInfo userInfo = userInfoFeignClient.getById(userId);

        //封装需要参数
        HashMap<Object,Object> options = new HashMap<Object, Object>();
        /**
         *  course_id      课程ID
         *  uid            用户唯一ID
         *  nickname       用户昵称
         *  role           用户角色，枚举见:ROLE 定义
         *     expire         有效期,默认:3600(单位:秒)
         *   options        可选项，包括:gender:枚举见上面GENDER定义,avatar:头像地址
         */
        try {
            String res = mtCloudClient.courseAccess(liveCourse.getCourseId().toString(),
                    userId.toString(),
                    userInfo.getNickName(),
                    MTCloud.ROLE_USER,
                    3600,
                    options);
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) {
                JSONObject object = commonResult.getData();
                System.out.println("access::"+object.getString("access_token"));
                return object;
            } else {
                throw new GgktException(20001,"获取access_token失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据ID查询课程
    @Override
    public Map<String, Object> getInfoById(Long courseId) {
        LiveCourse liveCourse = this.getById(courseId);
        //封装开始时间和结束时间的字符串，用于前端回显
        liveCourse.getParam().put("startTimeString", new DateTime(liveCourse.getStartTime()).toString("yyyy年MM月dd HH:mm"));
        liveCourse.getParam().put("endTimeString", new DateTime(liveCourse.getEndTime()).toString("yyyy年MM月dd HH:mm"));
        //获取教师信息
        Teacher teacher = courseFeignClient.getTeacherLive(liveCourse.getTeacherId());
        //获取课程描述
        LiveCourseDescription courseDescription = liveCourseDescriptionService.getByCourseId(courseId);
        //封装进map，用于前端回显
        Map<String,Object> map = new HashMap<>();
        map.put("liveCourse", liveCourse);
        map.put("liveStatus", this.getLiveStatus(liveCourse));
        map.put("teacher", teacher);
        if(null != courseDescription) {
            map.put("description", courseDescription.getDescription());
        } else {
            map.put("description", "");
        }
        return map;
    }

    /**
     * 直播状态 0：未开始 1：直播中 2：直播结束  通过时间判断
     * @param liveCourseVo
     * @return
     */
    private Integer getLiveStatus(LiveCourse liveCourseVo) {
        int liveStatus = 0;
        Date curTime = new Date();
        //参数说明 前面是开始时间 ，后面是结束时间 开始>结束则返回true
        if(DateUtil.dateCompare(curTime, liveCourseVo.getStartTime())) {
            liveStatus = 0;
        } else if(DateUtil.dateCompare(curTime, liveCourseVo.getEndTime())) {
            liveStatus = 1;
        } else {
            liveStatus = 2;
        }
        return liveStatus;
    }

    //修改欢透云上的配置信息
    private void updateLifeConfig(LiveCourseConfigVo liveCourseConfigVo) {
        LiveCourse liveCourse =
                baseMapper.selectById(liveCourseConfigVo.getLiveCourseId());
        //封装平台方法需要参数
        //参数设置
        HashMap<Object,Object> options = new HashMap<Object, Object>();
        //界面模式
        options.put("pageViewMode", liveCourseConfigVo.getPageViewMode());
        //观看人数开关
        JSONObject number = new JSONObject();
        number.put("enable", liveCourseConfigVo.getNumberEnable());
        options.put("number", number.toJSONString());
        //观看人数开关
        JSONObject store = new JSONObject();
        store.put("enable", liveCourseConfigVo.getStoreEnable());
        store.put("type", liveCourseConfigVo.getStoreType());
        options.put("store", number.toJSONString());
        //商城列表
        List<LiveCourseGoods> liveCourseGoodsList = liveCourseConfigVo.getLiveCourseGoodsList();
        if(!CollectionUtils.isEmpty(liveCourseGoodsList)) {
            //liveCourseGoods 转换成 liveCourseGoodsView
            List<LiveCourseGoodsView> liveCourseGoodsViewList = new ArrayList<>();
            for(LiveCourseGoods liveCourseGoods : liveCourseGoodsList) {
                LiveCourseGoodsView liveCourseGoodsView = new LiveCourseGoodsView();
                BeanUtils.copyProperties(liveCourseGoods, liveCourseGoodsView);
                liveCourseGoodsViewList.add(liveCourseGoodsView);
            }
            JSONObject goodsListEdit = new JSONObject();
            goodsListEdit.put("status", "0");
            options.put("goodsListEdit ", goodsListEdit.toJSONString());
            options.put("goodsList", JSON.toJSONString(liveCourseGoodsViewList));//放进商品信息
        }
        try {
            String res = mtCloudClient.courseUpdateLifeConfig(liveCourse.getCourseId().toString(), options);
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            //检验结果
            if(Integer.parseInt(commonResult.getCode()) != MTCloud.CODE_SUCCESS) {
                throw new GgktException(20001,"修改配置信息失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
