package com.wsk.parent.vod.controller;

import com.wsk.parent.vod.service.FileService;
import com.wsk.serviceutil.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author:WuShangke
 * @create:2022/8/19-17:11
 */
@Api(tags = "文件上传接口")
@RestController
@RequestMapping("/admin/vod/file")
public class FileUploadController {
    @Autowired
    private FileService fileService;

    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public Result upload(MultipartFile file){
        String url = fileService.upload(file);
        return Result.ok(url).message("文件上传成功");
    }
}
