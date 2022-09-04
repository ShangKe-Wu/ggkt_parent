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
@ApiModel(description = "Chapter")
@TableName("chapter")
@AllArgsConstructor
@NoArgsConstructor
public class Chapter extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "课程ID")
	@TableField("course_id")
	private Long courseId;

	@ApiModelProperty(value = "章节名称")
	@TableField("title")
	private String title;

	@ApiModelProperty(value = "显示排序")
	@TableField("sort")
	private Integer sort;

}