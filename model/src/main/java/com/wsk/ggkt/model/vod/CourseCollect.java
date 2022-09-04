package com.wsk.ggkt.model.vod;

import com.wsk.ggkt.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(description = "CourseCollect")
@TableName("course_collect")
@AllArgsConstructor
@NoArgsConstructor
public class CourseCollect extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "课程讲师ID")
	@TableField("course_id")
	private Long courseId;

	@ApiModelProperty(value = "会员ID")
	@TableField("user_id")
	private Long userId;

}