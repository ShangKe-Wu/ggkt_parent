package com.wsk.parent.vod.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author:WuShangke
 * @create:2022/8/19-17:01
 */
public interface FileService {
    //文件上传
    String upload(MultipartFile multipartFile);
}
