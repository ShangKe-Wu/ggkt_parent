package com.wsk.parent.vod.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wsk.ggkt.model.vod.VideoVisitor;
import com.wsk.ggkt.vo.vod.VideoVisitorCountVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 视频来访者记录表 Mapper 接口
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@Mapper
public interface VideoVisitorMapper extends BaseMapper<VideoVisitor> {

    //统计数据：某个课程在某个时间段内的观看数据
    List<VideoVisitorCountVo> findCount(@Param("courseId") Long courseId,
                                        @Param("startDate") String startDate,
                                        @Param("endDate") String endDate);
}
