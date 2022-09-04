package com.wsk.ggkt.model.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(description = "MqRepeatRecord")
@TableName("mq_repeat_record")
@AllArgsConstructor
@NoArgsConstructor
public class MqRepeatRecord extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "业务编号")
	@TableField("business_no")
	private String businessNo;

}