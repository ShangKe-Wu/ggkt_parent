package com.wsk.parent.wechat.service;

import com.wsk.ggkt.model.wechat.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wsk.ggkt.vo.wechat.MenuVo;

import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 服务类
 * </p>
 *
 * @author wsk
 * @since 2022-08-21
 */
public interface MenuService extends IService<Menu> {
    //获取菜单列表，按照一级、二级菜单封装
    List<MenuVo> findMenuInfo();

    //获取所有一级菜单
    List<Menu> findMenuOneInfo();

    //同步公众号菜单
    void syncMenu();

    //删除菜单
    void removeMenu();
}
