package com.wsk.ggkt.vo.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "领取优惠券vo")
public class GiveCouponVo {

	@ApiModelProperty(value = "购物券类型")
	private Integer couponType;

	@ApiModelProperty(value = "优惠卷名字")
	private Long userId;

}