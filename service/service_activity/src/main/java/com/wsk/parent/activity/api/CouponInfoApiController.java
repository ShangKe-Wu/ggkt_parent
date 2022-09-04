package com.wsk.parent.activity.api;

import com.wsk.ggkt.model.activity.CouponInfo;
import com.wsk.parent.activity.service.CouponInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:WuShangke
 * @create:2022/8/21-22:06
 */
@RestController
@RequestMapping("/api/activity/couponInfo")
public class CouponInfoApiController {
    @Autowired
    private CouponInfoService couponInfoService;

    //获取优惠券(远程调用，用户领取优惠券) 这里调用的coupon_info表
    @GetMapping(value = "/inner/getById/{couponId}")
    public CouponInfo getById(@PathVariable("couponId") Long couponId) {
        CouponInfo couponInfo = couponInfoService.getById(couponId);
        return couponInfo;
    }


    //更新优惠券状态(远程调用) 这里调用的coupon_use TODO 感觉有bug 这里应该还需要用户ID吧?感觉这里的优惠券接口怪怪的
    @GetMapping(value = "/inner/updateCouponInfoUseStatus/{couponUseId}/{orderId}")
    public Boolean updateCouponInfoUseStatus(@PathVariable("couponUseId") Long couponUseId, @PathVariable("orderId") Long orderId) {
        couponInfoService.updateCouponInfoUseStatus(couponUseId, orderId);
        return true;
    }
}
