package com.wsk.parent.order.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wsk.ggkt.model.order.OrderInfo;
import com.wsk.ggkt.vo.order.OrderInfoQueryVo;
import com.wsk.parent.order.service.OrderInfoService;
import com.wsk.serviceutil.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 订单表 订单表 前端控制器
 * </p>
 *
 * @author wsk
 * @since 2022-08-20
 */
@RestController
@RequestMapping(value="/admin/order/orderInfo")
public class OrderInfoController {
    @Autowired
    OrderInfoService orderInfoService;

    //获取分页列表
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page, @PathVariable Long limit,
                        OrderInfoQueryVo orderInfoQueryVo){
        Page<OrderInfo> pageParam = new Page<>(page, limit);
        Map<String,Object> map = orderInfoService.findPageOrderInfo(pageParam, orderInfoQueryVo);
        return Result.ok(map);
    }
}

