package com.wsk.parent.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wsk.ggkt.model.user.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author wsk
 * @since 2022-08-20
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}
