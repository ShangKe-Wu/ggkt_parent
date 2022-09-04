package com.wsk.parent.activity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wsk.ggkt.model.activity.CouponUse;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 优惠券领用表 Mapper 接口
 * </p>
 *
 * @author wsk
 * @since 2022-08-20
 */
@Mapper
public interface CouponUseMapper extends BaseMapper<CouponUse> {

}
