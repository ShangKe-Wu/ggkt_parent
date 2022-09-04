package com.wsk.parent.order.service.impl;


import com.wsk.ggkt.model.order.OrderDetail;
import com.wsk.parent.order.mapper.OrderDetailMapper;
import com.wsk.parent.order.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单明细 订单明细 服务实现类
 * </p>
 *
 * @author wsk
 * @since 2022-08-20
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
