package com.wsk.parent.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wsk.ggkt.model.wechat.Menu;
import com.wsk.ggkt.vo.wechat.MenuVo;
import com.wsk.parent.wechat.mapper.MenuMapper;
import com.wsk.parent.wechat.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsk.serviceutil.exception.GgktException;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单明细 订单明细 服务实现类
 * </p>
 *
 * @author wsk
 * @since 2022-08-21
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private WxMpService wxMpService;

    //获取菜单列表，按照一级、二级菜单封装
    @Override
    public List<MenuVo> findMenuInfo() {
        //最终返回的List
        List<MenuVo> result = new ArrayList<>();
        //先查询所有的菜单
        List<Menu> menuList = menuMapper.selectList(null);
        //根据上一步结果,提取出一级菜单
        List<Menu> menuOne = menuList.stream().filter(menu -> menu.getParentId().longValue() == 0).collect(Collectors.toList());
        //遍历一级菜单，并把一级菜单下的二级菜单封装到VO对象中
        for(Menu oneMenu:menuOne){
            MenuVo vo = new MenuVo();
            //复制属性
            BeanUtils.copyProperties(oneMenu,vo);
            //找出相应的二级菜单列表
            List<Menu> twoMenuList = menuList.stream().filter(menu -> menu.getParentId().equals(oneMenu.getId()) ).collect(Collectors.toList());
            //二级菜单也要转换成VO
            List<MenuVo> children = new ArrayList<>();
            for(Menu twoMenu : twoMenuList){
                MenuVo twoVo = new MenuVo();
                BeanUtils.copyProperties(twoMenu,twoVo);
                children.add(twoVo);
            }
            //把二级菜单列表封装到一级菜单下
            vo.setChildren(children);
            //把VO添加到结果集合中
            result.add(vo);
        }
        return result;
    }

    //获取所有一级菜单
    @Override
    public List<Menu> findMenuOneInfo() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,0);
        List<Menu> menus = menuMapper.selectList(queryWrapper);
        return menus;
    }

    //同步公众号菜单，结构参考公众号开发官方文档
    @Override
    public void syncMenu() {
        //获取所有菜单数据
        List<MenuVo> menuInfo = this.findMenuInfo();
        //封装到button里面，数组结构
        JSONArray buttonList = new JSONArray();
        //遍历菜单
        for(MenuVo menuVo:menuInfo){
            //json对象 一级菜单
            JSONObject oneMenu = new JSONObject();
            oneMenu.put("name",menuVo.getName());
            //json数组 一级菜单下 的 二级菜单
            JSONArray twoMenuList = new JSONArray();
            //遍历menuVo下的二级数组
            for(MenuVo twoMenu:menuVo.getChildren()){
                //封装好信息后加入到二级菜单数组
                JSONObject view = new JSONObject();
                view.put("type",twoMenu.getType());
                view.put("name",twoMenu.getName());
                //根据类型进行封装
                //页面跳转
                if(twoMenu.getType().equals("view")){
                    //这里改成自己的域名地址 √
                    view.put("url","http://c5819l1239.oicp.vip/#"+twoMenu.getUrl());
                }
                else {
                    view.put("key",twoMenu.getMeunKey());
                }
                twoMenuList.add(view);
            }
            oneMenu.put("sub_button",twoMenuList);
            buttonList.add(oneMenu);
        }
        //封装最外层的button格式
        JSONObject button = new JSONObject();
        button.put("button",buttonList);
        //上传到公众号平台上
        try {
            String menuId =
                    this.wxMpService.getMenuService().menuCreate(button.toJSONString());
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new GgktException(20001,"公众号菜单同步失败");
        }
    }

    //删除菜单
    @Override
    public void removeMenu() {
        //删除所有的菜单
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new GgktException(20001,"删除菜单失败");
        }
    }
}
