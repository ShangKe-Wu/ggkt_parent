package com.wsk.parent.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wsk.ggkt.model.order.OrderInfo;
import com.wsk.ggkt.vo.order.OrderFormVo;
import com.wsk.ggkt.vo.order.OrderInfoQueryVo;
import com.wsk.ggkt.vo.order.OrderInfoVo;

import java.util.Map;

/**
 * <p>
 * 订单表 订单表 服务类
 * </p>
 *
 * @author wsk
 * @since 2022-08-20
 */
public interface OrderInfoService extends IService<OrderInfo> {

    //获取分页列表
    Map<String, Object> findPageOrderInfo(Page<OrderInfo> pageParam, OrderInfoQueryVo orderInfoQueryVo);

    //新增点播课程订单（生成订单并返回订单号）
    Long submitOrder(OrderFormVo orderFormVo);

    //获取订单信息
    OrderInfoVo getOrderInfoById(Long id);

    //更新订单状态为已支付
    void updateOrderStatus(String out_trade_no);
}
