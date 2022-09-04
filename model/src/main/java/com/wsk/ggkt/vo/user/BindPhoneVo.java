package com.wsk.ggkt.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BindPhoneVo {
	
	@ApiModelProperty(value = "手机号")
	private String phone;

	@ApiModelProperty(value = "验证码")
	private String code;

}

