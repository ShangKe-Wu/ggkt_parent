package com.wsk.parent.vod.service;

import java.io.InputStream;
import java.util.Map;

/**
 * @author:WuShangke
 * @create:2022/8/20-17:23
 */
public interface VodService {
    //上传视频
    String uploadVideo(InputStream inputStream, String originalFilename);
    //删除视频
    void removeVideo(String id);

    //点播视频播放接口，获取播放凭证
    Map<String, Object> getPlayAuth(Long courseId, Long videoId);
}
