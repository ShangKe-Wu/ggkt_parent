package com.wsk.parent.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wsk.ggkt.model.activity.CouponInfo;
import com.wsk.ggkt.model.activity.CouponUse;
import com.wsk.ggkt.vo.activity.CouponUseQueryVo;

/**
 * <p>
 * 优惠券信息 服务类
 * </p>
 *
 * @author wsk
 * @since 2022-08-20
 */
public interface CouponInfoService extends IService<CouponInfo> {
    //按条件获取分页列表(用户获得的优惠券)
    IPage<CouponUse> selectCouponUsePage(Page<CouponUse> pageParam, CouponUseQueryVo couponUseQueryVo);
    //更新优惠券状态(远程调用)
    void updateCouponInfoUseStatus(Long couponUseId, Long orderId);
}
