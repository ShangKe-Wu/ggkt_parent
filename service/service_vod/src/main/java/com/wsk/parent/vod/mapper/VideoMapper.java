package com.wsk.parent.vod.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wsk.ggkt.model.vod.Video;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 课程视频 Mapper 接口
 * </p>
 *
 * @author wsk
 * @since 2022-08-14
 */
@Mapper
public interface VideoMapper extends BaseMapper<Video> {

}
