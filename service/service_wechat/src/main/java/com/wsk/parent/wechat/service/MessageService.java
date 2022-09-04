package com.wsk.parent.wechat.service;

import java.util.Map;

/**
 * @author:WuShangke
 * @create:2022/8/21-16:55
 */
public interface MessageService {
    //接收消息
    String receiveMessage(Map<String, String> param);

    //模板消息
    void pushPayMessage(long id);
}
