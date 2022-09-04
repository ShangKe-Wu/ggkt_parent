package com.wsk.ggkt.vo.live;

import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiveVisitorQueryVo {
	
	@ApiModelProperty(value = "直播课程id")
	private Long liveCourseId;

}

