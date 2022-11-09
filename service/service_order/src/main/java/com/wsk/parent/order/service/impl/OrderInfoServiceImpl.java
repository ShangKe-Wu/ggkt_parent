package com.wsk.parent.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsk.ggkt.client.CourseFeignClient;
import com.wsk.ggkt.client.UserInfoFeignClient;
import com.wsk.ggkt.model.activity.CouponInfo;
import com.wsk.ggkt.model.order.OrderDetail;
import com.wsk.ggkt.model.order.OrderInfo;
import com.wsk.ggkt.model.user.UserInfo;
import com.wsk.ggkt.model.vod.Course;
import com.wsk.ggkt.vo.order.OrderFormVo;
import com.wsk.ggkt.vo.order.OrderInfoQueryVo;
import com.wsk.ggkt.vo.order.OrderInfoVo;
import com.wsk.ggtk.client.CouponInfoFeignClient;
import com.wsk.parent.order.mapper.OrderInfoMapper;
import com.wsk.parent.order.service.OrderDetailService;
import com.wsk.parent.order.service.OrderInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsk.serviceutil.exception.GgktException;
import com.wsk.serviceutil.utils.AuthContextHolder;
import com.wsk.serviceutil.utils.OrderNoUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务实现类
 * </p>
 *
 * @author wsk
 * @since 2022-08-20
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private CourseFeignClient courseFeignClient;
    @Autowired
    private UserInfoFeignClient userInfoFeignClient;
    @Autowired
    private CouponInfoFeignClient couponInfoFeignClient;

    //获取分页列表
    @Override
    public Map<String, Object> findPageOrderInfo(Page<OrderInfo> pageParam, OrderInfoQueryVo orderInfoQueryVo) {
        //获取VO中数据，并进行封装，查询
        Long userId = orderInfoQueryVo.getUserId();
        String nickName = orderInfoQueryVo.getNickName();
        String phone = orderInfoQueryVo.getPhone();
        Integer orderStatus = orderInfoQueryVo.getOrderStatus();
        String outTradeNo = orderInfoQueryVo.getOutTradeNo();
        String province = orderInfoQueryVo.getProvince();
        String createTimeBegin = orderInfoQueryVo.getCreateTimeBegin();
        String createTimeEnd = orderInfoQueryVo.getCreateTimeEnd();
        //判断条件值是否为空，不为空，进行条件封装
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(orderStatus)) {
            wrapper.eq("order_status",orderStatus);
        }
        if(!StringUtils.isEmpty(userId)) {
            wrapper.eq("user_id",userId);
        }
        if(!StringUtils.isEmpty(outTradeNo)) {
            wrapper.eq("out_trade_no",outTradeNo);
        }
        if(!StringUtils.isEmpty(phone)) {
            wrapper.eq("phone",phone);
        }
        if(!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time",createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time",createTimeEnd);
        }
        if(!StringUtils.isEmpty(nickName)) {
            wrapper.eq("nick_name",nickName);
        }
        if(!StringUtils.isEmpty(province)) {
            wrapper.eq("province",province);
        }
        //实现分页查询
        Page<OrderInfo> pages = orderInfoMapper.selectPage(pageParam, wrapper);
        long totalCount = pages.getTotal();
        long pageCount = pages.getPages();
        List<OrderInfo> records = pages.getRecords();
        //订单里面包含详情内容，根据订单ID查询详情表中的数据，并封装
        //TODO 优化：不循环遍历
        for(OrderInfo orderInfo:records){
            this.getOrderDetail(orderInfo);
        }
        //封装到MAP中
        Map<String,Object> map = new HashMap<>();
        map.put("total",totalCount);
        map.put("pageCount",pageCount);
        map.put("records",records);
        return map;
    }

    //新增点播课程订单（生成订单并返回订单号）
    //TODO 这里要用SeaTa进行事务管理  -- GlobalTransactional
    @Override
    public Long submitOrder(OrderFormVo orderFormVo) {
        //获取生成订单条件值
        Long courseId = orderFormVo.getCourseId();
        Long couponId = orderFormVo.getCouponId();
        Long userId = AuthContextHolder.getUserId();
        //判断当前用户是否已经购买过该课程
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getCourseId,courseId);
        wrapper.eq(OrderDetail::getUserId,userId);
        OrderDetail orderDetailExists = orderDetailService.getOne(wrapper);
        if(orderDetailExists != null) {
            return orderDetailExists.getId();//订单已经存在，直接返回订单id
        }
        // 根据课程id查询课程信息
        Course course = courseFeignClient.getById(courseId);
        if(course == null) {
            throw new GgktException(20001,"课程不存在");
        }
        // 根据用户id查询用户信息
        UserInfo userInfo = userInfoFeignClient.getById(userId);
        if(userInfo == null) {
            throw new GgktException(20001,"用户不存在");
        }
        // 根据优惠券id查询优惠券信息 精度问题，用bigDecimal
        BigDecimal couponReduce = new BigDecimal(0);
        if(couponId != null) {
            CouponInfo couponInfo = couponInfoFeignClient.getById(couponId);
            couponReduce = couponInfo.getAmount();
        }
        // 封装订单生成需要数据到对象，完成添加订单
        //1 封装数据到OrderInfo对象里面，添加订单基本信息表
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setNickName(userInfo.getNickName());
        orderInfo.setPhone(userInfo.getPhone());
        orderInfo.setProvince(userInfo.getProvince());
        orderInfo.setOriginAmount(course.getPrice());
        orderInfo.setCouponReduce(couponReduce);
        orderInfo.setFinalAmount(orderInfo.getOriginAmount().subtract(orderInfo.getCouponReduce()));
        orderInfo.setOutTradeNo(OrderNoUtils.getOrderNo());
        orderInfo.setTradeBody(course.getTitle());
        orderInfo.setOrderStatus("0");
        baseMapper.insert(orderInfo);

        //2 封装数据到OrderDetail对象里面，添加订单详情信息表
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderInfo.getId());
        orderDetail.setUserId(userId);
        orderDetail.setCourseId(courseId);
        orderDetail.setCourseName(course.getTitle());
        orderDetail.setCover(course.getCover());
        orderDetail.setOriginAmount(course.getPrice());
        orderDetail.setCouponReduce(new BigDecimal(0));
        orderDetail.setFinalAmount(orderDetail.getOriginAmount().subtract(orderDetail.getCouponReduce()));
        orderDetailService.save(orderDetail);
        // 更新优惠券状态
        if(null != orderFormVo.getCouponUseId()) {
            couponInfoFeignClient.updateCouponInfoUseStatus(orderFormVo.getCouponUseId(), orderInfo.getId());
        }

        // 返回订单id
        return orderInfo.getId();
    }

    //获取订单信息
    @Override
    public OrderInfoVo getOrderInfoById(Long id) {

        //id查询订单基本信息和详情信息
        OrderInfo orderInfo = baseMapper.selectById(id);
        OrderDetail orderDetail = orderDetailService.getById(id);

        //封装OrderInfoVo
        OrderInfoVo orderInfoVo = new OrderInfoVo();
        //VO继承了orderInfo 所以需要copy
        BeanUtils.copyProperties(orderInfo,orderInfoVo);
        orderInfoVo.setCourseId(orderDetail.getCourseId());
        orderInfoVo.setCourseName(orderDetail.getCourseName());
        orderInfoVo.setCover(orderDetail.getCover());
        return orderInfoVo;
    }

    //更新订单状态为已支付
    @Override
    public void updateOrderStatus(String out_trade_no) {
        //根据订单号查询订单
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOutTradeNo,out_trade_no);
        OrderInfo orderInfo = baseMapper.selectOne(wrapper);

        //设置订单状态
        orderInfo.setOrderStatus("1");

        //调用方法更新
        baseMapper.updateById(orderInfo);
    }

    //查询订单详情数据
    private OrderInfo getOrderDetail(OrderInfo orderInfo){
        //通过订单ID查询订单详情表中的数据
        Long id = orderInfo.getId();
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(OrderDetail::getCourseId,id);
        OrderDetail one = orderDetailService.getOne(queryWrapper);
        if(one!=null){
            orderInfo.getParam().put("courseName",one.getCourseName());
        }
        return orderInfo;
    }
}
