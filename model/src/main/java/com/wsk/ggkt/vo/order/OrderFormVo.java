package com.wsk.ggkt.vo.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderFormVo  {

	@ApiModelProperty(value = "课程id")
	private Long courseId;

	@ApiModelProperty(value = "优惠券id")
	private Long couponId;

	@ApiModelProperty(value = "优惠券领取表id")
	private Long couponUseId;
}

