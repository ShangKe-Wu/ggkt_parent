package com.wsk.parent.wechat.controller;


import com.alibaba.fastjson.JSONObject;
import com.wsk.ggkt.model.wechat.Menu;
import com.wsk.ggkt.vo.wechat.MenuVo;
import com.wsk.parent.wechat.service.MenuService;
import com.wsk.parent.wechat.utils.ConstantPropertiesUtil;
import com.wsk.parent.wechat.utils.HttpClientUtils;
import com.wsk.serviceutil.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 订单明细 订单明细 前端控制器
 * </p>
 *
 * @author wsk
 * @since 2022-08-21
 */
@RestController
@RequestMapping("/admin/wechat/menu")
public class MenuController {
//weixin-java-mp 是封装好了的微信接口客户端，使用起来很方便，后续就使用weixin-java-mp处理微信平台接口。
    @Autowired
    private MenuService menuService;

    //删除所有菜单
    @DeleteMapping("/removeMenu")
    public Result removeMenu(){
        menuService.removeMenu();
        return Result.ok();
    }

    //同步公众号菜单，通过微信封装的WxMpService
    @GetMapping("/syncMenu")
    public Result createMenu(){
        menuService.syncMenu();
        return Result.ok();
    }

    //获取access_token，用于操作公众号(公众号操作接口需要access_token)，只是了解底层，上面那个同步功能内部已经给我们封装好了获取access_token的方法
    //通过httpClient发送get请求获取（参考官方文档）
    @GetMapping("/getAccessToken")
    public Result getAccessToken(){
        try {
            //拼接请求地址
            StringBuffer buffer = new StringBuffer();
            buffer.append("https://api.weixin.qq.com/cgi-bin/token");
            buffer.append("?grant_type=client_credential");
            buffer.append("&appid=%s");
            buffer.append("&secret=%s");
            //请求地址设置参数
            String url = String.format(buffer.toString(),
                    ConstantPropertiesUtil.ACCESS_KEY_ID,
                    ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            //发送http请求
            String tokenString = HttpClientUtils.get(url);
            //获取access_token,转换成jason对象
            JSONObject jsonObject = JSONObject.parseObject(tokenString);
            String access_token = jsonObject.getString("access_token");
            //返回
            return Result.ok(access_token);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }

    //获取菜单列表，按照一级、二级菜单封装
    @GetMapping("/findMenuInfo")
    public Result findMenuInfo() {
        List<MenuVo> list = menuService.findMenuInfo();
        return Result.ok(list);
    }

    //获取所有一级菜单(parentId为0的就是一级菜单)
    @GetMapping("/findOneMenuInfo")
    public Result findOneMenuInfo() {
        List<Menu> list = menuService.findMenuOneInfo();
        return Result.ok(list);
    }

    //根据ID获取菜单项
    @GetMapping("/get/{id}")
    public Result get(@PathVariable Long id) {
        Menu menu = menuService.getById(id);
        return Result.ok(menu);
    }

    //新增菜单项
    @PostMapping("/save")
    public Result save(@RequestBody Menu menu) {
        menuService.save(menu);
        return Result.ok(null);
    }

    //修改菜单项
    @PutMapping("/update")
    public Result updateById(@RequestBody Menu menu) {
        menuService.updateById(menu);
        return Result.ok(null);
    }

    //删除菜单项
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        menuService.removeById(id);
        return Result.ok(null);
    }

    //批量删除菜单项
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        menuService.removeByIds(idList);
        return Result.ok(null);
    }
}

