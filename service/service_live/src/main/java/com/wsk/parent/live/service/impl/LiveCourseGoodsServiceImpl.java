package com.wsk.parent.live.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wsk.ggkt.model.live.LiveCourseGoods;
import com.wsk.ggkt.vo.live.LiveCourseConfigVo;
import com.wsk.parent.live.mapper.LiveCourseGoodsMapper;
import com.wsk.parent.live.service.LiveCourseGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 直播课程关联推荐表 服务实现类
 * </p>
 *
 * @author wsk
 * @since 2022-08-22
 */
@Service
public class LiveCourseGoodsServiceImpl extends ServiceImpl<LiveCourseGoodsMapper, LiveCourseGoods> implements LiveCourseGoodsService {

    //通过课程ID查询商品列表
    @Override
    public List<LiveCourseGoods> getGoodsListCourseId(Long id) {
        LambdaQueryWrapper<LiveCourseGoods> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(LiveCourseGoods::getLiveCourseId,id);
        List<LiveCourseGoods> liveCourseGoods = baseMapper.selectList(queryWrapper);
        return liveCourseGoods;
    }

    //更新商品表（先移除原本所有的，再添加进去）
    @Override
    public void updateGoodList(LiveCourseConfigVo liveCourseConfigVo) {

    }
}
