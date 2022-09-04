package com.wsk.parent.live.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wsk.ggkt.model.live.LiveCourseAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 直播课程账号表（受保护信息） Mapper 接口
 * </p>
 *
 * @author wsk
 * @since 2022-08-22
 */
@Mapper
public interface LiveCourseAccountMapper extends BaseMapper<LiveCourseAccount> {

}
