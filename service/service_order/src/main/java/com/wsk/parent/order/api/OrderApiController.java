package com.wsk.parent.order.api;

import com.wsk.ggkt.vo.order.OrderFormVo;
import com.wsk.ggkt.vo.order.OrderInfoVo;
import com.wsk.parent.order.service.OrderInfoService;
import com.wsk.serviceutil.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author:WuShangke
 * @create:2022/8/21-21:44
 */
@RestController
@RequestMapping("api/order/orderInfo")
public class OrderApiController {
    @Autowired
    private OrderInfoService orderInfoService;

    //新增点播课程订单（生成订单并返回订单号）
    @PostMapping("/submitOrder")
    public Result submitOrder(@RequestBody OrderFormVo orderFormVo){
        //返回订单ID
        Long orderId = orderInfoService.submitOrder(orderFormVo);
        return Result.ok(orderId);
    }

    //获取订单信息
    @GetMapping("getInfo/{id}")
    public Result getInfo(@PathVariable Long id) {
        OrderInfoVo orderInfoVo = orderInfoService.getOrderInfoById(id);
        return Result.ok(orderInfoVo);
    }
}
