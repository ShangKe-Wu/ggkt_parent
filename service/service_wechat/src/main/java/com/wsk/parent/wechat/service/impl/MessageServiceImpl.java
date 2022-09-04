package com.wsk.parent.wechat.service.impl;

import com.wsk.ggkt.client.CourseFeignClient;
import com.wsk.ggkt.model.vod.Course;
import com.wsk.parent.wechat.service.MessageService;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author:WuShangke
 * @create:2022/8/21-16:55
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private CourseFeignClient courseFeignClient;

    @Autowired
    private WxMpService wxMpService;

    //模板消息（订单支付成功），暂时写成固定值
    @Override
    @SneakyThrows
    public void pushPayMessage(long id) {
        //微信openid
        //TODO 这里openID的获取可以在User里面的授权登录里获取，也可以直接从数据库查
        String openid ="o9ZLp58SyNZm9OiEEfaqg2FaQy7s";
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(openid)//要推送的用户openid
                .templateId("cvCqvpLuTVX7h3B9U4fwxP2h4YnYWvLYIfqjpZrepdI")//模板id
                .url("http://c5819l1239.oicp.vip/#/pay/"+id)//点击模板消息要访问的网址  改成自己的域名 √
                .build();
        //3,如果是正式版发送消息，这里需要配置你的信息 TODO 完善成不是固定值
        templateMessage.addData(new WxMpTemplateData("first", "亲爱的用户：您有一笔订单支付成功。", "#272727"));
        templateMessage.addData(new WxMpTemplateData("keyword1", "1314520", "#272727"));//订单号
        templateMessage.addData(new WxMpTemplateData("keyword2", "java基础课程", "#272727"));//课程名
        templateMessage.addData(new WxMpTemplateData("keyword3", "2022-01-11", "#272727"));//当前日期
        templateMessage.addData(new WxMpTemplateData("keyword4", "100", "#272727"));//金额
        templateMessage.addData(new WxMpTemplateData("remark", "感谢你购买课程，如有疑问，随时咨询！", "#272727"));
        try {
            String msg = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }

    //接收消息
    @Override
    public String receiveMessage(Map<String, String> param) {
        String content = "";
        String msgType = param.get("MsgType");
        //判断什么类型消息
        switch (msgType) {
            case "text":   //普通文本类型，例如输入关键字java
                content = this.search(param);
                break;
            case "event":  //点击事件： 关注 、关于我们
                String event = param.get("Event");
                String eventKey = param.get("EventKey");
                if("subscribe".equals(event)) {//关注
                    content = this.subscribe(param);
                }
                else if("CLICK".equals(event) && "aboutUs".equals(eventKey)) { //关于我们
                    content = this.aboutUs(param);
                }
                else { //其他
                    content = "success";
                }
                break;
            default:  //其他情况
                content = "success";
        }

        return content;
    }



    //关于我们
    private String aboutUs(Map<String, String> param) {
        StringBuffer msg = this.text(param, "硅谷课堂现开设Java、HTML5前端+全栈、大数据、" +
                "全链路UI/UE设计、人工智能、大数据运维+Python自动化、" +
                "Android+HTML5混合开发等多门课程");
        return msg.toString();
    }

    //关注
    private String subscribe(Map<String, String> param) {
        return this.text(param,
                "感谢你关注“硅谷课堂”，可以根据关键字搜索您想看的视频教程，如：JAVA基础、Spring boot、大数据等")
                .toString();
    }

    //处理关键字,参考官方文档
    private String search(Map<String, String> param) {
        String fromusername = param.get("FromUserName");
        String tousername = param.get("ToUserName");
        String content = param.get("Content");
        //单位为秒，不是毫秒
        Long createTime = new Date().getTime() / 1000;
        StringBuffer text = new StringBuffer();

        List<Course> courseList = courseFeignClient.findByKeyword(content);

        if(CollectionUtils.isEmpty(courseList)) {
            text = this.text(param, "请重新输入关键字，没有匹配到相关视频课程");
        } else {
            //一次只能返回一个
            Random random = new Random();
            int num = random.nextInt(courseList.size());
            Course course = courseList.get(num);
            StringBuffer articles = new StringBuffer();
            articles.append("<item>");
            articles.append("<Title><![CDATA["+course.getTitle()+"]]></Title>");
            articles.append("<Description><![CDATA["+course.getTitle()+"]]></Description>");
            articles.append("<PicUrl><![CDATA["+course.getCover()+"]]></PicUrl>");
            //TODO url修改成自己的域名，不确定这个是要跳转到哪里啊, 是/admin/live/liveCourse/getInfo/{id}吗
            //c5819l1239.oicp.vip 感觉域名要改成这个?还是wsk.free.idcfengye.com,获取点播课程？还是直播
            articles.append("<Url><![CDATA[http://c5819l1239.oicp.vip/#/liveInfo/"+course.getId()+"]]></Url>");
            //<![CDATA[http://glkt.atguigu.cn/#/liveInfo/"+course.getId()+"]]>
            articles.append("</item>");

            text.append("<xml>");
            text.append("<ToUserName><![CDATA["+fromusername+"]]></ToUserName>");
            text.append("<FromUserName><![CDATA["+tousername+"]]></FromUserName>");
            text.append("<CreateTime><![CDATA["+createTime+"]]></CreateTime>");
            text.append("<MsgType><![CDATA[news]]></MsgType>");
            text.append("<ArticleCount><![CDATA[1]]></ArticleCount>");
            text.append("<Articles>");
            text.append(articles);
            text.append("</Articles>");
            text.append("</xml>");
        }
        return text.toString();
    }

    /**
     * 回复文本
     * @param param
     * @param content
     * @return
     */
    private StringBuffer text(Map<String, String> param, String content) {
        String fromusername = param.get("FromUserName");
        String tousername = param.get("ToUserName");
        //单位为秒，不是毫秒
        Long createTime = new Date().getTime() / 1000;
        StringBuffer text = new StringBuffer();
        text.append("<xml>");
        text.append("<ToUserName><![CDATA["+fromusername+"]]></ToUserName>");
        text.append("<FromUserName><![CDATA["+tousername+"]]></FromUserName>");
        text.append("<CreateTime><![CDATA["+createTime+"]]></CreateTime>");
        text.append("<MsgType><![CDATA[text]]></MsgType>");
        text.append("<Content><![CDATA["+content+"]]></Content>");
        text.append("</xml>");
        return text;
    }
}
