package com.wsk.parent.live.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wsk.ggkt.model.live.LiveCourseGoods;
import com.wsk.ggkt.vo.live.LiveCourseConfigVo;

import java.util.List;

/**
 * <p>
 * 直播课程关联推荐表 服务类
 * </p>
 *
 * @author wsk
 * @since 2022-08-22
 */
public interface LiveCourseGoodsService extends IService<LiveCourseGoods> {

    //通过课程ID查询商品列表
    List<LiveCourseGoods> getGoodsListCourseId(Long id);
    //更新商品表（先移除原本所有的，再添加进去）
    void updateGoodList(LiveCourseConfigVo liveCourseConfigVo);
}
