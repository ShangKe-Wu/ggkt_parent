package com.wsk.parent.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.xml.internal.bind.v2.TODO;
import com.wsk.ggkt.client.UserInfoFeignClient;
import com.wsk.ggkt.model.activity.CouponInfo;
import com.wsk.ggkt.model.activity.CouponUse;
import com.wsk.ggkt.model.user.UserInfo;
import com.wsk.ggkt.vo.activity.CouponUseQueryVo;
import com.wsk.parent.activity.mapper.CouponInfoMapper;
import com.wsk.parent.activity.service.CouponInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsk.parent.activity.service.CouponUseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 优惠券信息 服务实现类
 * </p>
 *
 * @author wsk
 * @since 2022-08-20
 */
@Service
public class CouponInfoServiceImpl extends ServiceImpl<CouponInfoMapper, CouponInfo> implements CouponInfoService {
    @Autowired
    private CouponUseService couponUseService;
    @Autowired
    private UserInfoFeignClient userInfoFeignClient;

    //按条件获取分页列表(用户获得的优惠券)
    @Override
    public IPage<CouponUse> selectCouponUsePage(Page<CouponUse> pageParam, CouponUseQueryVo couponUseQueryVo) {
        //查询VO信息，并进行封装
        Long couponId = couponUseQueryVo.getCouponId();
        String couponStatus = couponUseQueryVo.getCouponStatus();
        String getTimeBegin = couponUseQueryVo.getGetTimeBegin();
        String getTimeEnd = couponUseQueryVo.getGetTimeEnd();
        //条件查询数据库
        QueryWrapper<CouponUse> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(couponId)) {
            wrapper.eq("coupon_id",couponId);
        }
        if(!StringUtils.isEmpty(couponStatus)) {
            wrapper.eq("coupon_status",couponStatus);
        }
        if(!StringUtils.isEmpty(getTimeBegin)) {
            wrapper.ge("get_time",getTimeBegin);
        }
        if(!StringUtils.isEmpty(getTimeEnd)) {
            wrapper.le("get_time",getTimeEnd);
        }
        //调用方法条件分页查询
        IPage<CouponUse> pageModel = couponUseService.page(pageParam, wrapper);
        List<CouponUse> couponUseList = pageModel.getRecords();
        //遍历,封装用户信息（手机号和昵称）
        //TODO 优化：不循环调用?
        couponUseList.stream().forEach(item->{
            this.getUserInfoById(item);
        });
        return pageModel;
    }

    //更新优惠券状态(远程调用)
    @Override
    public void updateCouponInfoUseStatus(Long couponUseId, Long orderId) {
        //正确写法应该是根据 用户ID 和 优惠券ID 和 优惠券状态 查询数据库，查询优惠券是否存在,查询优惠券数量，如果有多张优惠券，则从list中选一个
        //判断用户是否有优惠券？以及优惠券数量是否足够？还能不能使用？
        //下面是老师写的，感觉有bug
        CouponUse couponUse = new CouponUse();
        couponUse.setId(couponUseId);
        couponUse.setOrderId(orderId);
        couponUse.setCouponStatus("1");
        couponUse.setUsingTime(new Date());
        couponUseService.updateById(couponUse);//这里直接根据主键更新了...
    }


    //获取用户信息
    private void getUserInfoById(CouponUse item) {
        Long userId = item.getUserId();
        if(!StringUtils.isEmpty(userId)){
            UserInfo user = userInfoFeignClient.getById(userId);
            if(user!=null){
                item.getParam().put("nickName",user.getNickName());
                item.getParam().put("phone",user.getPhone());
            }
        }

    }
}
