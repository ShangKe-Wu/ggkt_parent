package com.wsk.ggtk.client;

import com.wsk.ggkt.model.activity.CouponInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author:WuShangke
 * @create:2022/8/21-22:34
 */
@FeignClient(value = "service-activity")
public interface CouponInfoFeignClient {
    //获取优惠券(远程调用，用户领取优惠券) 这里调用的coupon_info表
    @GetMapping(value = "/api/activity/couponInfo/inner/getById/{couponId}")
    CouponInfo getById(@PathVariable("couponId") Long couponId);

    ////更新优惠券状态(远程调用)这里调用的coupon_use表
    @GetMapping(value = "/api/activity/couponInfo/inner/updateCouponInfoUseStatus/{couponUseId}/{orderId}")
    Boolean updateCouponInfoUseStatus(@PathVariable("couponUseId") Long couponUseId, @PathVariable("orderId") Long orderId);
}
