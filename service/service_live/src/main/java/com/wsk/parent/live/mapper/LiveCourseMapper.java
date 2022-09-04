package com.wsk.parent.live.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wsk.ggkt.model.live.LiveCourse;
import com.wsk.ggkt.vo.live.LiveCourseVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 直播课程表 Mapper 接口
 * </p>
 *
 * @author wsk
 * @since 2022-08-22
 */
@Mapper
public interface LiveCourseMapper extends BaseMapper<LiveCourse> {

    List<LiveCourseVo> getLatelyList();
}
