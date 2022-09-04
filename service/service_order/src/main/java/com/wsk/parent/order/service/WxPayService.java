package com.wsk.parent.order.service;

import java.util.Map;

/**
 * @author:WuShangke
 * @create:2022/8/22-11:52
 */
public interface WxPayService {
    //下单，微信支付
    Map<String, Object> createJsapi(String orderNo);

    //根据订单号调用微信接口查询支付状态
    Map<String, String> queryPayStatus(String orderNo);
}
