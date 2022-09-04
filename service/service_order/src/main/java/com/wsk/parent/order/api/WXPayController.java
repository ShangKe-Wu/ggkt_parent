package com.wsk.parent.order.api;

import com.wsk.parent.order.service.OrderInfoService;
import com.wsk.parent.order.service.WxPayService;
import com.wsk.serviceutil.exception.GgktException;
import com.wsk.serviceutil.result.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author:WuShangke
 * @create:2022/8/22-11:45
 */
@RestController
@RequestMapping("/api/order/wxPay")
public class WXPayController {
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private OrderInfoService orderInfoService;

    //微信支付模块只有正式号才能进行支付，测试号做不了，下面只是根据官方文档写出来的
    //下单，微信支付
    @GetMapping("/createJsapi/{orderNo}")
    public Result createJsapi(@PathVariable String orderNo){
        Map<String,Object> map = wxPayService.createJsapi(orderNo);
        return Result.ok(map);
    }

    //查询支付状态
    @GetMapping("/queryPayStatus/{orderNo}")
    public Result queryPayStatus(@PathVariable("orderNo") String orderNo) {
        //根据订单号调用微信接口查询支付状态
        Map<String,String> resultMap = wxPayService.queryPayStatus(orderNo);
        //判断支付是否成功：根据微信支付状态接口判断
        if(resultMap == null) {
            throw new GgktException(20001,"支付出错");
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))) {//成功
            //更新订单状态 ：已经支付
            String out_trade_no = resultMap.get("out_trade_no");
            orderInfoService.updateOrderStatus(out_trade_no);
            return Result.ok(null).message("支付成功");
        }
        return Result.ok(null).message("支付中");
    }
}
